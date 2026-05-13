package com.github.fabricio.services;

import com.github.fabricio.buscasemantica.MovieDto;
import dev.langchain4j.model.embedding.EmbeddingModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmbeddingCalculator {

    @Inject
    EmbeddingModel embeddingModel;

    public float[] calculateVector(final MovieDto movieDto) {
        return calculateVector(movieDto.plot());
    }

    public float[] calculateVector(final String text) {
        var embeddingResponse = embeddingModel.embed(text);
        return embeddingResponse.content().vector();
    }
}
