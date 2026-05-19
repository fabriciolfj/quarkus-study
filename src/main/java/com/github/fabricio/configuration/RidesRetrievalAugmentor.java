package com.github.fabricio.configuration;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.function.Supplier;

@ApplicationScoped
public class RidesRetrievalAugmentor implements Supplier<RetrievalAugmentor> {

    private final RetrievalAugmentor retrievalAugmentor;

    public RidesRetrievalAugmentor(final ChromaEmbeddingStore store,
                                   @Named("ollamaEmb")
                                   final EmbeddingModel model) {
        EmbeddingStoreContentRetriever contentRetriever =
                 EmbeddingStoreContentRetriever.builder()
                         .embeddingModel(model)
                         .embeddingStore(store)
                         .maxResults(10)
                         .minScore(0.7)
                         .build();

        this.retrievalAugmentor = DefaultRetrievalAugmentor.builder().contentRetriever(contentRetriever)
                .build();
    }

    @Override
    public RetrievalAugmentor get() {
        return this.retrievalAugmentor;
    }
}
