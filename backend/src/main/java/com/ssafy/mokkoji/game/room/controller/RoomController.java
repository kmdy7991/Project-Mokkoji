package com.ssafy.mokkoji.game.room.controller;

import com.ssafy.mokkoji.common.chat.domain.response.MessageResponse;
import com.ssafy.mokkoji.game.room.dto.UserDto;
import com.ssafy.mokkoji.game.room.service.RoomService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import com.ssafy.mokkoji.common.chat.domain.request.MessageDto;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;

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
//    @GetMapping("/{room_id}/start")
//    public ResponseEntity<Object> gameStart(@PathVariable("room_id") int roomId, HttpServletRequest request) {
//        try {
////            게임이 폭파된 경우 실행 안되게 한다.
//            if (roomService.isExplosion(roomId) == 1) {
//                return ResponseEntity.status(300).body("300, 방이 이미 터졌습니다. -> room으로 리다이렉트되게");
//            }
//            int gs = roomService.gameStart(roomId); // gs -> game_start
//            System.out.println(gs);
//            if (gs == 1) {
//                return ResponseEntity.status(200).body("게임 시작 성공");
//            } else {
//                return ResponseEntity.status(HttpStatus.FOUND)
//                        .location(UriComponentsBuilder.fromPath("/api/room/{room_id}")
//                                .buildAndExpand(roomId).toUri())
//                        .build();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(404).body("404 게임 시작 실패!");
//        }
//    }

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

    @DeleteMapping("/delete/{room_id}/{user_nickname}")
    public ResponseEntity<Object> goOutUser(HttpServletRequest request, @PathVariable("room_id") int roomId, @PathVariable("user_nickname") String user_nickname) {
        try {
            String jwtToken = request.getHeader("Authorization");
            int user_id = roomService.nicknameToID(user_nickname);
//            int user_in_the_room_number = roomService.userRoomID(user_id); // url 변경으로 인해 사용 x

//        나가는 유저
            int is_go_out_user = roomService.goOutUser(user_id);
            if (is_go_out_user == 1) {
                int count_down_user_count = roomService.userCountMinusOne(roomId);
                if (count_down_user_count == 1) {

                    List<UserDto> participantList = roomService.participantInfo(roomId);
                    // 방의 유저수가 0명이면 isExplosion 활성화
                    if (roomService.countInGame(roomId) == 0) {
                        int room_explosion = roomService.gameExplosion(roomId);
                        if (room_explosion == 1) {
                            return ResponseEntity.status(200).body("200 성공");
                        }
                        return ResponseEntity.status(404).body("방폭파 실패");
                    }
                    // 방장이 나가면 방 터짐 (소켓 활용)
                    if (roomService.checkOwner(roomId).equals(user_nickname)) {
                        MessageResponse ownerMessage = MessageResponse.builder().content("방장이 나가 방이 터졌습니다.").type(MessageResponse.Type.OWNER).build();
                        messagingTemplate.convertAndSend("/topic/" + roomId, ownerMessage);
                        HttpHeaders headers = new HttpHeaders();
                        // 한 번 봐보기
                        URI uri = new URI("http://192.168.31.58:8080/api/room");
                        headers.setLocation(uri);
                        return ResponseEntity.status(HttpStatus.FOUND)
                                .headers(headers)
                                .body(participantList);
                        // 헤더에 로케이션 담기 리다이렉트 시키기
                    } else {
                        return ResponseEntity.status(200).body(participantList);
                    }


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

    @GetMapping("/{room_id}/participants")
    public ResponseEntity<Object> getParticipantsList(@PathVariable("room_id") int roomId){
        try {
            List<UserDto> participantList = roomService.participantInfo(roomId);
            return ResponseEntity.status(200).body(participantList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(404).body("방폭파 실패");
        }
    }
}