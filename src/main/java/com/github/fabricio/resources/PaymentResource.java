package com.github.fabricio.resources;

import com.github.fabricio.dto.PaymentDTO;
import com.github.fabricio.services.PaymentService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@Path("/api/v1/payment")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class PaymentResource {

    private final PaymentService paymentService;

    @POST
    public Response createPayment(final PaymentDTO dto) {
        paymentService.sendPayment(dto);
        return Response.status(Response.Status.CREATED).build();
    }
}
