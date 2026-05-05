package com.github.fabricio.resources;

import com.github.fabricio.model.Evaluation;
import com.github.fabricio.services.TriageService;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import jakarta.inject.Inject;

import java.util.List;

@WebSocket(path = "/chat")
public class WebSocketChatBot {

    @Inject
    TriageService triageService;

    @OnTextMessage
    public String onMessage(String message) {
        Evaluation evaluation = triageService.triage(
                List.of(Evaluation.values()),
                message);

        return evaluation.name();
    }
}
