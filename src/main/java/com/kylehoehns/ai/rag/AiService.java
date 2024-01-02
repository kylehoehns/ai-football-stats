package com.kylehoehns.ai.rag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AiService {

  @Value("classpath:/prompts/rag-prompt.st")
  private Resource prompt;

  private final ChatClient chatClient;

  Generation generate(String message, List<Document> similarDocuments) {
    log.info("Asking AI model to reply to question...");
    var systemPrompt = buildSystemPromptFromTemplate(similarDocuments);
    var userMessage = new UserMessage(message);
    var prompt = new Prompt(List.of(systemPrompt, userMessage));
    return chatClient.generate(prompt).getGeneration();
  }

  private Message buildSystemPromptFromTemplate(List<Document> similarDocuments) {
    String documents = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));
    var promptTemplate = new SystemPromptTemplate(prompt);
    return promptTemplate.createMessage(Map.of("documents", documents));
  }
}
