package com.github.fabricio.services;

import com.github.fabricio.dto.State;
import dev.langchain4j.model.output.structured.Description;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class QuestionRouter {

    public enum Type {
        EMBEDDING("embedding"),
        GENERAL("general");

        public final String nodeName;

        Type(String nodeName) {
            this.nodeName = nodeName;
        }
    }

    public static class Route {
        @Description("dado a questao do usuario, escolha a rota para geral ou embedding")
        Type nextNode;
        String answer;
    }

    @Inject
    Service service;

    public String routeToNode(String query) {
        return service.route(query).nextNode.nodeName;
    }
}
