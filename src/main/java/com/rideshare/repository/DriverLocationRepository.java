package com.rideshare.repository;

import com.rideshare.domain.enums.DriverAvailabilityStatus;
import com.rideshare.domain.model.DriverLocation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {

    Optional<DriverLocation> findByDriverId(Long driverId);

    List<DriverLocation> findByAvailabilityStatus(DriverAvailabilityStatus availabilityStatus);

    List<DriverLocation> findByAvailabilityStatusAndZoneKey(DriverAvailabilityStatus availabilityStatus, String zoneKey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<DriverLocation> findByAvailabilityStatusOrderByLastUpdatedAtAsc(DriverAvailabilityStatus availabilityStatus);
}