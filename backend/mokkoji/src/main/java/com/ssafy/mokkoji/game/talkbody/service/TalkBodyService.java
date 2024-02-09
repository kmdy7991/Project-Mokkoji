package com.ssafy.mokkoji.game.talkbody.service;

import com.ssafy.mokkoji.game.talkbody.dto.TalkBodyDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TalkBodyService {

    List<TalkBodyDto> getAllSubject();
}

