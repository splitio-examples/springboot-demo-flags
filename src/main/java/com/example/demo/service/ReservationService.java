package com.example.demo.service;

import com.example.demo.repository.ReservationRepository;
import com.example.demo.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final int availableSeats = 40;
    private final Clock clock;

    public ReservationService(
            ReservationRepository reservationRepository,
            Clock clock) {
        this.reservationRepository = reservationRepository;
        this.clock = clock;
    }

    @Autowired
    public ReservationService(@Qualifier("in-memory") ReservationRepository reservationRepository) {
        this(reservationRepository, Clock.systemDefaultZone());
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    private boolean canAccept(
            Reservation newReservation) {
            int takenSeats = reservationRepository.getTakenSeatsForDate(newReservation.getDate());
            return takenSeats + newReservation.getQuantity() <= availableSeats;
    }

    public Optional<String> tryCreateReservation(Reservation reservation) {
        if (!canAccept(reservation))
            return Optional.of("Couldn't accept: not enough seats available.");

        UUID id = UUID.randomUUID();
        reservationRepository.insertReservation(id, reservation);
        return Optional.empty();
    }
}
