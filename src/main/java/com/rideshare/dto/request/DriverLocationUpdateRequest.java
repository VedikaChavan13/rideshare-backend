package com.rideshare.dto.request;

import com.rideshare.domain.enums.DriverAvailabilityStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DriverLocationUpdateRequest {

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", inclusive = true, message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", inclusive = true, message = "Latitude must be <= 90")
    private BigDecimal latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", inclusive = true, message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", inclusive = true, message = "Longitude must be <= 180")
    private BigDecimal longitude;

    @NotNull(message = "Availability status is required")
    private DriverAvailabilityStatus availabilityStatus;

    public DriverLocationUpdateRequest() {
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public DriverAvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public void setAvailabilityStatus(DriverAvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}