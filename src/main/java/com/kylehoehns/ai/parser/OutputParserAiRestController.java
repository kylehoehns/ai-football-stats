package com.kylehoehns.ai.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/parser")
public class OutputParserAiRestController {

  private final ChatClient chatClient;

  @GetMapping
  public TeamRecord getWinLossRecord(@RequestParam(value = "team") String team) {

    var outputParser = new BeanOutputParser<>(TeamRecord.class);

    var message =
        """
        You are an expert college football analyst.
        Provide the last three years of win-loss records for the {team} college football team.
        {format}
        """;

    var promptTemplate = new PromptTemplate(message, Map.of("team", team, "format", outputParser.getFormat()));
    var prompt = promptTemplate.create();
    var generation = chatClient.generate(prompt).getGeneration();

    return outputParser.parse(generation.getContent());
  }

  public record TeamRecord(String team, List<SeasonRecord> seasonRecords) {}
  public record SeasonRecord(int year, int wins, int losses) {}
}
