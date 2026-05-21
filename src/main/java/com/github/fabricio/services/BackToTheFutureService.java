package com.github.fabricio.services;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.inject.Singleton;

@Singleton
@RegisterAiService(retrievalAugmentor =  EmbeddingStoreRetrieval.class)
public interface BackToTheFutureService {

    @UserMessage("""
            voce e uma assistente de tarefa perguntas e respostas.
            Segue usando retrieved context para responder as questoes.
            Se nao souber a respostas, apenas diga nao sei.
            Use aquelas sentences ao maximo e mantenha a repostas concisa.
            
            Questao: {{question}}
            """)
    String generate(String question);
}
