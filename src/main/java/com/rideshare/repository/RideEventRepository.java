package com.rideshare.repository;

import com.rideshare.domain.model.RideEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideEventRepository extends JpaRepository<RideEvent, Long> {
}