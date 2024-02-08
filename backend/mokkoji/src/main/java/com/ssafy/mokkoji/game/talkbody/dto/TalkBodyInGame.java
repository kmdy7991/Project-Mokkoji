package com.ssafy.mokkoji.game.talkbody.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection ="talkBody_themes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TalkBodyInGame {
    @Id
    private String _id;
    private int room_id;
    private String subject;
    private List<String> elements;
}
