package com.binebli.demospringai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import java.util.List;

public class SelfEvaluatingBoardGameService implements BoardGameService {

    private final ChatClient chatClient;
    private final RelevancyEvaluator evaluator;

    public SelfEvaluatingBoardGameService(ChatClient.Builder chatClientBuilder) {
        ChatOptions options = ChatOptions.builder().model("gpt-4o-mini").build();
        this.chatClient = chatClientBuilder.defaultOptions(options).build();
        this.evaluator = new RelevancyEvaluator(chatClientBuilder);
    }

    @Override
    @Retryable(retryFor = AnswerNotRelevantException.class)
    public Answer askQuestion(Question question) {
        String answerText = chatClient.prompt().user(question.question()).call().content();
        evaluateRelevancy(question, answerText);
        return new Answer(answerText);
    }

    private void evaluateRelevancy(Question question, String answerText) {
        EvaluationRequest request = new EvaluationRequest(question.question(), List.of(), answerText);
        EvaluationResponse evaluate = evaluator.evaluate(request);
        if (!evaluate.isPass()) {
            throw new AnswerNotRelevantException(question.question(), answerText);
        }

    }

    @Recover
    public Answer recover(AnswerNotRelevantException e) {
        return new Answer("I'm sorry, I wasn't able to answer the question");
    }


}
