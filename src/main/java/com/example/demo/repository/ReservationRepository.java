package com.example.demo.repository;

import com.example.demo.model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository {
    List<Reservation> getAllReservations();

    void insertReservation(UUID id, Reservation reservation);

    List<Reservation> getAllReservationsForDate(LocalDate date);

    int getTakenSeatsForDate(LocalDate date);
}
