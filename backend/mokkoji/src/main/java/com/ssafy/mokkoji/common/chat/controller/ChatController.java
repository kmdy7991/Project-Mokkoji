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
        boolean ans = false;

// 정답 확인
        if (messageDto.getType() == MessageDto.Type.CHAT) {
            if (!messageDto.getContent().isEmpty() && messageDto.getContent() != null && getWord(Integer.parseInt(messageDto.getRoomId())) != null) {
                System.out.println("정답매칭시작");
                ans = findWord(messageDto.getContent(),getWord(Integer.parseInt(messageDto.getRoomId()))); // true 정답 false 오답
            }
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
                    .userNickname("시스템")
                    .content(messageDto.getUserNickname()+"님 정답입니다!") // 성공 메시지의 내용을 여기에 작성하세요.
                    .time(time)
                    .type(MessageResponse.Type.SUCCESS)
                    .build();
            chatService.sendMessage(successMessage);

            MessageResponse themeMessage = MessageResponse.
                    builder().
                    roomId(messageDto.getRoomId())
                    .userNickname(messageDto.getUserNickname())
                    .content(getQuestion(Integer.parseInt(messageDto.getRoomId())))
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
                            .content(getQuestion(Integer.parseInt(messageDto.getRoomId())))
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
                            .userList(getParticipantsOrderByScore(Integer.parseInt(messageDto.getRoomId())))
                            .time(time)
                            .type(MessageResponse.Type.END)
                            .build();
                }
        );

        if(messageDto.getType() == MessageDto.Type.START){
            int n = endChangeZero(Integer.parseInt(messageDto.getRoomId()));
            System.out.println(n);
        }
    }

    public List<Map<String, Object>> getParticipantsOrderByScore(int roomID) {
        String query = "SELECT p.user_nickname, p.score " +
                "FROM participant p WHERE room_id = ? " +
                "ORDER BY p.score DESC";

        return jdbcTemplate.queryForList(query, roomID);
    }
    public String getQuestion(int roomID) {
        log.info("roomId = {}", roomID);
        Query query = new Query(Criteria.where("room_id").is(roomID));
        List<TalkBodyInGame> result = mongoTemplate.find(query, TalkBodyInGame.class);
        System.out.println("debug"+result);
        System.out.println(query);
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
            String tmp = subject +" "+ selectedElement;
            // MongoDB에서 선택된 요소를 제거
            Update update = new Update().pull("elements", selectedElement);
            mongoTemplate.updateFirst(query, update, TalkBodyInGame.class);
            return tmp;
        }
        return null;
    }

    public String getWord(int roomID){
        Query query = new Query(Criteria.where("room_id").is(roomID));
        List<TalkBodyInGame> result = mongoTemplate.find(query, TalkBodyInGame.class);
        if (!result.isEmpty()) {
            TalkBodyInGame document = result.get(0);
            return document.getElement();
        }
        return "없어";
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

    public int endChangeZero(int roomID){
        String query = "UPDATE participant set score = 0 where room_id = ?";
        return jdbcTemplate.update(query, roomID);
    }
}