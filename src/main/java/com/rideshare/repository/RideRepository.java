package com.rideshare.repository;

import com.rideshare.domain.model.Ride;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {
    Optional<Ride> findByClientRequestId(String clientRequestId);
}