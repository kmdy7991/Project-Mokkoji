package com.ssafy.mokkoji.member.administrator.controller;

import com.ssafy.mokkoji.member.administrator.dto.TalkBodyDto;
import com.ssafy.mokkoji.member.administrator.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private MongoTemplate mongoTemplate;

    private final AdminService talkBodyService;


    @Autowired
    public AdminController(AdminService talkBodyService) {
        this.talkBodyService = talkBodyService;
    }

    @GetMapping("/talkbodylist")
    public ResponseEntity<Object> getAllSubject() {
        try {
            List<TalkBodyDto> a = talkBodyService.getAllSubject();
            return ResponseEntity.ok().body(a);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("404");
        }
    }

    @DeleteMapping("/delete/{subject}")
    public ResponseEntity<Object> deleteBySubject(@PathVariable("subject") String subject) {
        try {
            System.out.println(subject);
//          만약 id가 들어오게 한다면 ( 보안 및 성능 때문에 권장 )
            talkBodyService.deleteBySubject("65b8802fe2ff81307cbb3995");
//            subject가 들어온다면
//            Criteria criteria = Criteria.where("subject").is(subject);
//            Query query = new Query(criteria);
//            mongoTemplate.remove(query,TalkBodyDto.class);
            return ResponseEntity.status(200).body(true);
        }catch (Exception e){
            return ResponseEntity.status(404).body("삭제 실패");
        }
    }

    @PostMapping("/input")
    public ResponseEntity<Object> save(@RequestBody TalkBodyDto talk){
        System.out.println(talk);
        talkBodyService.save(talk);
        return ResponseEntity.ok().body("성공");
    }
}
