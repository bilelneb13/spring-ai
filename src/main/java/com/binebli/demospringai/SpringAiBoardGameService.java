package com.binebli.demospringai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import static org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor.FILTER_EXPRESSION;

@Service
class SpringAiBoardGameService implements BoardGameService {

  private final ChatClient chatClient;
  private final GameRulesService gameRulesService;

  //    private static final String questionPromptTemplate = """
  //            You are a helpful assistant, answering questions about tabletop games.
  //            If you don't know anything about the game or don't know the answer,
  //            say "I don't know".
  //            The game is {game}.
  //            The question is: {question}.
  //            """;
  //  @Value("classpath:/templates/questionPromptTemplate.st")
  //  Resource questionPromptTemplate;
  @Value("classpath:/templates/systemPromptTemplate.st")
  Resource systemPromptTemplate;

  public SpringAiBoardGameService(
      ChatClient chatClient, GameRulesService gameRulesService, VectorStore vectorStore) {
    ChatOptions options = ChatOptions.builder().model("gpt-4o-mini").build();
    this.chatClient = chatClient;
    this.gameRulesService = gameRulesService;
  }

  @Override
  public Answer askQuestion(Question question) {
    String gameNameMatch = String.format(
            "gameTitle == '%s'",
            gameRulesService.normalizeGameTitle(question.gameTitle()));
    return chatClient
        .prompt()
        .system(
            promptSystemSpec ->
                promptSystemSpec
                    .text(systemPromptTemplate)
                    .param("gameTitle", question.gameTitle()))
            .advisors(advisorSpec ->
                    advisorSpec.param(FILTER_EXPRESSION, gameNameMatch))
        .user(question.question())
        .call()
        .entity(Answer.class);
  }
}
