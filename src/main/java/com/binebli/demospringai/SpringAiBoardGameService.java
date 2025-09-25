package com.binebli.demospringai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;

@Service
class SpringAiBoardGameService implements BoardGameService {

    private final ChatClient chatClient;

    public SpringAiBoardGameService(ChatClient.Builder chatClientBuilder) {
        ChatOptions options = ChatOptions.builder().model("gpt-4o-mini").build();
        this.chatClient = chatClientBuilder.defaultOptions(options).build();
    }

    @Override
    public Answer askQuestion(Question question) {
        String answerText = chatClient.prompt().user(question.question()).call().content();
        return new Answer(answerText);
    }
}
