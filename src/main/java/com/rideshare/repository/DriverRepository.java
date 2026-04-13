package com.rideshare.repository;

import com.rideshare.domain.model.Driver;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByPhoneNumber(String phoneNumber);
    Optional<Driver> findByVehicleNumber(String vehicleNumber);
}