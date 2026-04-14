package com.rideshare.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;

class DriverAssignmentConcurrencyTest {

    @Test
    void onlyOneThreadShouldSuccessfullyClaimSingleAvailableDriver() throws Exception {
        DriverLocationRepository repository = mock(DriverLocationRepository.class);
        DriverRepository driverRepository = mock(DriverRepository.class);
        ActiveDriverCacheService cacheService = mock(ActiveDriverCacheService.class);

        Driver driver = new Driver(
                "Driver One",
                "1111111111",
                "CAR-111",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        DriverLocation driverLocation = new DriverLocation(
                driver,
                BigDecimal.valueOf(40.7128),
                BigDecimal.valueOf(-74.0060),
                "40:-74",
                DriverAvailabilityStatus.AVAILABLE,
                LocalDateTime.now()
        );

        Object monitor = new Object();

        when(repository.findByAvailabilityStatusOrderByLastUpdatedAtAsc(DriverAvailabilityStatus.AVAILABLE))
                .thenAnswer(invocation -> {
                    synchronized (monitor) {
                        if (driverLocation.getAvailabilityStatus() == DriverAvailabilityStatus.AVAILABLE) {
                            driverLocation.updateLocation(
                                    driverLocation.getLatitude(),
                                    driverLocation.getLongitude(),
                                    driverLocation.getZoneKey(),
                                    DriverAvailabilityStatus.ON_TRIP,
                                    LocalDateTime.now()
                            );
                            return List.of(driverLocation);
                        }
                        return List.of();
                    }
                });

        doAnswer(invocation -> invocation.getArgument(0))
                .when(repository).save(any(DriverLocation.class));

        DriverMatchingService matchingService =
                new DriverMatchingService(repository, driverRepository, cacheService);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        Callable<Boolean> task = () -> {
            ready.countDown();
            start.await();

            try {
                matchingService.findAndClaimNearestAvailableDriver(
                        BigDecimal.valueOf(40.7127),
                        BigDecimal.valueOf(-74.0059)
                );
                return true;
            } catch (IllegalStateException ex) {
                return false;
            }
        };

        Future<Boolean> future1 = executorService.submit(task);
        Future<Boolean> future2 = executorService.submit(task);

        ready.await();
        start.countDown();

        boolean result1 = future1.get();
        boolean result2 = future2.get();

        executorService.shutdown();

        int successCount = (result1 ? 1 : 0) + (result2 ? 1 : 0);
        int failureCount = (result1 ? 0 : 1) + (result2 ? 0 : 1);

        assertEquals(1, successCount);
        assertEquals(1, failureCount);
        assertTrue(driverLocation.getAvailabilityStatus() == DriverAvailabilityStatus.ON_TRIP);
    }
}