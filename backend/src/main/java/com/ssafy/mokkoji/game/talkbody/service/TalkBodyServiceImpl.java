package com.ssafy.mokkoji.game.talkbody.service;

import com.ssafy.mokkoji.game.talkbody.dto.TalkBodyDto;
import com.ssafy.mokkoji.game.talkbody.repository.TalkBodyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TalkBodyServiceImpl implements TalkBodyService{
    private final TalkBodyRepository talkBodyRepository;

    @Override
    public List<TalkBodyDto> getAllSubject() {
        return talkBodyRepository.findAll();
    }
}