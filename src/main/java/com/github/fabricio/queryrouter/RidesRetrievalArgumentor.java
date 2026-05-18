package com.github.fabricio.queryrouter;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import dev.langchain4j.web.search.WebSearchEngine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ApplicationScoped
public class RidesRetrievalArgumentor implements Supplier<RetrievalAugmentor> {

    private final RetrievalAugmentor argumentor;

    public RidesRetrievalArgumentor(ChromaEmbeddingStore store,
                                    @Named("ollama") EmbeddingModel model,
                                    WebSearchEngine searchEngine,
                                    ChatModel languageModel) {

        final ContentRetriever webSearchContentRetriever = WebSearchContentRetriever
                .builder()
                .webSearchEngine(searchEngine)
                .maxResults(3)
                .build();

        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(store)
                .embeddingModel(model)
                .build();

        Map<ContentRetriever, String> routing = new HashMap<>();
        routing.put(webSearchContentRetriever, "travel to the theme park");
        routing.put(contentRetriever, "description of a ride or minimum height to access to a ride");

        QueryRouter queryRouter = new LanguageModelQueryRouter(languageModel, routing);

        this.argumentor = DefaultRetrievalAugmentor.builder()
                .queryTransformer(query -> {
                    String original = query.text();
                    String newQuery = original + System.lineSeparator() + " the theme park is in barcelona";

                    return Collections.singletonList(Query.from(newQuery, query.metadata()));
                })
                .queryRouter(queryRouter)
                .build();
    }

    @Override
    public RetrievalAugmentor get() {
        return argumentor;
    }
}