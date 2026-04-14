package com.rideshare.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ActiveDriverCacheServiceTest {

    @Test
    void shouldGenerateStableZoneKeyFromCoordinates() {
        ActiveDriverCacheService service = new ActiveDriverCacheService(null);

        String key = service.zoneKey(
                BigDecimal.valueOf(40.712776),
                BigDecimal.valueOf(-74.005974)
        );

        assertEquals("zone:40:-74:available_drivers", key);
    }
}