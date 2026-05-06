package com.github.fabricio.resources;

import com.github.fabricio.entities.Ride;
import com.github.fabricio.repositories.RideRepository;
import com.github.fabricio.services.ThemeParkChatBot;
import com.github.fabricio.services.WaitingTime;
import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/ride")
public class RideResource {

    @Inject
    RideRepository rideRepository;

    @Inject
    WaitingTime waitingTime;

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
        return this.themeParkChatBot.chat(userId,"qual o melhor brinquedo no momento?");
    }

    @GET
    @Path("/chat/waiting/{userid}")
    public String askForWaitingTime(@PathParam("userid") Integer userId) {
        return  this.themeParkChatBot.chat(userId, "qual o tempo de espera para o dragon fun?");
    }
}
