package com.rideshare.controller;

import com.rideshare.dto.request.DriverLocationUpdateRequest;
import com.rideshare.dto.request.DriverRegistrationRequest;
import com.rideshare.dto.response.DriverResponse;
import com.rideshare.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponse registerDriver(@Valid @RequestBody DriverRegistrationRequest request) {
        return driverService.registerDriver(request);
    }

    @PutMapping("/{driverId}/location")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDriverLocation(
            @PathVariable Long driverId,
            @Valid @RequestBody DriverLocationUpdateRequest request
    ) {
        driverService.updateDriverLocation(driverId, request);
    }
}