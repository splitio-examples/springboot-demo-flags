package com.example.demo.service;

import com.example.demo.model.Reservation;
import com.example.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final int availableSeats = 40;
    private final Clock clock;

    public ReservationServiceImpl(@Qualifier("in-memory") ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        this.clock = Clock.systemDefaultZone();
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    private boolean canAccept(Reservation newReservation) {
        int takenSeats = reservationRepository.getTakenSeatsForDate(newReservation.getDate());
        return takenSeats + newReservation.getQuantity() <= availableSeats;
    }

    @Override
    public Optional<String> tryCreateReservation(Reservation reservation, boolean validatePastDate) {

        LocalDate today = clock.instant().atZone(clock.getZone()).toLocalDate();

        if (validatePastDate && reservation.getDate().isBefore(today)) {
            return Optional.of("past-date");
        }

        if (!canAccept(reservation)) {
            return Optional.of("no-seats");
        }

        UUID id = UUID.randomUUID();
        reservationRepository.insertReservation(id, reservation);
        return Optional.empty();
    }
}
