//package com.ssafy.mokkoji.common.chat.controller;
//
//import com.ssafy.mokkoji.game.talkbody.dto.TalkBodyInGame;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//public class zzz {
//
//    public static void main(String[] args) {
//        zzz controller = new zzz(); // zzz 클래스의 인스턴스 생성
//        Map<String, String> result = controller.getQuestion("59");
//        System.out.println(result);
//    }
//
//    public Map<String, String> getQuestion(String roomID) {
//        MongoTemplate mongoTemplate;
//
//        System.out.println(roomID + "aaaaaaaaaaaaaaaaaaaaa");
//        Query query = new Query(Criteria.where("room_id").is(roomID));
//        List<TalkBodyInGame> result = mongoTemplate.find(query, TalkBodyInGame.class);
//        System.out.println("debug"+result);
//        System.out.println(query);
//        // result에는 해당 room_id에 대한 문서가 포함된다.
//        // 그 중에서 subject와 elements를 가져와서 사용하면 된다.
//        if (!result.isEmpty()) {
//            TalkBodyInGame document = result.get(0);
//            String subject = document.getSubject();
//            List<String> elements = document.getElements();
//            System.out.println("Subject: " + subject);
//            System.out.println("Elements: " + elements);
//            Random random = new Random();
//            int randomNumber = random.nextInt(elements.size());
//            String selectedElement = elements.get(randomNumber);
//            // JSON 응답을 위한 Map 생성
//            Map<String, String> responseMap = new HashMap<>();
//            responseMap.put("subject", subject);
//            responseMap.put("selectedElement", selectedElement);
//
//            document.setElement(selectedElement);
//            mongoTemplate.save(document);
//            // 선택된 요소를 리스트에서 제거
//            elements.remove(selectedElement);
//
//            // MongoDB에서 선택된 요소를 제거
//            Update update = new Update().pull("elements", selectedElement);
//            mongoTemplate.updateFirst(query, update, TalkBodyInGame.class);
//            return responseMap;
//        }
//        return null;
//    }
//
//
//}
