package com.example.mokkoji.talkbody.controller;

import com.example.mokkoji.talkbody.dto.TalkBodyDto;
import com.example.mokkoji.talkbody.service.TalkBodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
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

    @GetMapping("/select")
    public ResponseEntity<Object> getAllSubject() {
        try {
            List<TalkBodyDto> a = talkBodyService.getAllSubject();
            Random random = new Random();
            int randomNumber = random.nextInt(a.size());
            System.out.println(a.get(randomNumber));
            return ResponseEntity.ok().body(a.get(randomNumber));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("404");
        }
    }
}
