package com.example.mokkoji.room.controller;

import com.example.mokkoji.room.dto.RoomDto;
import com.example.mokkoji.room.service.RoomService;
import com.fasterxml.jackson.databind.JsonNode;
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
    @PostMapping("/{room_id}/checkpwd")
    public ResponseEntity<Object> checkPassword(@PathVariable("room_id") int roomId, @RequestBody JsonNode requestBody) {
        System.out.println(requestBody.get("password").asText());
        System.out.println(roomService.checkPassword(roomId));
        if (roomService.checkPassword(roomId).equals(requestBody.get("password").asText())) {
            return ResponseEntity.status(200).body("200, 입장하세요.");
        }
        return ResponseEntity.status(401).body("401 비밀번호 불일치");
    }

    //    @GetMapping("/start/{room_id}")
    @GetMapping("/{room_id}/start")
    public ResponseEntity<Object> gameStart(@PathVariable("room_id") int roomId, HttpServletRequest request) {
        try {
//            게임이 폭파된 경우 실행 안되게 한다.
            if (roomService.isExplosion(roomId) == 1) {
                return ResponseEntity.status(300).body("300, 방이 이미 터졌습니다. -> room으로 리다이렉트되게");
            }
            int gs = roomService.gameStart(roomId); // gs -> game_start
            System.out.println(gs);
            if (gs == 1) {
                return ResponseEntity.status(200).body("게임 시작 성공");
            } else {
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

    @GetMapping("/{room_id}/end")
    public ResponseEntity<Object> gameEnd(@PathVariable("room_id") int roomId, HttpServletRequest request) {
        try {
            int game_end = roomService.gameEnd(roomId);
            if (game_end == 1) {
                return ResponseEntity.status(200).body("게임 끝 성공");
            } else {
                return ResponseEntity.status(300).body("게임나가기 실패 -> 있던 곳으로 리다이렉트 해주세요");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("404 게임 끝 실패1");
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
//            int user_in_the_room_number = roomService.userRoomID(user_id); // url 변경으로 인해 사용 x
            System.out.println("aaa" + roomId);
//        나가는 유저
            int is_go_out_user = roomService.goOutUser(user_id);
            if (is_go_out_user == 1) {
                int count_down_user_count = roomService.userCountMinusOne(roomId);
                if (count_down_user_count >= 1) {
                    return ResponseEntity.status(200).body("나가기 성공");
                } else {
                    return ResponseEntity.status(300).body("실패");
                }
            } else {
                return ResponseEntity.status(300).body("실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("500, Server Error");
        }
    }

    @GetMapping("/{room_id}/explosion")
    public ResponseEntity<Object> roomExplosion(HttpServletRequest request, @PathVariable("room_id") int roomId) {
        if (roomService.countInGame(roomId) == 0) {
            int room_is_explosion = roomService.isExplosion(roomId);
            if (room_is_explosion == 1) {
                return ResponseEntity.status(200).body("200 성공");
            }
            return ResponseEntity.status(404).body("방폭파 실패");
        } else {
            return ResponseEntity.status(404).body("사람이 1명 이상이라 방 폭파 실패");
        }
    }
}
