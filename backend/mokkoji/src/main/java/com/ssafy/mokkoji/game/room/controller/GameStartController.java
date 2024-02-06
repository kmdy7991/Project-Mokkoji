package com.ssafy.mokkoji.game.room.controller;

import com.ssafy.mokkoji.game.room.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/room")
public class GameStartController {
    @Autowired
    private RoomService roomService;
    private final Map<Integer, SseEmitter> emitters = new HashMap<>();

    @GetMapping("/{room_id}/start")
    public ResponseEntity<Object> gameStart(@PathVariable("room_id") int roomId) {
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
            e.printStackTrace();
            return ResponseEntity.status(404).body("404 게임 시작 실패!");
        }
    }

//    private void sendGameStartEvent(int roomId) {
//        SseEmitter emitter = emitters.get(roomId);
//        if (emitter != null) {
//            try {
//                Map<String, String> eventData = new HashMap<>();
//                eventData.put("event", "gameStart");
//                eventData.put("message", "Game started!");
//                emitter.send(SseEmitter.event().data(eventData).build());
//                emitter.complete();
//            } catch (IOException e) {
//                // 에러 처리
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @GetMapping("/{room_id}/subscribe")
//    public SseEmitter subscribe(@PathVariable("room_id") int roomId) {
//        SseEmitter emitter = new SseEmitter();
//        emitters.put(roomId, emitter);
//        return emitter;
//    }
}
