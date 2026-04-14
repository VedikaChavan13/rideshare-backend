package com.rideshare.controller;

import com.rideshare.dto.request.RiderRegistrationRequest;
import com.rideshare.dto.response.RiderResponse;
import com.rideshare.service.RiderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/riders")
public class RiderController {

    private final RiderService riderService;

    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RiderResponse registerRider(@Valid @RequestBody RiderRegistrationRequest request) {
        return riderService.registerRider(request);
    }
}