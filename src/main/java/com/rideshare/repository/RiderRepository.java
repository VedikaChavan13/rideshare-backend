package com.rideshare.repository;

import com.rideshare.domain.model.Rider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {
    Optional<Rider> findByPhoneNumber(String phoneNumber);
}