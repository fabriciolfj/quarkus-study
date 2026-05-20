package com.github.fabricio.dto;

import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AppenderState extends AgentState {

    static Map<String, Channel<?>> SCHEMA = Map.of(
            "message", Channels.appender(ArrayList::new)
    );

    public AppenderState() {
        super(Map.of());
    }

    public AppenderState(Map<String, Object> initData) {
        super(initData);
    }

    public Optional<Integer> age() {
        return value("age");
    }

    Map<String, Object> setsMessage(AppenderState state) {
        return Map.of("message", "current age " + state.age().get());
    }

    List<String> message() {
        return this.<List<String>>value("message").orElseGet(ArrayList::new);
    }

    public Optional<String> lastMessage() {
        var messages = this.message();
        return messages.isEmpty() ? Optional.empty() : Optional.of(messages.getLast());
    }

    public CompiledGraph<AppenderState> createGraph() throws GraphStateException {
        return new StateGraph<>(AppenderState.SCHEMA, AppenderState::new)
                .addNode("createMessage", AsyncNodeAction.node_async(this::setsMessage))
                .addEdge(StateGraph.START, "createMessage")
                .addNode("toUpperCase", AsyncNodeAction.node_async(state ->
                        Map.of("message", state.lastMessage().get().toUpperCase())))
                .addEdge("createMessage", "toUpperCase")
                .addEdge("toUpperCase", StateGraph.END)
                .compile();
    }

    static void main() throws GraphStateException {
        AppenderState appenderState = new AppenderState();
        var graph = appenderState.createGraph();

        var finalState = graph.invoke(Map.of("age", 16));
        IO.println(finalState.get().message());
    }
}
