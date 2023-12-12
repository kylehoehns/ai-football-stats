package com.kylehoehns.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.retriever.VectorStoreRetriever;
import org.springframework.ai.vectorstore.SimplePersistentVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VectorStoreService {

  @Value("classpath:/vector-store/iowa-football-vector-store.json")
  private Resource iowaFootballVectorStoreResource;

  private final SimplePersistentVectorStore vectorStore;
  private final ResourceLoader resourceLoader;

  public VectorStoreService(SimplePersistentVectorStore vectorStore, ResourceLoader resourceLoader) {
    this.vectorStore = vectorStore;
    this.resourceLoader = resourceLoader;
  }

  void populateVectorStoreWithEmbeddings() throws IOException {
    log.info("Loading Documents...");

    var resources = ResourcePatternUtils
        .getResourcePatternResolver(resourceLoader)
        .getResources("classpath:/data/*.md");

    List<Document> documents = new ArrayList<>();
    for (var resource : resources) {
      var textReader = new TextReader(resource);
      documents.addAll(textReader.get());
    }

    log.info("Creating Embeddings...");
    vectorStore.add(documents);

    log.info("Saving Embeddings...");
    vectorStore.save(iowaFootballVectorStoreResource.getFile());
  }

  public List<Document> getSimilarDocuments(String message) throws IOException {
    log.info("Loading Embeddings...");
    vectorStore.load(iowaFootballVectorStoreResource.getFile());
    var documents = new VectorStoreRetriever(vectorStore).retrieve(message);
    var fileNames = documents.stream().map(d -> d.getMetadata().get("source")).toList();
    log.info("Found {} similar documents from {}", documents.size(), fileNames);
    return documents;
  }

}
