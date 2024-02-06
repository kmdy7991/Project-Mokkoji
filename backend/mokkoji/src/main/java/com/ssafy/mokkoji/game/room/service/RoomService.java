package com.ssafy.mokkoji.game.room.service;

import com.ssafy.mokkoji.game.room.dto.RoomDto;
import com.ssafy.mokkoji.game.room.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

// 1. 방생성 - 방제목, 방 비밀번호, 방 게임 설정(뮤직큐, 몸으로 말해요)
// 2. 방 입장
public interface RoomService {
    List<RoomDto> getRoomList();
    Page<Map<String, Object>> list (Map<String, Object> paramMap, Pageable page);
    int countRooms();
    void createRoom(RoomDto roomdto);
    RoomDto selectRoom(int roomId);
    int enterUser(Map<String, String> params);
    int userCountPlusOne(int roomID);
    int userCountMinusOne(int roomID);
    int nicknameToID(String nickname);
    int userInGame(String user_id);
    String checkPassword(int roomID);
    int gameStart(int roomID);
    int gameEnd(int roomID);
    int goOutUser(int userId);
    int userRoomID(int userID);

    int isExplosion(int roomID);

    int gameExplosion(int roomID);

    int countInGame(int roomID);

    List<UserDto> participantInfo(int roomID);

    String checkOwner(int roomID);

    String findNickname(int userID);
}
