package com.ssafy.mokkoji.common.chat.controller;

import com.ssafy.mokkoji.common.chat.domain.request.MessageDto;
import com.ssafy.mokkoji.common.chat.domain.response.MessageResponse;
import com.ssafy.mokkoji.common.chat.service.ChatService;
import com.ssafy.mokkoji.game.talkbody.dto.TalkBodyInGame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ChatController {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ChatService chatService;

    // message 매핑으로 들어옴 /pub/message 프론트에서 보낼 때 백에서 받을 때는 /roomId
    @MessageMapping("/{roomId}")
    public void receive(MessageDto messageDto) {
        log.info("get Message {}", messageDto);
        String answer = "";
        String content = "";
        boolean ans = false;
        List<Map<String, Object>> part = null;
        if (messageDto.getType() == MessageDto.Type.THEME) {
            Map<String, String> responseMap = getQuestion(messageDto.getRoomId());

            if (!responseMap.isEmpty()) {
                content = responseMap.get("subject") + responseMap.get("selectedElement");
                answer = responseMap.get("selectedElement");
            }
        }

        if (!answer.isEmpty()){
            ans = findWord(answer,messageDto.getContent()); // true 정답 false 오답
        }


//  게임 끝났을 때 순위 판별
        if (messageDto.getType() == MessageDto.Type.END){
            part = getParticipantsOrderByScore();
        }

        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        // 정답 맞춤 스코어 1 증가 및 성공했다는 메세지와 새로운 주제 보내줌
        if (ans){
            String query = "UPDATE participant " +
                    "SET score = score + 1 " +
                    "WHERE room_id = ? AND user_nickname = ?";
            jdbcTemplate.update(query, messageDto.getRoomId() , messageDto.getUserNickname());

            MessageResponse successMessage = MessageResponse.builder()
                    .roomId(messageDto.getRoomId())
                    .userNickname(messageDto.getUserNickname())
                    .content(messageDto.getUserNickname()+"님 정답입니다!") // 성공 메시지의 내용을 여기에 작성하세요.
                    .time(time)
                    .type(MessageResponse.Type.SUCCESS)
                    .build();
            chatService.sendMessage(successMessage);

            MessageResponse themeMessage = MessageResponse.
                    builder().
                    roomId(messageDto.getRoomId())
                    .userNickname(messageDto.getUserNickname())
                    .content(content)
                    .time(time)
                    .type(MessageResponse.Type.THEME)
                    .build();
            chatService.sendMessage(themeMessage);
        }
        chatService.sendMessage(
                switch (messageDto.getType()) {
                    case ENTER -> MessageResponse.
                            builder()
                            .roomId(messageDto.getRoomId())
                            .userNickname("시스템")
                            .content(messageDto.getUserNickname() + "님 입장하였습니다!!")
                            .time(time)
                            .type(MessageResponse.Type.ENTER)
                            .build();
                    case START -> MessageResponse.
                            builder()
                            .roomId(messageDto.getRoomId())
                            .userNickname("시스템")
                            .content("게임을 시작합니다")
                            .time(time)
                            .type(MessageResponse.Type.START)
                            .build();
                    case CHAT -> MessageResponse.
                            builder()
                            .roomId(messageDto.getRoomId())
                            .userNickname(messageDto.getUserNickname())
                            .content(messageDto.getContent())
                            .time(time)
                            .corrects(ans) // 여기서 true이면 정답
                            .type(MessageResponse.Type.CHAT)
                            .build();
                    case THEME -> MessageResponse.
                            builder().
                            roomId(messageDto.getRoomId())
                            .userNickname(messageDto.getUserNickname())
                            .content(content)
                            .time(time)
                            .type(MessageResponse.Type.THEME)
                            .build();
                    case SUCCESS -> MessageResponse.
                            builder()
                            .roomId(messageDto.getRoomId())
                            .userNickname(messageDto.getUserNickname())
                            .content(messageDto.getContent())
                            .time(time)
                            .type(MessageResponse.Type.SUCCESS)
                            .build();
                    case OWNER -> null;
                    case END -> MessageResponse.
                            builder()
                            .roomId(messageDto.getRoomId())
                            .userNickname(messageDto.getUserNickname())
                            .content(messageDto.getContent())
                            .time(time)
                            .type(MessageResponse.Type.END)
                            .build();
                }
        );
    }
    public List<Map<String, Object>> getParticipantsOrderByScore() {
        String query = "SELECT p.user_nickname, p.score " +
                "FROM participant p " +
                "ORDER BY p.score DESC";

        return jdbcTemplate.queryForList(query);
    }
    public Map<String, String> getQuestion(String roomID) {
        log.info("roomId = {}", roomID);
        Query query = new Query(Criteria.where("room_id").is(roomID));
        List<TalkBodyInGame> result = mongoTemplate.find(query, TalkBodyInGame.class);

        // result에는 해당 room_id에 대한 문서가 포함된다.
        // 그 중에서 subject와 elements를 가져와서 사용하면 된다.
        if (!result.isEmpty()) {
            TalkBodyInGame document = result.get(0);
            String subject = document.getSubject();
            List<String> elements = document.getElements();
            System.out.println("Subject: " + subject);
            System.out.println("Elements: " + elements);
            Random random = new Random();
            int randomNumber = random.nextInt(elements.size());
            String selectedElement = elements.get(randomNumber);
            // JSON 응답을 위한 Map 생성
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("subject", subject);
            responseMap.put("selectedElement", selectedElement);

            document.setElement(selectedElement);
            mongoTemplate.save(document);
            // 선택된 요소를 리스트에서 제거
            elements.remove(selectedElement);

            // MongoDB에서 선택된 요소를 제거
            Update update = new Update().pull("elements", selectedElement);
            mongoTemplate.updateFirst(query, update, TalkBodyInGame.class);
            return responseMap;
        }
        return null;
    }

    // 게임 정답 맞추는 알고리즘
    static boolean findWord(String origin, String find) {
        int originIndex;
        int findIndex;
        int originLen = origin.length();
        int findLen = find.length();
        // 한글은 유니코드때문에 MAX_VALUE로 배열 생성함
        int[] table = new int[Character.MAX_VALUE + 1];

        // 배열의 요소 초기화
        Arrays.fill(table, findLen);

        // pattern에 있는 문자에 대해 table 값 변경
        for (int i = 0; i < findLen; i++) {
            table[find.charAt(i)] = findLen - 1 - i;
        }

        // 같은 지점에서 시작하여 오른쪽에서 왼쪽으로 탐색
        originIndex = findLen - 1;

        while (originIndex < originLen) {
            findIndex = findLen - 1;

            // dest의 문자와 pattern의 문자가 같다면 오른쪽에서 왼쪽으로 탐색함
            while (origin.charAt(originIndex) == find.charAt(findIndex)) {
                if (findIndex == 0)

                    return true;
                originIndex--;
                findIndex--;
            }

            // dest의 문자와 pattern의 문자가 다르다면 jump (jump 기준은 origin index가 가르키는 문자가 find에 존재한다면 find와 originindex가 가르키는 값까지 jump
            // 아니라면 findLen에서 findIndex만큼 점프
            originIndex += (table[origin.charAt(originIndex)] > findLen - findIndex) ? table[origin.charAt(originIndex)] : findLen - findIndex;
        }
        return false;
    }
}