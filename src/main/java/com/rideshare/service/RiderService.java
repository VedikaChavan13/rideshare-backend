package com.rideshare.service;

import com.rideshare.domain.model.Rider;
import com.rideshare.dto.request.RiderRegistrationRequest;
import com.rideshare.dto.response.RiderResponse;
import com.rideshare.repository.RiderRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    @Transactional
    public RiderResponse registerRider(RiderRegistrationRequest request) {
        Rider rider = new Rider(
                request.getFullName(),
                request.getPhoneNumber(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Rider savedRider = riderRepository.save(rider);

        return new RiderResponse(
                savedRider.getId(),
                savedRider.getFullName(),
                savedRider.getPhoneNumber()
        );
    }
}