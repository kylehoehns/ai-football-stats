package com.kylehoehns.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.client.Generation;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
public class AiController {

  private final VectorStoreService vectorStoreService;
  private final AiService aiService;

  @Autowired
  public AiController(AiService aiService, VectorStoreService vectorStoreService) {
    this.aiService = aiService;
    this.vectorStoreService = vectorStoreService;
  }

  @PostMapping("/ai/create-embeddings")
  @ResponseStatus(HttpStatus.CREATED)
  public void createEmbeddings() throws Exception {
    vectorStoreService.populateVectorStoreWithEmbeddings();
  }

  @PostMapping("/ai")
  public Generation rag(@RequestBody InputPrompt inputPrompt) throws Exception {
    var similarDocuments = vectorStoreService.getSimilarDocuments(inputPrompt.message());
    return aiService.generate(inputPrompt.message(), similarDocuments);
  }

}