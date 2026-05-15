package com.github.fabricio.configuration;

import com.github.fabricio.entities.Movie;
import com.github.fabricio.services.EmbeddingCalculator;
import com.github.fabricio.services.MoviesParserService;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.random.RandomGenerator;

@ApplicationScoped
public class StartMovieDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartMovieDataService.class);

    private static final RandomGenerator RANDOM_GENERATOR = RandomGenerator.getDefault();

    @Inject
    MoviesParserService moviesParse;

    @Inject
    EmbeddingCalculator embeddingCalculator;

    /*@Transactional
    @TransactionConfiguration(timeout = 500)
    public void onStart(@Observes StartupEvent event) {
        LOGGER.info("iniciou");
        var movieDtos = moviesParse.loadMoviesGreaterThanReleaseDate(2007);

        var entities = movieDtos.stream()
                .filter(Objects::nonNull)
                .map(m -> {
                    float[] vector = embeddingCalculator.calculateVector(m);
                    return new Movie(m.title(), m.director(), m.plot(), calculateRating(), vector);
                }).toList();

        entities.forEach(movie -> movie.persist());

        LOGGER.info("fim");
    }*/

    private static double calculateRating() {
        return RANDOM_GENERATOR.nextDouble();
    }
}
