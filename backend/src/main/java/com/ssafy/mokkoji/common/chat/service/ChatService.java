package com.ssafy.mokkoji.common.chat.service;

import com.ssafy.mokkoji.common.chat.domain.response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    void sendMessage(MessageResponse message);
}
