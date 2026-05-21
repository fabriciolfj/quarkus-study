package com.github.fabricio.services;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.inject.Singleton;

@Singleton
@RegisterAiService(retrievalAugmentor = RegisterAiService.NoRetrievalAugmentorSupplier.class)
public interface AssistantService {

    @UserMessage("""
            voce e uma assistente para tarefas pergunta e reposta
            use o rag para responder a questao.
            se nao sober, diga nao sei.
            use o maximo de sentenças e manternha a resposta concisa.
            
            Question: {{question}}            
            """)
    String generate(String question);
}
