package com.example.mokkoji.room.mapper;

import com.example.mokkoji.room.dto.RoomDto;
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

    void enterUser(Map<String, String> params);

    int userCountPlusOne(int roomID);

    int userCountMinusOne(int roomID);

    int nicknameToID(String nickname);

    int userInGame(int user_id);

    int checkPassword(int roomID);

    int gameStart(int roomID);

    int gameEnd(int roomID);

    int goOutUser(int userId);

    int userRoomID(int userID);
}
