package com.binebli.demospringai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

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
      ChatClient.Builder chatClientBuilder, GameRulesService gameRulesService) {
    ChatOptions options = ChatOptions.builder().model("gpt-4o-mini").build();
    this.chatClient = chatClientBuilder.defaultOptions(options).build();
    this.gameRulesService = gameRulesService;
  }

  @Override
  public Answer askQuestion(Question question) {
    String gameRules = gameRulesService.getRulesFor(question.gameTitle());
    String answerText =
        chatClient
            .prompt()
            .system(
                promptSystemSpec -> promptSystemSpec
                    .text(systemPromptTemplate)
                    .param("gameTitle", question.gameTitle())
                    .param("rules", gameRules))
            .user(question.question())
            .call()
            .content();
    return new Answer(question.gameTitle(), answerText);
  }
}
