package com.example.demo.controller;

import com.example.demo.model.Reservation;
import com.example.demo.service.ReservationServiceImpl;
import io.split.client.SplitClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    @Value("#{ @environment['split.api.token'] }")
    private String apiToken;

    private final ReservationServiceImpl reservationService;
    private final SplitClient splitClient;

    public ReservationController(ReservationServiceImpl reservationService, SplitClient splitClient) {
        this.reservationService = reservationService;
        this.splitClient = splitClient;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping
    public void addNewReservation(@RequestBody Reservation reservation) {

        String treatment = splitClient.getTreatment(apiToken,"VALIDATE-DATE");
        boolean validateAgainstPastDate;

        if (treatment.equals("on")) {
            validateAgainstPastDate = true;
        } else if (treatment.equals("off")) {
            validateAgainstPastDate = false;
        } else {
            throw new RuntimeException("Couldn't retrieve treatment from Split.");
        }

        Optional<String> errorMessage = reservationService.tryCreateReservation(reservation, validateAgainstPastDate);

        if (errorMessage.isPresent()) {
            String error = errorMessage.get();
            String message = "";
            HttpStatus status = HttpStatus.OK;

            if (error.equals("no-seats")) {
                message = "Couldn't accept: there aren't enough seats available.";
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            } else if (error.equals("past-date")) {
                message = "The specified date is in the past.";
                status = HttpStatus.BAD_REQUEST;
            }

            throw new ResponseStatusException(status, message);
        }
    }
}
