package com.binebli.demospringai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.PostgresChatMemoryRepositoryDialect;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class AiConfig {
  @Autowired
  JdbcChatMemoryRepository chatMemoryRepository;
  @Bean
  public ChatMemory chatMemory() {
      return MessageWindowChatMemory.builder()
              .chatMemoryRepository(chatMemoryRepository)
              .maxMessages(10)
              .build();
  }

  @Bean
  ChatMemoryRepository chatMemoryRepository(DataSource dataSource) {
    return JdbcChatMemoryRepository.builder()
            .dialect(new PostgresChatMemoryRepositoryDialect())
            .dataSource(dataSource)
            .build();
  }

  @Bean
  ChatClient chatClient(
      ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, VectorStore vectorStore) {
    return chatClientBuilder
        .defaultAdvisors(
            MessageChatMemoryAdvisor.builder(chatMemory).build(), // #2
            QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder().build())
                .build())
        .build();
  }
}
