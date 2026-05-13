package com.github.fabricio.services;

import com.github.fabricio.repositories.RideRepository;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;
import jakarta.enterprise.context.ApplicationScoped;



@RegisterAiService
@ApplicationScoped
public interface ThemeParkChatBot {

    @SystemMessage("""
        Você é um assistente para responder perguntas sobre o parque temático.

        Essas perguntas só podem ser relacionadas ao parque temático.
        Exemplos dessas perguntas:

        - Você pode descrever um determinado brinquedo?
        - Qual é a altura mínima para entrar em um brinquedo?
        - Quais brinquedos posso acessar com a minha altura?
        - Qual é o melhor brinquedo no momento?
        - Qual é o tempo de espera para um determinado brinquedo?

        Se as perguntas não forem sobre o parque temático ou você não souber
        a resposta, você deve sempre retornar "Não sei".
        Não forneça informações incorretas.
        """)
    @UserMessage("""
        O usuário do parque temático tem a seguinte pergunta: {question}

        A resposta deve ter no máximo 2 linhas.
        """)
    @ToolBox({RideRepository.class, WaitingTime.class})
    String chat(@MemoryId int userId, final String question);
}
