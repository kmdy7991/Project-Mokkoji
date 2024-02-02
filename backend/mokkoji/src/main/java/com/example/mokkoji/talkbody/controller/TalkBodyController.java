package com.example.mokkoji.talkbody.controller;

import com.example.mokkoji.talkbody.dto.TalkBodyDto;
import com.example.mokkoji.talkbody.dto.TalkBodyInGame;
import com.example.mokkoji.talkbody.service.TalkBodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/api/talkbody")
@CrossOrigin("*")
public class TalkBodyController {

    @Autowired
    private MongoTemplate mongoTemplate;

    private final TalkBodyService talkBodyService;

    @Autowired
    public TalkBodyController(TalkBodyService talkBodyService) {
        this.talkBodyService = talkBodyService;
    }
    // 이렇게 되면 방에서 선택되게하고 mongodb에 따로 저장한다.
    @GetMapping("/{room_id}/select")
    public ResponseEntity<Object> getAllSubject(@PathVariable("room_id") int roomId) {
        try {
            List<TalkBodyDto> a = talkBodyService.getAllSubject();
            Random random = new Random();
            int randomNumber = random.nextInt(a.size());
            TalkBodyDto b = a.get(randomNumber);
            TalkBodyInGame tk = new TalkBodyInGame();
            tk.setRoom_id(roomId);
            tk.setSubject(b.getSubject());
            tk.setElements(b.getElements());
            mongoTemplate.save(tk);
            return ResponseEntity.ok().body(b);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("404");
        }
    }

    @GetMapping("/{room_id}/question")
    public ResponseEntity<Object> getQuestion(@PathVariable("room_id") int roomID){
        try{
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

                // 선택된 요소를 리스트에서 제거
                elements.remove(selectedElement);

                // MongoDB에서 선택된 요소를 제거
                Update update = new Update().pull("elements", selectedElement);
                mongoTemplate.updateFirst(query, update, TalkBodyInGame.class);
                return ResponseEntity.status(200).body(responseMap);
            }
            return ResponseEntity.status(404).body("404 /{room_id}/question elements 길이 0 실패");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(404).body("404 /{room_id}/question 실패");
        }
    }


}
