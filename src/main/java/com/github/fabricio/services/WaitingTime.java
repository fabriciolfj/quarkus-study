package com.github.fabricio.services;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;

@Slf4j
@ApplicationScoped
public class WaitingTime {

    private static final RandomGenerator RANDOM_GENERATOR = new SecureRandom();

    private final ValueCommands<String, Long> timeCommands;

    public WaitingTime(RedisDataSource ds) {
        this.timeCommands = ds.value(Long.class);
    }

    public void setRandomWaitingTime(String attraction) {
        this.setWaitingTime(attraction, RANDOM_GENERATOR.nextLong());
    }

    public void setWaitingTime(String attraction, long waitingTime) {
        this.timeCommands.set(attraction, waitingTime);
    }

    @Tool("obtenha o tempo de espera para a atração com o nome especificado.")
    public long getWaitingTime(@P("attraction name") String attraction) {
        log.info("gets waiting time for %s", attraction);

        return this.timeCommands.get(attraction);
    }
}
