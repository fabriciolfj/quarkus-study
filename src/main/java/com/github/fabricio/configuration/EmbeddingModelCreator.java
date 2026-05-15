package com.github.fabricio.configuration;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.OnnxEmbeddingModel;
import dev.langchain4j.model.embedding.onnx.PoolingMode;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.nio.file.Paths;

@ApplicationScoped
public class EmbeddingModelCreator {

    public static final String MODEL_PATH = "./src/main/protected";

    @Produces
    @Named("onnx")
    public EmbeddingModel create() {
        PoolingMode poolingMode = PoolingMode.MEAN; //transforma o vetor de cada token em um unico vetor representando o texto inteiro
        String model = Paths.get(MODEL_PATH, "model.onnx")
                .toAbsolutePath()
                .toString();

        String tokenizer = Paths.get(MODEL_PATH, "tokenizer.json")
                .toAbsolutePath().toString();

        return new OnnxEmbeddingModel(model, tokenizer, poolingMode);
    }

    @Produces
    @Named("ollama")
    public EmbeddingModel createOllama() {
        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("nomic-embed-text")
                .build();

        return embeddingModel;
    }
}
