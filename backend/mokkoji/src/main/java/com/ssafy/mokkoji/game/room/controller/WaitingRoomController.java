package com.ssafy.mokkoji.game.room.controller;

import com.ssafy.mokkoji.game.room.dto.RoomDto;
import com.ssafy.mokkoji.game.room.dto.UserDto;
import com.ssafy.mokkoji.game.room.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Object> room() {
        try {
            List<RoomDto> roomList = roomService.getRoomList();
            return ResponseEntity.status(200)
                    .body(roomList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("500 ServerError");
        }
    }
//    @GetMapping
//    @ResponseBody
//    public ResponseEntity<Object> list(@RequestParam Map<String, Object> paramMap, @PageableDefault(value = 6) Pageable page){
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        List<Map<String, Object>> contentList = new ArrayList<>();
//        Page<Map<String,Object>> result = roomService.list(paramMap,page);
//        resultMap.put("pages", result);
//        resultMap.put("size", page.getPageSize());
//
//        // 여기서 content만을 추출하여 List로 변환
//        List<Map<String, Object>> content = result.getContent();
//
//        // 필요한 경우 다른 페이지 정보 등도 resultMap에 추가 가능
//        contentList.addAll(content);
//        System.out.println(contentList);
//        System.out.println("aaa");
//            return ResponseEntity.status(200)
//                    .body(content);
//    }
    @PostMapping("/create")
    public ResponseEntity<Object> createRoom(@RequestBody RoomDto roomdto, HttpServletRequest request) {
        try {
            if (roomService.countRooms() <= 20) {
                // Authorization 헤더에서 토큰을 가져옴
                String jwtToken = request.getHeader("Authorization");

                String nickname = roomdto.getOwner();
                // 세션에서 닉네임을 가져옴

                roomService.createRoom(roomdto);
                int user_id = roomService.nicknameToID(nickname);
                if (roomService.userInGame(nickname) >= 1) {
                    return ResponseEntity.badRequest().body("이미 방에 들어가서 방 생성이 불가능 합니다.");
                }
                Map<String, String> params = new HashMap<>();
                params.put("user_id", Integer.toString(user_id));  // 여기에는 사용자 번호가 들어갑니다.
                params.put("user_nickname",nickname); // 유저 닉네임 들어감
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


    @GetMapping("/enter/{room_id}/{user_nickname}")
    public ResponseEntity<Object> enterRoom(@PathVariable("room_id") int roomId, @PathVariable("user_nickname") String user_nickname, HttpServletRequest request) {
        System.out.println(user_nickname);
        try {
            // 여기에서 roomId를 사용하여 방에 입장하는 로직을 구현합니다.
            // 해당 방의 정보를 가져오거나, 사용자를 방에 추가하는 등의 동작을 수행할 수 있습니다.

            // 반환하는 문자열은 보통 입장 후 이동할 페이지의 경로를 나타냅니다.d
            // 예를 들어, "redirect:/rooms/{roomId}" 등으로 다른 페이지로 리다이렉트할 수 있습니다.
            RoomDto room = roomService.selectRoom(roomId);
            System.out.println(room);
            if (room.is_explosion()) {
                return ResponseEntity.badRequest().body("해당하는 방 없음");
            }

            if (room.is_active()) {
                return ResponseEntity.badRequest().body("이미 진행중인 게임");
            }

            if (room.getUser_count() < 6) {
                String jwtToken = request.getHeader("Authorization");
                System.out.println(user_nickname);
                int user_id = roomService.nicknameToID(user_nickname); // 예외처리 생각
                if (roomService.userInGame(user_nickname) >= 1) {
                    return ResponseEntity.badRequest().body("이미 방에 들어갔습니다.");
                }
                Map<String, String> params = new HashMap<>();
                params.put("user_id", Integer.toString(user_id));  // 여기에는 사용자 번호가 들어갑니다.
                params.put("user_nickname", user_nickname);
                params.put("room_id", Integer.toString(roomId));  // 여기에는 방 번호가 들어갑니다.
                if (roomService.enterUser(params)==1){
                    roomService.userCountPlusOne(roomId);
                    List<UserDto> participantList = roomService.participantInfo(roomId);
                    // participantList와 함께 room을 추가
                    Map<String, Object> response = new HashMap<>();
                    response.put("participants", participantList);
                    response.put("room", room);

                    return ResponseEntity.ok().body(response); // 방으로이동할 수 있게 redirect만들어야할듯
                }
                return ResponseEntity.status(HttpStatus.CONFLICT).body("등록 실패!");
            } else {
                return ResponseEntity.status(403).body("인원수가 초과하였습니다.");
            }


        } catch (Exception e) {
//            sessionService.isExist(sessionName);
//            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
            return ResponseEntity.status(404)
                    .body("입장실패");
        }
    }


}
