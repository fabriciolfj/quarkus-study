package com.github.fabricio.services;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.OnnxEmbeddingModel;
import dev.langchain4j.model.embedding.onnx.PoolingMode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

import java.nio.file.Paths;

@ApplicationScoped
public class EmbeddingModelCreator {

    public static final String MODEL_PATH = "./protected";

    @Produces
    public EmbeddingModel create() {
        PoolingMode poolingMode = PoolingMode.MEAN; //transforma o vetor de cada token em um unico vetor representando o texto inteiro
        String model = Paths.get(MODEL_PATH, "model.onnx")
                .toAbsolutePath()
                .toString();

        String tokenizer = Paths.get(MODEL_PATH, "tokenizer.json")
                .toAbsolutePath().toString();

        return new OnnxEmbeddingModel(model, tokenizer, poolingMode);
    }
}
