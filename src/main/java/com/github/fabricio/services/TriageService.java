package com.github.fabricio.services;

import com.github.fabricio.model.Evaluation;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.List;

@RegisterAiService
public interface TriageService {

    @SystemMessage(
            "Analise se o sentimento expresso no texto"
    )
    @UserMessage("""
            Sua tarefa e analisar a mensagem e classificar como positiva, negativa ou neutra
            A possibilidade de sentimentos
            {#for s in sentiments}
            {s.name()}
            {/for}
            ---
            {{review}}
            ---
            """)
    Evaluation triage(List<Evaluation> sentiments, String review);
}
