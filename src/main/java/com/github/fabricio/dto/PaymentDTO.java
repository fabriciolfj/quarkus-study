package com.github.fabricio.dto;

import java.math.BigDecimal;

public record PaymentDTO(String description, BigDecimal value) {
}
