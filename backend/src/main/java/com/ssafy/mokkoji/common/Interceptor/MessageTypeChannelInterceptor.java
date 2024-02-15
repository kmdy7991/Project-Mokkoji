package com.ssafy.mokkoji.common.Interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

// 스프링 버전이 바뀌고 ChannelInterceptor 상속받아 직접 구현해야함
public class MessageTypeChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 메시지 전송 전에 실행되는 로직
        return message;
    }
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        // 메시지 전송 후에 실행되는 로직
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        // 메시지 전송 완료 후에 실행되는 로직
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        // 메시지 수신 전에 실행되는 로직
        return true;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        // 메시지 수신 후에 실행되는 로직
        return message;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        // 메시지 수신 완료 후에 실행되는 로직
    }
}
