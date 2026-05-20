package com.github.fabricio.dto;

import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class State extends AgentState {

    public State() {
        super(Map.of());
    }

    public State(Map<String, Object> initData) {
        super(initData);
    }

    public Optional<Integer> age() {
        return value("age");
    }

    public Optional<String> message() {
        return value("message");
    }

    Map<String, Object> surroundCase(State state) {
        return Map.of("message", "test");
    }

    Map<String, Object> setsMessage(State state) {
        return Map.of("message", "current age " + state.age().get());
    }

    public CompiledGraph<State> createGraph() throws GraphStateException {
        return new StateGraph<>(State::new)
                .addNode("createMessage", AsyncNodeAction.node_async(this::setsMessage))
                .addEdge(StateGraph.START, "createMessage")
                .addNode("toUpperCase",
                        AsyncNodeAction.node_async(
                        state -> Map.of("message", state.message().get().toUpperCase())))
                .addNode("surroundCase", AsyncNodeAction.node_async(this::surroundCase))
                .addConditionalEdges("createMessage", AsyncEdgeAction.edge_async(state ->
                        state.age().map(age -> age >= 18 ? "adult" : "minor")
                                .orElse("minor")),
                        Map.of("minor", "surroundCase",
                                "adult", "toUpperCase"))
                .addEdge("toUpperCase", StateGraph.END)
                .addEdge("surroundCase", StateGraph.END)
                .compile();

    }

    static void main() throws GraphStateException {
        final State state = new State();
        final CompiledGraph<State> graph = state.createGraph();
        final Optional<State> finalState = graph.invoke(
                Map.of("age", 16)
        );

        final State stateResult = finalState.get();
        IO.println(stateResult.message().get());
    }
}
