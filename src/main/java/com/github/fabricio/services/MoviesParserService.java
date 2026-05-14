package com.github.fabricio.services;

import com.github.fabricio.buscasemantica.MovieDto;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static java.lang.Integer.parseInt;

@ApplicationScoped
public class MoviesParserService {

    @SuppressWarnings("deprecation")
    public List<MovieDto> loadMoviesGreaterThanReleaseDate(int releaseYear) {
        String location = "./src/main/protected/wiki_movie_plots_deduped.csv";

        try (Reader reader = new FileReader(location)) {
            try (CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

                return csvParser
                        .stream()
                        .filter(r -> parseInt(r.get("Release Year")) > releaseYear)
                        .map(r -> new MovieDto(r.get("Title"),
                                r.get("Director"),
                                r.get("Plot")))
                        .toList();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
