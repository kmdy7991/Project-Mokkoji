package com.ssafy.mokkoji.game.talkbody.controller;

import com.ssafy.mokkoji.game.talkbody.dto.TalkBodyInGame;
import com.ssafy.mokkoji.game.talkbody.dto.TalkBodyDto;
import com.ssafy.mokkoji.game.talkbody.service.TalkBodyServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/talkbody")
public class TalkBodyController {
    @Autowired
    private MongoTemplate mongoTemplate;
    private final TalkBodyServiceImpl talkBodyService;

    // 이렇게 되면 방에서 선택되게하고 mongodb에 따로 저장한다.
    @GetMapping("/{room_id}/select")
    public ResponseEntity<Object> getAllSubject(@PathVariable("room_id") int roomId) {
        log.info("get subject = {}", roomId);
        try {
            System.out.println("aaa");
            // 이상한 이슈로 다시 select가 눌리면 방 3번이 중복 된다. 그래서 room_id같은 거 삭제시켜줘야함
            Query query = new Query(Criteria.where("room_id").is(roomId));
            System.out.println(query);
            mongoTemplate.remove(query, TalkBodyInGame.class);

            System.out.println("bbb");
            List<TalkBodyDto> a = talkBodyService.getAllSubject();
            System.out.println(a);
            Random random = new Random();
            int randomNumber = random.nextInt(a.size());

            TalkBodyDto b = a.get(randomNumber);
            TalkBodyInGame tk = new TalkBodyInGame();
            tk.setRoom_id(roomId);
            tk.setSubject(b.getSubject());
            tk.setElements(b.getElements());
            System.out.println(tk);

            mongoTemplate.save(tk);
            return ResponseEntity.ok().body(b);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정 필요");
        }
    }

    @GetMapping("/{roomId}/question")
    public ResponseEntity<Object> getQuestion(@PathVariable("roomId") int roomID) {
        log.info("roomId = {}", roomID);
        try {
            Query query = new Query(Criteria.where("room_id").is(roomID));
            List<TalkBodyInGame> result = mongoTemplate.find(query, TalkBodyInGame.class);

            // result에는 해당 room_id에 대한 문서가 포함된다.
            // 그 중에서 subject와 elements를 가져와서 사용하면 된다.

            log.info("word = {}", result);

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

                // 선택된 요소를 리스트에서 제거
                elements.remove(selectedElement);

                // MongoDB에서 선택된 요소를 제거
                Update update = new Update().pull("elements", selectedElement);
                mongoTemplate.updateFirst(query, update, TalkBodyInGame.class);
                return ResponseEntity.ok().body(responseMap);
            } else {
                System.out.println("result 다 비었다.");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 /{room_id}/question elements 길이 0 실패");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 /{room_id}/question 실패");
        }
    }


}
