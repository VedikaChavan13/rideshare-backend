package com.rideshare.service;

import com.rideshare.cache.ActiveDriverCacheService;
import com.rideshare.domain.model.Driver;
import com.rideshare.domain.model.DriverLocation;
import com.rideshare.dto.request.DriverLocationUpdateRequest;
import com.rideshare.dto.request.DriverRegistrationRequest;
import com.rideshare.dto.response.DriverResponse;
import com.rideshare.repository.DriverLocationRepository;
import com.rideshare.repository.DriverRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverLocationRepository driverLocationRepository;
    private final ActiveDriverCacheService activeDriverCacheService;

    public DriverService(
            DriverRepository driverRepository,
            DriverLocationRepository driverLocationRepository,
            ActiveDriverCacheService activeDriverCacheService
    ) {
        this.driverRepository = driverRepository;
        this.driverLocationRepository = driverLocationRepository;
        this.activeDriverCacheService = activeDriverCacheService;
    }

    @Transactional
    public DriverResponse registerDriver(DriverRegistrationRequest request) {
        Driver driver = new Driver(
                request.getFullName(),
                request.getPhoneNumber(),
                request.getVehicleNumber(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Driver savedDriver = driverRepository.save(driver);

        return new DriverResponse(
                savedDriver.getId(),
                savedDriver.getFullName(),
                savedDriver.getPhoneNumber(),
                savedDriver.getVehicleNumber()
        );
    }

    @Transactional
    public void updateDriverLocation(Long driverId, DriverLocationUpdateRequest request) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));

        String zoneKey = zoneKey(request.getLatitude(), request.getLongitude());

        DriverLocation driverLocation = driverLocationRepository.findByDriverId(driverId)
                .orElseGet(() -> new DriverLocation(
                        driver,
                        request.getLatitude(),
                        request.getLongitude(),
                        zoneKey,
                        request.getAvailabilityStatus(),
                        LocalDateTime.now()
                ));

        if (driverLocation.getId() != null) {
            driverLocation.updateLocation(
                    request.getLatitude(),
                    request.getLongitude(),
                    zoneKey,
                    request.getAvailabilityStatus(),
                    LocalDateTime.now()
            );
        }

        DriverLocation saved = driverLocationRepository.save(driverLocation);
        activeDriverCacheService.syncDriverLocation(saved);
    }

    private String zoneKey(BigDecimal latitude, BigDecimal longitude) {
        return latitude.intValue() + ":" + longitude.intValue();
    }
}