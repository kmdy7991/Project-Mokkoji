package com.example.mokkoji.talkbody.service;

import com.example.mokkoji.room.dto.RoomDto;
import com.example.mokkoji.talkbody.dto.TalkBodyDto;
import com.example.mokkoji.talkbody.repository.TalkkBodyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TalkBodyService {
    private final TalkkBodyRepository talkkBodyRepository;

    @Autowired
    public TalkBodyService(TalkkBodyRepository talkkBodyRepository) {
        this.talkkBodyRepository = talkkBodyRepository;
    }

    public List<TalkBodyDto> getAllSubject(){
        return talkkBodyRepository.findAll();
    }
}
