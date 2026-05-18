package com.github.fabricio.resources;

import com.github.fabricio.configuration.DocumentFromText;
import com.github.fabricio.entities.Ride;
import com.github.fabricio.repositories.RideRepository;
import com.github.fabricio.services.ThemeParkChatBot;
import com.github.fabricio.services.WaitingTime;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.nio.file.Paths;

import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

@Path("/ride")
public class RideResource {

    @Inject
    @Named("ollama")
    EmbeddingModel embeddingModel;

    @Inject
    RideRepository rideRepository;

    @Inject
    WaitingTime waitingTime;
    @Inject
    DocumentFromText documentFromText;
    @Inject
    ChromaEmbeddingStore embeddingStore;

    @Startup
    @Transactional
    public void populateData() {
        insertRides();
    }

    private void insertRides() {
        Ride r1 = new Ride();
        r1.setName("oncharted");
        r1.setRating(5.0);

        rideRepository.persist(r1);

        waitingTime.setRandomWaitingTime(r1.getName());

        Ride r2 = new Ride();
        r2.setName("dragon fun");
        r2.setRating(4.9);

        rideRepository.persist(r2);
        waitingTime.setRandomWaitingTime(r2.getName());
    }

    @Inject
    ThemeParkChatBot themeParkChatBot;

    @GET
    @Path("/chat/best/{userid}")
    public String askForTheBest(@PathParam("userid") Integer userId) {
        return this.themeParkChatBot.chat("quais voos tenho como opcao para viajar a um park tematico?");
    }

    @GET
    @Path("/chat/waiting/{userid}")
    public String askForWaitingTime(@PathParam("userid") Integer userId) {
        return  this.themeParkChatBot.chat(userId, "como funciona a torre do pavor?");
    }

    public void ingest(@Observes StartupEvent startupEvent) {
        var documents = documentFromText.createDocuments(Paths.get("./rides"));

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor
                .builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .documentSplitter(recursive(300, 30))
                .build();

        ingestor.ingest(documents);
        System.out.println("ingeriu");
    }
}
