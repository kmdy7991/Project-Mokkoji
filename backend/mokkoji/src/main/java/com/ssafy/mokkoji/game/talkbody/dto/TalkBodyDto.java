package com.ssafy.mokkoji.game.talkbody.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "talkBody")
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TalkBodyDto {
    @Id
    private String id;
    private String subject;
    private List<String> elements;
}