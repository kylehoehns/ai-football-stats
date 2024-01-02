package com.kylehoehns.ai.rag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.Generation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class RagAiRestController {

  private final VectorStoreService vectorStoreService;
  private final AiService aiService;

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

  public record InputPrompt(String message) {}

}