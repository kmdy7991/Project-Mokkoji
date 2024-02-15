package com.ssafy.mokkoji.game.room.service;

import com.ssafy.mokkoji.game.room.dto.RoomDto;
import com.ssafy.mokkoji.game.room.dto.UserDto;
import com.ssafy.mokkoji.game.room.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;
    // gameId -> userList
//    private Map<String, List<User>> userList;

    @Override
    public List<RoomDto> getRoomList() {
        return roomMapper.getAllRooms();
    }

    @Override
    public Page<Map<String, Object>> list(Map<String, Object> paramMap, Pageable page) {
        paramMap.put("offset", page.getOffset());
        paramMap.put("pageSize", page.getPageSize());
        List<Map<String, Object>> contents = roomMapper.list(paramMap);
        int count = roomMapper.countRooms();
        return new PageImpl<>(contents, page, count);
    }

    @Override
    public void createRoom(RoomDto roomdto) {
        roomMapper.createRoom(roomdto);
    }

    @Override
    public int countRooms() {
        return roomMapper.countRooms();
    }

    @Override
    public RoomDto selectRoom(int roomId) {
        return roomMapper.selectRoom(roomId);
    }

    @Override
    public int enterUser(Map<String, String> params) {
        System.out.println(params.get("user_id"));
        System.out.println(params.get("room_id"));
        return roomMapper.enterUser(params);
    }

    @Override
    public int userCountPlusOne(int roomID) {
        return roomMapper.userCountPlusOne(roomID);
    }

    @Override
    public int userCountMinusOne(int roomID) {
        return roomMapper.userCountMinusOne(roomID);
    }

    @Override
    public int nicknameToID(String nickname) {
        return roomMapper.nicknameToID(nickname);
    }

    @Override
    public int userInGame(String user_id) {
        return roomMapper.userInGame(user_id);
    }

    @Override
    public String checkPassword(int roomID) {
        return roomMapper.checkPassword(roomID);
    }

    @Override
    public int gameStart(int roomID) {
        return roomMapper.gameStart(roomID);
    }

    @Override
    public int gameEnd(int roomID) {
        return roomMapper.gameEnd(roomID);
    }

    @Override
    public int goOutUser(int userId) {
        return roomMapper.goOutUser(userId);
    }

    @Override
    public int userRoomID(int userID) {
        return roomMapper.userRoomID(userID);
    }

    @Override
    public int isExplosion(int roomID) {
        return roomMapper.isExplosion(roomID);
    }

    @Override
    public int gameExplosion(int roomID) {
        return roomMapper.gameExplosion(roomID);
    }

    @Override
    public int countInGame(int roomID) {
        return roomMapper.countInGame(roomID);
    }

    @Override
    public List<UserDto> participantInfo(int roomID) {
        return roomMapper.participantInfo(roomID);
    }

    @Override
    public String checkOwner(int roomID) {
        return roomMapper.checkOwner(roomID);
    }

    @Override
    public String findNickname(int userID) {
        return roomMapper.findNickname(userID);
    }
}