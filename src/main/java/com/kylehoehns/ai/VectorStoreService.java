package com.kylehoehns.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.retriever.VectorStoreRetriever;
import org.springframework.ai.vectorstore.SimplePersistentVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VectorStoreService {

  @Value("classpath:/data/iowa-team-first-downs.md")
  private Resource firstDownsResource;

  @Value("classpath:/data/iowa-team-kicking.md")
  private Resource kickingResource;

  @Value("classpath:/data/iowa-team-misc.md")
  private Resource miscResource;

  @Value("classpath:/data/iowa-team-passing.md")
  private Resource passingResource;

  @Value("classpath:/data/iowa-team-rushing.md")
  private Resource rushingResource;

  @Value("classpath:/data/iowa-team-scoring.md")
  private Resource scoringResource;

  @Value("classpath:/data/iowa-team-total-offense.md")
  private Resource totalOffenseResource;

  @Value("classpath:/data/iowa-football-vector-store")
  private Resource iowaFootballVectorStoreResource;

  private final SimplePersistentVectorStore vectorStore;

  public VectorStoreService(SimplePersistentVectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

  void populateVectorStoreWithEmbeddings() throws IOException {
    log.info("Loading Documents...");
    var resources = List.of(
      firstDownsResource,
      kickingResource,
      miscResource,
      passingResource,
      rushingResource,
      scoringResource,
      totalOffenseResource
    );
    List<Document> documents = new ArrayList<>();
    for (var resource : resources) {
      TextReader textReader = new TextReader(resource);
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
    log.info("Found {} similar documents", documents.size());
    return documents;
  }

}
