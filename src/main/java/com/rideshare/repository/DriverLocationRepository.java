package com.rideshare.repository;

import com.rideshare.domain.model.DriverLocation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {
    Optional<DriverLocation> findByDriverId(Long driverId);
}