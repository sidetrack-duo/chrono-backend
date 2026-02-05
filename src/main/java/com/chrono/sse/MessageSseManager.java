package com.chrono.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageSseManager {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    //연결하기
    public SseEmitter connect(Long userId){
        SseEmitter emitter = new SseEmitter(60*60*1000L); //1시간
        emitters.put(userId, emitter);

        //연결 종료시 정리
        emitter.onCompletion(()->emitters.remove(userId));
        emitter.onTimeout(()->emitters.remove(userId));
        emitter.onError(e->emitters.remove(userId));

        return emitter;
    }

    //이벤트 전송하기
    public void sendNewMessage(Long receiverId, Long messageId){
        SseEmitter emitter = emitters.get(receiverId);
        if(emitter == null){
            return;
        }

        try{
            emitter.send(
                    SseEmitter.event()
                            .name("새로운 메시지")
                            .data(messageId)
            );
        }catch (IOException e){
            emitters.remove(receiverId);
        }
    }
}
