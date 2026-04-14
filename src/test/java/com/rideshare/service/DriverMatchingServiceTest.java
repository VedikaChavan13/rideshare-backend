package com.rideshare.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import java.util.List;
import org.junit.jupiter.api.Test;

class DriverMatchingServiceTest {

    private final DriverLocationRepository driverLocationRepository = mock(DriverLocationRepository.class);
    private final DriverRepository driverRepository = mock(DriverRepository.class);
    private final ActiveDriverCacheService activeDriverCacheService = mock(ActiveDriverCacheService.class);

    private final DriverMatchingService driverMatchingService =
            new DriverMatchingService(driverLocationRepository, driverRepository, activeDriverCacheService);

    @Test
    void shouldReturnNearestAvailableDriverUsingLinearScan() {
        Driver nearDriver = new Driver(
                "Driver One",
                "1111111111",
                "CAR-111",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Driver farDriver = new Driver(
                "Driver Two",
                "2222222222",
                "CAR-222",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        DriverLocation nearLocation = new DriverLocation(
                nearDriver,
                BigDecimal.valueOf(40.7128),
                BigDecimal.valueOf(-74.0060),
                "40:-74",
                DriverAvailabilityStatus.AVAILABLE,
                LocalDateTime.now()
        );

        DriverLocation farLocation = new DriverLocation(
                farDriver,
                BigDecimal.valueOf(40.7306),
                BigDecimal.valueOf(-73.9352),
                "40:-73",
                DriverAvailabilityStatus.AVAILABLE,
                LocalDateTime.now()
        );

        when(driverLocationRepository.findByAvailabilityStatus(DriverAvailabilityStatus.AVAILABLE))
                .thenReturn(List.of(nearLocation, farLocation));

        Driver matchedDriver = driverMatchingService.findNearestAvailableDriverByLinearScan(
                BigDecimal.valueOf(40.7127),
                BigDecimal.valueOf(-74.0059)
        );

        assertEquals("1111111111", matchedDriver.getPhoneNumber());
    }

    @Test
    void shouldReturnNearestAvailableDriverUsingPriorityQueue() {
        Driver nearDriver = new Driver(
                "Driver One",
                "1111111111",
                "CAR-111",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Driver farDriver = new Driver(
                "Driver Two",
                "2222222222",
                "CAR-222",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        DriverLocation nearLocation = new DriverLocation(
                nearDriver,
                BigDecimal.valueOf(40.7128),
                BigDecimal.valueOf(-74.0060),
                "40:-74",
                DriverAvailabilityStatus.AVAILABLE,
                LocalDateTime.now()
        );

        DriverLocation farLocation = new DriverLocation(
                farDriver,
                BigDecimal.valueOf(40.7306),
                BigDecimal.valueOf(-73.9352),
                "40:-73",
                DriverAvailabilityStatus.AVAILABLE,
                LocalDateTime.now()
        );

        when(driverLocationRepository.findByAvailabilityStatus(DriverAvailabilityStatus.AVAILABLE))
                .thenReturn(List.of(nearLocation, farLocation));

        Driver matchedDriver = driverMatchingService.findNearestAvailableDriverByPriorityQueue(
                BigDecimal.valueOf(40.7127),
                BigDecimal.valueOf(-74.0059)
        );

        assertEquals("1111111111", matchedDriver.getPhoneNumber());
    }

    @Test
    void shouldThrowWhenNoAvailableDriversExistForLinearScan() {
        when(driverLocationRepository.findByAvailabilityStatus(DriverAvailabilityStatus.AVAILABLE))
                .thenReturn(List.of());

        assertThrows(
                IllegalStateException.class,
                () -> driverMatchingService.findNearestAvailableDriverByLinearScan(
                        BigDecimal.valueOf(40.7127),
                        BigDecimal.valueOf(-74.0059)
                )
        );
    }

    @Test
    void shouldThrowWhenNoAvailableDriversExistForPriorityQueue() {
        when(driverLocationRepository.findByAvailabilityStatus(DriverAvailabilityStatus.AVAILABLE))
                .thenReturn(List.of());

        assertThrows(
                IllegalStateException.class,
                () -> driverMatchingService.findNearestAvailableDriverByPriorityQueue(
                        BigDecimal.valueOf(40.7127),
                        BigDecimal.valueOf(-74.0059)
                )
        );
    }
}