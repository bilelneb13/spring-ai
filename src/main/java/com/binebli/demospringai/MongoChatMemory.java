package com.binebli.demospringai;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MongoChatMemory implements ChatMemory {
  private final ConversationRepository conversationRepository;

  public MongoChatMemory(ConversationRepository conversationRepository) {
    this.conversationRepository = conversationRepository;
  }

  @Override
  public void add(String conversationId, List<Message> messages) {
    var conversationMessages =
        messages.stream()
            .map(
                message ->
                    new ConversationMessage(message.getMessageType().getValue(), message.getText()))
            .toList();
    conversationRepository
        .findById(conversationId)
        .ifPresentOrElse(
            conversation -> {
              List<ConversationMessage> existingConversation = conversation.messages();
              existingConversation.addAll(conversationMessages);
              conversationRepository.save(new Conversation(conversationId, existingConversation));
            },
            () ->
                conversationRepository.save(
                    new Conversation(conversationId, conversationMessages)));
    var conversation = new Conversation(conversationId, conversationMessages);
    conversationRepository.save(conversation);
  }

  @Override
  public List<Message> get(String conversationId) {
    return conversationRepository
        .findById(conversationId)
        .map(
            conversation -> {
              List<Message> messageList =
                  conversation.messages().stream()
                      .map(
                          conversationMessage -> {
                            String messageType = conversationMessage.messageType();
                              return messageType.equals(MessageType.USER.getValue()) ?
                                      new UserMessage(conversationMessage.content()) :
                                      new AssistantMessage(conversationMessage.content());
                          }).collect(Collectors.toList());
              return messageList;
            })
        .orElse(Collections.emptyList());
  }

  @Override
  public void clear(String conversationId) {}
}
