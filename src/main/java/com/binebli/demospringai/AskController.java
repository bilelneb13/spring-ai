package com.binebli.demospringai;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RestController
public class AskController {
    private final BoardGameService boardGameService;

    public AskController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

//    @PostMapping(path = "/ask", produces = "text/event-stream")
    @PostMapping(path = "/ask", produces = "application/json")
    public Answer askQuestion(@RequestHeader(name="X_AI_CONVERSATION_ID",
            defaultValue = "default") String conversationId, @Valid @RequestBody Question question) {
        return boardGameService.askQuestion(question, conversationId);
    }

}
