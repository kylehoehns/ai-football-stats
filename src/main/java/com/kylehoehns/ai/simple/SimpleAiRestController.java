package com.kylehoehns.ai.simple;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/example")
public class SimpleAiRestController {

  private static final String DEFAULT_QUESTION
      = "What year did the Iowa Hawkeyes football team last win 10 games in a season?";

  private final ChatClient chatClient;

  @GetMapping
  public String getFootballFact(@RequestParam(value = "question", defaultValue = DEFAULT_QUESTION) String question) {
    return chatClient.generate(question);
  }
}
