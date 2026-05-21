package com.github.fabricio.services;

import com.github.fabricio.services.QuestionRouter.Route;
import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.inject.Singleton;

@Singleton
@RegisterAiService(retrievalAugmentor = RegisterAiService.NoRetrievalAugmentorSupplier.class)
public interface Service {

    @SystemMessage(
            """
                    voce expert em rotear questoes do usuario para embedding store
                    ou modelo geral.
                    Embedding store contains documentos relacionados ao film de volta para o futuro
                    delorean dmc-12 car.
                    Uso do embedding para questoes relacionadas a ao filme de volta pro futuro, delorean.

                    Uso geral quando a questao nao esta relacionada ao filme de volta para futuro ou delorean car.
                    O campo nextNode deve ser um JSON string com valor "EMBEDDING" ou "GENERAL" (com aspas).
                    Exemplo de resposta valida:
                    {
                      "nextNode": "EMBEDDING",
                      "answer": "..."
                    }
                    ou
                    
                    {
                      "nextNode": "GENERAL",
                      "answer": "..."
                    }
                    """
    )
    Route route(String question);
}
