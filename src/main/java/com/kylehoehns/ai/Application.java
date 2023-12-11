package com.kylehoehns.ai;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.SimplePersistentVectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    SimplePersistentVectorStore vectorStore(EmbeddingClient embeddingClient) {
        return new SimplePersistentVectorStore(embeddingClient);
    }

}
