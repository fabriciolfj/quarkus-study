package com.github.fabricio.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Ride {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private double rating;
}
