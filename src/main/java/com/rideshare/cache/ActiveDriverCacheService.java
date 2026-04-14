package com.rideshare.cache;

import com.rideshare.domain.enums.DriverAvailabilityStatus;
import com.rideshare.domain.model.DriverLocation;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ActiveDriverCacheService {

    private static final Duration ACTIVE_DRIVER_TTL = Duration.ofSeconds(60);

    private final StringRedisTemplate stringRedisTemplate;

    public ActiveDriverCacheService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String zoneKey(BigDecimal latitude, BigDecimal longitude) {
        int latBucket = latitude.intValue();
        int lonBucket = longitude.intValue();
        return "zone:" + latBucket + ":" + lonBucket + ":available_drivers";
    }

    public void syncDriverLocation(DriverLocation driverLocation) {
        Long driverId = driverLocation.getDriver().getId();
        if (driverId == null) {
            return;
        }

        String key = zoneKey(driverLocation.getLatitude(), driverLocation.getLongitude());

        if (driverLocation.getAvailabilityStatus() == DriverAvailabilityStatus.AVAILABLE) {
            stringRedisTemplate.opsForSet().add(key, String.valueOf(driverId));
            stringRedisTemplate.expire(key, ACTIVE_DRIVER_TTL);
        } else {
            stringRedisTemplate.opsForSet().remove(key, String.valueOf(driverId));
        }
    }

    public Set<Long> getActiveDriverIdsForZone(BigDecimal latitude, BigDecimal longitude) {
        String key = zoneKey(latitude, longitude);
        Set<String> members = stringRedisTemplate.opsForSet().members(key);

        if (members == null || members.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Long> driverIds = new HashSet<>();
        for (String member : members) {
            driverIds.add(Long.valueOf(member));
        }
        return driverIds;
    }

    public void populateZone(BigDecimal latitude, BigDecimal longitude, Set<Long> driverIds) {
        String key = zoneKey(latitude, longitude);

        if (driverIds.isEmpty()) {
            return;
        }

        for (Long driverId : driverIds) {
            stringRedisTemplate.opsForSet().add(key, String.valueOf(driverId));
        }
        stringRedisTemplate.expire(key, ACTIVE_DRIVER_TTL);
    }
}