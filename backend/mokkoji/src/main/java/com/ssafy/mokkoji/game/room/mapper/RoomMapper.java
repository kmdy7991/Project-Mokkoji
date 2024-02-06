package com.ssafy.mokkoji.game.room.mapper;

import com.ssafy.mokkoji.game.room.dto.RoomDto;
import com.ssafy.mokkoji.game.room.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomMapper {
    List<RoomDto> getAllRooms();
    List<Map<String, Object>> list (Map<String, Object> paramMap);
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
