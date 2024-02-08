package com.ssafy.mokkoji.game.talkbody.service;

import com.ssafy.mokkoji.game.talkbody.dto.TalkBodyDto;
import com.ssafy.mokkoji.game.talkbody.repository.TalkBodyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TalkBodyServiceImpl implements TalkBodyService{
    private final TalkBodyRepository talkBodyRepository;

    @Autowired
    public TalkBodyServiceImpl(TalkBodyRepository talkkBodyRepository) {
        this.talkBodyRepository = talkkBodyRepository;
    }

    @Override
    public List<TalkBodyDto> getAllSubject() {
        return talkBodyRepository.findAll();
    }
}

//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class TalkBodyServiceImpl {
//    private final TalkBodyRepository talkBodyRepository;
//
//    @Autowired
//    public TalkBodyServiceImpl(TalkBodyRepository talkkBodyRepository) {
//        this.talkBodyRepository = talkkBodyRepository;
//    }
//
//    public List<TalkBodyDto> getAllSubject(){
//        System.out.println("ccc");
//        return talkBodyRepository.findAll();
//    }
//}