package com.rideshare.controller;

import com.rideshare.dto.request.RideRequest;
import com.rideshare.dto.response.RideResponse;
import com.rideshare.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RideResponse requestRide(@Valid @RequestBody RideRequest request) {
        return rideService.requestRide(request);
    }
}