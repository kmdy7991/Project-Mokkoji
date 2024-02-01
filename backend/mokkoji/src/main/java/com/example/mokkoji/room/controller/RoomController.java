package com.example.mokkoji.room.controller;

import com.example.mokkoji.room.dto.RoomDto;
import com.example.mokkoji.room.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Autowired
//    private GameController gameController;

    //    public RoomController(JwtTokenProvider jwtTokenProvider) {
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
    @GetMapping("/checkpwd")
    public ResponseEntity<Object> checkPassword(int roomId, String password) {
        if (roomService.checkPassword(roomId) == Integer.parseInt(password)) {
            return ResponseEntity.status(200).body("200, 입장하세요.");
        }
        return ResponseEntity.status(401).body("401 비밀번호 불일치");
    }

    @GetMapping("/start/{room_id}")
    public ResponseEntity<Object> gameStart(@PathVariable("room_id") int roomId, HttpServletRequest request) {
        try {
            System.out.println("aaa");
            int gs = roomService.gameStart(roomId);
            System.out.println(gs);
            if (gs == 1) {
                System.out.println("bbb");
                return ResponseEntity.status(200).body("게임 시작 성공");
            } else {
                System.out.println("ccc");
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(UriComponentsBuilder.fromPath("/api/room/{room_id}")
                                .buildAndExpand(roomId).toUri())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("404 게임 시작 실패!");
        }
    }

    @GetMapping("/delete/{room_id}")
    public ResponseEntity<Object> goOutUser(HttpServletRequest request, @PathVariable("room_id") int roomId) {
        try {
            String jwtToken = request.getHeader("Authorization");
            String nickname = "";
            // Authorization 헤더에 토큰이 있는 경우
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                // 토큰에서 닉네임 추출
                nickname = "tokennickname";
//                nickname = jwtTokenProvider.getUserNickname(jwtToken.substring(7));
            } else {
                // 로그인하지 않은 사용자인 경우 세션에서 닉네임 가져오기
                Object sessionNickname = request.getSession().getAttribute("nickname");

                if (sessionNickname != null) {
                    nickname = (String) sessionNickname;
                } else {
                    // 세션에 사용자 정보가 없는 경우 익명 사용자로 처리(임시)
                    nickname = "ssafy222";
                }
            }
            int user_id = roomService.nicknameToID(nickname);
            int user_in_the_room_number = roomService.userRoomID(user_id);
            System.out.println("aaa" + user_in_the_room_number);
//        나가는 유저
            int is_go_out_user = roomService.goOutUser(user_id);
            if (is_go_out_user == 1) {
                int count_down_user_count = roomService.userCountMinusOne(user_in_the_room_number);
                if (count_down_user_count >= 1) {
                    return ResponseEntity.status(200).body("나가기 성공");
                } else {
                    return ResponseEntity.status(300).body("실패");
                }
            } else {
                return ResponseEntity.status(300).body("실패");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("500, Server Error");
        }
    }

}
