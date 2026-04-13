package com.rideshare;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * M0 note:
 * The default Spring Initializr test boots the full Spring context.
 * We intentionally disable it until we introduce Testcontainers-based integration testing.
 */
class RideshareApplicationTests {

    @Disabled("M0: enable in later milestone with Testcontainers (MySQL/Redis) + proper test profile")
    @Test
    void contextLoads() {
        // intentionally disabled
    }

    @Test
    void sanity() {
        // a tiny unit test so `mvn test` proves the test pipeline works
        int x = 2 + 2;
        org.junit.jupiter.api.Assertions.assertEquals(4, x);
    }
}