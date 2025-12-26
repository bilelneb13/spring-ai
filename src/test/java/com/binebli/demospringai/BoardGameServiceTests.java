package com.binebli.demospringai;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardGameServiceTests {
  @Autowired private SpringAiBoardGameService service;
  private RelevancyEvaluator relevancyEvaluator;
  @Autowired private ChatClient.Builder chatClientBuilder;
  private FactCheckingEvaluator factCheckingEvaluator;

  @BeforeEach
  public void setup() {
    relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
    factCheckingEvaluator = FactCheckingEvaluator.builder(chatClientBuilder).build();
  }

  //    @Test
  //    public void evaluateRelevancy() {
  //        String userText = "Why is the sky blue?";
  //        Question question = new Question(userText);
  //        Answer answer = service.askQuestion(question);
  //        EvaluationRequest evaluationRequest = new EvaluationRequest(userText, answer.answer());
  //        EvaluationResponse response = relevancyEvaluator.evaluate(evaluationRequest);
  //        Assertions.assertThat(response.isPass()).withFailMessage("""
  //                ========================================
  //                The answer "%s"
  //                is not considered relevant to the question
  //                "%s".
  //                ========================================
  //                """, answer.answer(), userText).isTrue();
  //    }

  //    @Test
  //    public void evaluateFactualAccuracy() {
  //        String userText = "Why is the sky blue?";
  //        Question question = new Question(userText);
  //        Answer answer = service.askQuestion(question);
  //        String referenceAnswer = "The sky is blue because of that was the paint color that was
  // on sale.";
  //        EvaluationRequest evaluationRequest = new EvaluationRequest(userText, answer.answer());
  //        EvaluationResponse evaluationResponse =
  // factCheckingEvaluator.evaluate(evaluationRequest);
  //        Assertions.assertThat(evaluationResponse.isPass())
  //                .withFailMessage("""
  //                        ========================================
  //                        The answer "%s"
  //                        is not considered correct for the question
  //                        "%s".
  //                        ========================================
  //                        """, answer.answer(), userText)
  //                .isTrue();
  //    }
}
