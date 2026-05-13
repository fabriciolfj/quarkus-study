package com.github.fabricio.services;

import com.github.fabricio.entities.Movie;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.random.RandomGenerator;

@ApplicationScoped
public class StartSertDataService {

    private static final RandomGenerator RANDOM_GENERATOR = RandomGenerator.getDefault();

    @Inject
    MoviesParserService moviesParse;

    @Inject
    EmbeddingCalculator embeddingCalculator;

    @Startup
    @Transactional
    @TransactionConfiguration(timeout = 500)
    public void startup() {
        var movieDtos = moviesParse.loadMoviesGreaterThanReleaseDate(2007);

        movieDtos.stream()
                .map(m -> {
                    float[] vector = embeddingCalculator.calculateVector(m);
                    return new Movie(m.title(), m.director(), m.plot(), calculateRating(), vector);
                }).forEach(movie -> movie.persist());
    }

    private static double calculateRating() {
        return RANDOM_GENERATOR.nextDouble();
    }
}
