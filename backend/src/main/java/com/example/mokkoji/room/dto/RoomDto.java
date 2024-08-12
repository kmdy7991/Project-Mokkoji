package com.example.mokkoji.room.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// DTO가 있을 때 RESTAPI호출시 null이 나오는 필드가 있음 공개하지 않는다.
//import com.fasterxml.jackson.annotation.JsonInclude;
//
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class RoomDto {
    private int room_id;
    private String room_name;
    private String room_password;
    private int user_count;
    private boolean is_private;
    private boolean is_explosion;
    private boolean is_active;
    private int game_type;
    private String owner;
}
