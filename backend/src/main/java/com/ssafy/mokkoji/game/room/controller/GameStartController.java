package com.ssafy.mokkoji.game.room.controller;

import com.ssafy.mokkoji.game.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class GameStartController {
    private final RoomService roomService;
    @GetMapping("/{roomId}/start")
    public ResponseEntity<Object> gameStart(@PathVariable("roomId") int roomId) {
        try {
            // 게임이 폭파된 경우 실행 안되게 한다.
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
            return ResponseEntity.status(404).body("404 게임 시작 실패!");
        }
    }
}