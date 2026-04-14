package com.rideshare.service;

import com.rideshare.cache.ActiveDriverCacheService;
import com.rideshare.domain.enums.DriverAvailabilityStatus;
import com.rideshare.domain.model.Driver;
import com.rideshare.domain.model.DriverLocation;
import com.rideshare.repository.DriverLocationRepository;
import com.rideshare.repository.DriverRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DriverMatchingService {

    private final DriverLocationRepository driverLocationRepository;
    private final DriverRepository driverRepository;
    private final ActiveDriverCacheService activeDriverCacheService;

    public DriverMatchingService(
            DriverLocationRepository driverLocationRepository,
            DriverRepository driverRepository,
            ActiveDriverCacheService activeDriverCacheService
    ) {
        this.driverLocationRepository = driverLocationRepository;
        this.driverRepository = driverRepository;
        this.activeDriverCacheService = activeDriverCacheService;
    }

    public Driver findNearestAvailableDriver(BigDecimal pickupLatitude, BigDecimal pickupLongitude) {
        return findAndClaimNearestAvailableDriver(pickupLatitude, pickupLongitude);
    }

    public Driver findAndClaimNearestAvailableDriver(BigDecimal pickupLatitude, BigDecimal pickupLongitude) {
        List<DriverLocation> lockedAvailableDriverLocations =
                driverLocationRepository.findByAvailabilityStatusOrderByLastUpdatedAtAsc(
                        DriverAvailabilityStatus.AVAILABLE
                );

        DriverLocation chosenLocation = lockedAvailableDriverLocations.stream()
                .min(Comparator.comparingDouble(location ->
                        haversineDistanceKm(
                                pickupLatitude.doubleValue(),
                                pickupLongitude.doubleValue(),
                                location.getLatitude().doubleValue(),
                                location.getLongitude().doubleValue()
                        )))
                .orElseThrow(() -> new IllegalStateException("No available drivers found"));

        chosenLocation.updateLocation(
                chosenLocation.getLatitude(),
                chosenLocation.getLongitude(),
                chosenLocation.getZoneKey(),
                DriverAvailabilityStatus.ON_TRIP,
                LocalDateTime.now()
        );

        driverLocationRepository.save(chosenLocation);
        activeDriverCacheService.syncDriverLocation(chosenLocation);
        return chosenLocation.getDriver();
    }

    public Driver findNearestAvailableDriverByLinearScan(BigDecimal pickupLatitude, BigDecimal pickupLongitude) {
        List<DriverLocation> availableDriverLocations =
                driverLocationRepository.findByAvailabilityStatus(DriverAvailabilityStatus.AVAILABLE);

        return availableDriverLocations.stream()
                .min(Comparator.comparingDouble(location ->
                        haversineDistanceKm(
                                pickupLatitude.doubleValue(),
                                pickupLongitude.doubleValue(),
                                location.getLatitude().doubleValue(),
                                location.getLongitude().doubleValue()
                        )))
                .map(DriverLocation::getDriver)
                .orElseThrow(() -> new IllegalStateException("No available drivers found"));
    }

    public Driver findNearestAvailableDriverByPriorityQueue(BigDecimal pickupLatitude, BigDecimal pickupLongitude) {
        List<DriverLocation> availableDriverLocations =
                driverLocationRepository.findByAvailabilityStatus(DriverAvailabilityStatus.AVAILABLE);

        if (availableDriverLocations.isEmpty()) {
            throw new IllegalStateException("No available drivers found");
        }

        PriorityQueue<DriverDistanceCandidate> minHeap =
                new PriorityQueue<>(Comparator.comparingDouble(DriverDistanceCandidate::distanceKm));

        for (DriverLocation location : availableDriverLocations) {
            double distance = haversineDistanceKm(
                    pickupLatitude.doubleValue(),
                    pickupLongitude.doubleValue(),
                    location.getLatitude().doubleValue(),
                    location.getLongitude().doubleValue()
            );

            minHeap.offer(new DriverDistanceCandidate(location.getDriver(), distance));
        }

        return minHeap.remove().driver();
    }

    public List<Driver> getCachedActiveDriversForZone(BigDecimal pickupLatitude, BigDecimal pickupLongitude) {
        Set<Long> cachedDriverIds = activeDriverCacheService.getActiveDriverIdsForZone(pickupLatitude, pickupLongitude);

        if (!cachedDriverIds.isEmpty()) {
            return driverRepository.findByIdIn(cachedDriverIds);
        }

        String zoneKey = pickupLatitude.intValue() + ":" + pickupLongitude.intValue();

        List<DriverLocation> dbDrivers = driverLocationRepository.findByAvailabilityStatusAndZoneKey(
                DriverAvailabilityStatus.AVAILABLE,
                zoneKey
        );

        Set<Long> driverIds = dbDrivers.stream()
                .map(location -> location.getDriver().getId())
                .collect(Collectors.toSet());

        activeDriverCacheService.populateZone(pickupLatitude, pickupLongitude, driverIds);
        return driverRepository.findByIdIn(driverIds);
    }

    double haversineDistanceKm(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {
        final double earthRadiusKm = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }

    record DriverDistanceCandidate(Driver driver, double distanceKm) {
    }
}