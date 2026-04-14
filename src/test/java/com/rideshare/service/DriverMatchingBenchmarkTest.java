package com.rideshare.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.rideshare.cache.ActiveDriverCacheService;
import com.rideshare.domain.enums.DriverAvailabilityStatus;
import com.rideshare.domain.model.Driver;
import com.rideshare.domain.model.DriverLocation;
import com.rideshare.repository.DriverLocationRepository;
import com.rideshare.repository.DriverRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class DriverMatchingBenchmarkTest {

    @Test
    void compareLinearScanVsPriorityQueue() {
        DriverLocationRepository repository = mock(DriverLocationRepository.class);
        DriverRepository driverRepository = mock(DriverRepository.class);
        ActiveDriverCacheService cacheService = mock(ActiveDriverCacheService.class);

        DriverMatchingService service =
                new DriverMatchingService(repository, driverRepository, cacheService);

        List<DriverLocation> locations = generateLocations(5_000);
        when(repository.findByAvailabilityStatus(DriverAvailabilityStatus.AVAILABLE)).thenReturn(locations);

        BigDecimal pickupLat = BigDecimal.valueOf(40.7127);
        BigDecimal pickupLon = BigDecimal.valueOf(-74.0059);

        long linearStart = System.nanoTime();
        Driver linearResult = service.findNearestAvailableDriverByLinearScan(pickupLat, pickupLon);
        long linearDuration = System.nanoTime() - linearStart;

        long heapStart = System.nanoTime();
        Driver heapResult = service.findNearestAvailableDriverByPriorityQueue(pickupLat, pickupLon);
        long heapDuration = System.nanoTime() - heapStart;

        assertNotNull(linearResult);
        assertNotNull(heapResult);

        System.out.println("Linear scan duration (ns): " + linearDuration);
        System.out.println("PriorityQueue duration (ns): " + heapDuration);
    }

    private List<DriverLocation> generateLocations(int count) {
        Random random = new Random(42);
        List<DriverLocation> locations = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            Driver driver = new Driver(
                    "Driver-" + i,
                    "900000" + i,
                    "CAR-" + i,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            double lat = 40.5 + random.nextDouble() * 0.5;
            double lon = -74.2 + random.nextDouble() * 0.5;
            String zoneKey = ((int) lat) + ":" + ((int) lon);

            DriverLocation location = new DriverLocation(
                    driver,
                    BigDecimal.valueOf(lat),
                    BigDecimal.valueOf(lon),
                    zoneKey,
                    DriverAvailabilityStatus.AVAILABLE,
                    LocalDateTime.now()
            );

            locations.add(location);
        }

        return locations;
    }
}