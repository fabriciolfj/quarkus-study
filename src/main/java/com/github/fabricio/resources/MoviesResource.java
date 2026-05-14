package com.github.fabricio.resources;

import com.github.fabricio.buscasemantica.MovieDto;
import com.github.fabricio.entities.Movie;
import com.github.fabricio.services.EmbeddingCalculator;
import com.github.fabricio.services.MoviesParserService;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Path("/movies/api")
public class MoviesResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesResource.class);

    @Inject
    MoviesParserService moviesParser;

    @Inject
    EmbeddingCalculator embeddingCalculator;

    @Startup
    @Transactional
    @TransactionConfiguration(timeout = 500)
    public void startup() { }

    public record MovieApiDto(String name, String plot, String director, double rating) { }

    @GET
    @Path("/search")
    public List<MovieApiDto> recommendMovies(@QueryParam("q") String description) {
        float[] plotVector = embeddingCalculator.calculateVector(description);
        LOGGER.info("pesquisa embedding executada");

        List<Movie> movies = Movie.suggestProducts(plotVector, Collections.emptyList());

        return movies.stream()
                .map(m -> new MovieApiDto(m.title, m.plot, m.director, m.rating))
                .toList();
    }
}
