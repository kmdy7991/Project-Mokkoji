package com.example.mokkoji.room.controller;

import com.example.mokkoji.room.dto.RoomDto;
import com.example.mokkoji.room.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// CORS 정책으로 인한 문제 해결하기 위해(모든 엔드포인트에 대해 모든 도메인에서의 요청이 허용)
//@CrossOrigin("*")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/room")
public class WaitingRoomController {

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

//    @GetMapping
//    public ResponseEntity<Object> room() {
//        try {
//            List<RoomDto> roomList = roomService.getRoomList();
//            return ResponseEntity.status(200)
//                    .body(roomList);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500)
//                    .body("500 ServerError");
//        }
//    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> paramMap, @PageableDefault(value = 6) Pageable page){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> contentList = new ArrayList<>();
        Page<Map<String,Object>> result = roomService.list(paramMap,page);
        resultMap.put("pages", result);
        resultMap.put("size", page.getPageSize());

        // 여기서 content만을 추출하여 List로 변환
        List<Map<String, Object>> content = result.getContent();

        // 필요한 경우 다른 페이지 정보 등도 resultMap에 추가 가능
        contentList.addAll(content);
        System.out.println(contentList);
        System.out.println("aaa");
            return ResponseEntity.status(200)
                    .body(contentList);
    }
    @PostMapping("/create")
    public ResponseEntity<Object> createRoom(@RequestBody RoomDto roomdto, HttpServletRequest request) {
        try {
            if (roomService.countRooms() <= 20) {
                // Authorization 헤더에서 토큰을 가져옴
                String jwtToken = request.getHeader("Authorization");
                String nickname = "";
                // 세션에서 닉네임을 가져옴

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
                        // 세션에 사용자 정보가 없는 경우 익명 사용자로 처리
                        nickname = "ssafy222";
                    }
                }
                roomdto.setOwner(nickname);
                roomService.createRoom(roomdto);
                int user_id = roomService.nicknameToID(nickname);
                if (roomService.userInGame(user_id) >= 1) {
                    return ResponseEntity.badRequest().body("이미 방에 들어가서 방 생성이 불가능 합니다.");
                }
                Map<String, String> params = new HashMap<>();
                params.put("user_id", Integer.toString(user_id));  // 여기에는 사용자 번호가 들어갑니다.
                params.put("room_id", Integer.toString(roomdto.getRoom_id()));  // 여기에는 방 번호가 들어갑니다.
                roomService.enterUser(params);

                return ResponseEntity.ok()
                        .body(roomdto);
            } else {
                return ResponseEntity.status(402)
                        .body("Room 개수가 초과되었습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404)
                    .body("방생성실패!");
        }
    }


    @GetMapping("/enter/{room_id}")
    public ResponseEntity<Object> enterRoom(@PathVariable("room_id") int roomId, HttpServletRequest request) {
        try {
            // 여기에서 roomId를 사용하여 방에 입장하는 로직을 구현합니다.
            // 해당 방의 정보를 가져오거나, 사용자를 방에 추가하는 등의 동작을 수행할 수 있습니다.

            // 반환하는 문자열은 보통 입장 후 이동할 페이지의 경로를 나타냅니다.
            // 예를 들어, "redirect:/rooms/{roomId}" 등으로 다른 페이지로 리다이렉트할 수 있습니다.
            RoomDto room = roomService.selectRoom(roomId);
            if (room.is_explosion()) {
                return ResponseEntity.badRequest().body("해당하는 방 없음");
            }

            if (room.is_active()) {
                return ResponseEntity.badRequest().body("이미 진행중인 게임");
            }

            if (room.getUser_count() < 6) {
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
                if (roomService.userInGame(user_id) >= 1) {
                    return ResponseEntity.badRequest().body("이미 방에 들어갔습니다.");
                }
                Map<String, String> params = new HashMap<>();
                params.put("user_id", Integer.toString(user_id));  // 여기에는 사용자 번호가 들어갑니다.
                params.put("room_id", Integer.toString(roomId));  // 여기에는 방 번호가 들어갑니다.
                roomService.enterUser(params);
                roomService.userCountPlusOne(roomId);
                return ResponseEntity.ok().body(room); // 방으로이동할 수 있게 redirect만들어야할듯
            } else {
                return ResponseEntity.status(403).body("인원수가 초과하였습니다.");
            }


        } catch (Exception e) {
//            sessionService.isExist(sessionName);
            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
//            return ResponseEntity.status(404)
//                    .body(SessionTokenPostRes.of(404, "서버에러 방없음", null));
        }
    }


}
