package com.binebli.demospringai;

import javax.validation.Valid;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TopSongsController {
  private final ChatClient chatClient;

  @Value("classpath:/templates/topSongsPromptTemplate.st")
  Resource topSongsPrompt;

  public TopSongsController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @PostMapping(path = "/topSongs", produces = "application/json")
  public List<String> topSongs(@RequestParam String year) {
    return chatClient
        .prompt()
        .user(
            promptUserSpec -> {
              promptUserSpec.text(topSongsPrompt).param("year", year);
            })
        .call()
        .entity(new ParameterizedTypeReference<List<String>>() {});
  }
}
