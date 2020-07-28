package com.example.demo.service;

import com.example.demo.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    Optional<String> tryCreateReservation(Reservation reservation, boolean validatePastDate);
    List<Reservation> getAllReservations();
}
