package com.example.demo.repository;

import com.example.demo.model.Reservation;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository("in-memory")
public class ReservationInMemoryRepository implements ReservationRepository {

    private List<Reservation> db = new ArrayList<>();

    @Override
    public List<Reservation> getAllReservations() {
        return db;
    }

    @Override
    public void insertReservation(UUID id, Reservation reservation) {
        Reservation r = new Reservation(
            id,
            reservation.getDate(),
            reservation.getName(),
            reservation.getEmail(),
            reservation.getQuantity()
        );

        db.add(r);
    }

    @Override
    public List<Reservation> getAllReservationsForDate(LocalDate date) {
        return db.stream()
            .filter(x -> x.getDate().equals(date))
            .collect(Collectors.toList());
    }

    @Override
    public int getTakenSeatsForDate(LocalDate date) {
        return getAllReservationsForDate(date)
            .stream()
            .mapToInt(x -> x.getQuantity()).sum();
    }
}
