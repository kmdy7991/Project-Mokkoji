package com.example.mokkoji.talkbody.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "Mokkoji")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TalkBodyDto {

    @Id
    private String _id;
    private String subject;
    private List<String> elements;
}