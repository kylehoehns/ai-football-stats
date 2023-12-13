package com.kylehoehns.ai.prompt;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.ai.prompt.messages.SystemMessage;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/prompt")
public class PromptAiRestController {

  private static final String DEFAULT_QUESTION
      = "What year did the Iowa football team last win 10 games in a season?";

  private static final String SYSTEM_MESSAGE
      = "Respond to the provided question as an American football color commentator.";

  private final AiClient aiClient;

  @GetMapping
  public AiResponse getFootballFact(@RequestParam(value = "question", defaultValue = DEFAULT_QUESTION) String question) {

    var systemMessage = new SystemMessage(SYSTEM_MESSAGE);
    var userMessage = new UserMessage(question);
    var prompt = new Prompt(List.of(systemMessage, userMessage));

    return aiClient.generate(prompt);
  }

  @Value("classpath:/prompts/prompt.st")
  private Resource template;

  @GetMapping("/template")
  public AiResponse getFootballFactWithTemplate(
      @RequestParam(value = "question", defaultValue = DEFAULT_QUESTION) String question,
      @RequestParam(value = "team", defaultValue = "Iowa") String team) {

    var promptTemplate = new SystemPromptTemplate(template);
    var systemMessage = promptTemplate.createMessage(Map.of("team", team));
    var userMessage = new UserMessage(question);
    var prompt = new Prompt(List.of(systemMessage, userMessage));

    return aiClient.generate(prompt);
  }
}
