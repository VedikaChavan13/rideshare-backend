package com.rideshare.service;

import com.rideshare.domain.enums.RideEventType;
import com.rideshare.domain.enums.RideStatus;
import com.rideshare.domain.model.Driver;
import com.rideshare.domain.model.Ride;
import com.rideshare.domain.model.RideEvent;
import com.rideshare.domain.model.Rider;
import com.rideshare.dto.request.RideRequest;
import com.rideshare.dto.response.RideResponse;
import com.rideshare.repository.RideEventRepository;
import com.rideshare.repository.RideRepository;
import com.rideshare.repository.RiderRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RideService {

    private static final int MAX_ASSIGNMENT_RETRIES = 3;

    private final RiderRepository riderRepository;
    private final RideRepository rideRepository;
    private final RideEventRepository rideEventRepository;
    private final DriverMatchingService driverMatchingService;

    public RideService(
            RiderRepository riderRepository,
            RideRepository rideRepository,
            RideEventRepository rideEventRepository,
            DriverMatchingService driverMatchingService
    ) {
        this.riderRepository = riderRepository;
        this.rideRepository = rideRepository;
        this.rideEventRepository = rideEventRepository;
        this.driverMatchingService = driverMatchingService;
    }

    @Transactional
    public RideResponse requestRide(RideRequest request) {
        Ride existingRide = rideRepository.findByClientRequestId(request.getClientRequestId())
                .orElse(null);

        if (existingRide != null) {
            return new RideResponse(
                    existingRide.getId(),
                    existingRide.getRider().getId(),
                    existingRide.getDriver().getId(),
                    existingRide.getStatus().name(),
                    existingRide.getClientRequestId()
            );
        }

        Rider rider = riderRepository.findById(request.getRiderId())
                .orElseThrow(() -> new EntityNotFoundException("Rider not found with id: " + request.getRiderId()));

        Driver matchedDriver = assignDriverWithRetry(
                request.getPickupLatitude(),
                request.getPickupLongitude()
        );

        Ride ride = new Ride(
                rider,
                matchedDriver,
                request.getPickupLatitude(),
                request.getPickupLongitude(),
                request.getDropoffLatitude(),
                request.getDropoffLongitude(),
                RideStatus.MATCHED,
                request.getClientRequestId(),
                LocalDateTime.now()
        );

        Ride savedRide = rideRepository.save(ride);

        RideEvent rideRequestedEvent = new RideEvent(
                savedRide,
                RideEventType.RIDE_REQUESTED,
                LocalDateTime.now(),
                "Ride requested by rider"
        );
        rideEventRepository.save(rideRequestedEvent);

        RideEvent driverMatchedEvent = new RideEvent(
                savedRide,
                RideEventType.DRIVER_MATCHED,
                LocalDateTime.now(),
                "Matched driver id: " + matchedDriver.getId()
        );
        rideEventRepository.save(driverMatchedEvent);

        return new RideResponse(
                savedRide.getId(),
                rider.getId(),
                matchedDriver.getId(),
                savedRide.getStatus().name(),
                savedRide.getClientRequestId()
        );
    }

    private Driver assignDriverWithRetry(java.math.BigDecimal pickupLatitude, java.math.BigDecimal pickupLongitude) {
        IllegalStateException lastException = null;

        for (int attempt = 1; attempt <= MAX_ASSIGNMENT_RETRIES; attempt++) {
            try {
                return driverMatchingService.findAndClaimNearestAvailableDriver(pickupLatitude, pickupLongitude);
            } catch (IllegalStateException ex) {
                lastException = ex;
            }
        }

        throw lastException != null ? lastException : new IllegalStateException("Driver assignment failed");
    }
}