package com.github.fabricio.services;

import com.github.fabricio.dto.PaymentDTO;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ApplicationScoped
public class PaymentService {

    private final MeterRegistry meterRegistry;
    private final AtomicInteger PAYMENT_IN_PROGRESS = new AtomicInteger(0);

    public PaymentService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    void init() {
        Gauge.builder("payment.current", PAYMENT_IN_PROGRESS, AtomicInteger::get)
                .description("Pagamentos em andamento")
                .register(meterRegistry);
    }

    @Timed(percentiles = {0.5, 0.95, 0.99}, value = "payment.duration")
    @Counted(description = "count payment", value = "payment.send")
    public void sendPayment(final PaymentDTO dto) {
        PAYMENT_IN_PROGRESS.incrementAndGet();
        try {
            log.info("payment received: {}", dto.description());
        } finally {
            PAYMENT_IN_PROGRESS.decrementAndGet();
        }
    }
}