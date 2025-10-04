package com.binebli.demospringai;


import reactor.core.publisher.Flux;

public interface BoardGameService {
    Flux<String> askQuestion(Question question);
}
