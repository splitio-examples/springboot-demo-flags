package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public class Reservation {
    private final UUID id;
    private final LocalDate date;
    private final String name;
    private final String email;
    private final int quantity;

    public Reservation(UUID id,
                       @JsonProperty("date") LocalDate date,
                       @JsonProperty("name") String name,
                       @JsonProperty("email") String email,
                       @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.email = email;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getQuantity() {
        return quantity;
    }
}
