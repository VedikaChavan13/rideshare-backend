package com.rideshare.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class RideRequest {

    @NotNull(message = "Rider id is required")
    private Long riderId;

    @NotNull(message = "Pickup latitude is required")
    @DecimalMin(value = "-90.0", inclusive = true, message = "Pickup latitude must be >= -90")
    @DecimalMax(value = "90.0", inclusive = true, message = "Pickup latitude must be <= 90")
    private BigDecimal pickupLatitude;

    @NotNull(message = "Pickup longitude is required")
    @DecimalMin(value = "-180.0", inclusive = true, message = "Pickup longitude must be >= -180")
    @DecimalMax(value = "180.0", inclusive = true, message = "Pickup longitude must be <= 180")
    private BigDecimal pickupLongitude;

    @NotNull(message = "Dropoff latitude is required")
    @DecimalMin(value = "-90.0", inclusive = true, message = "Dropoff latitude must be >= -90")
    @DecimalMax(value = "90.0", inclusive = true, message = "Dropoff latitude must be <= 90")
    private BigDecimal dropoffLatitude;

    @NotNull(message = "Dropoff longitude is required")
    @DecimalMin(value = "-180.0", inclusive = true, message = "Dropoff longitude must be >= -180")
    @DecimalMax(value = "180.0", inclusive = true, message = "Dropoff longitude must be <= 180")
    private BigDecimal dropoffLongitude;

    @NotBlank(message = "Client request id is required")
    private String clientRequestId;

    public RideRequest() {
    }

    public Long getRiderId() {
        return riderId;
    }

    public BigDecimal getPickupLatitude() {
        return pickupLatitude;
    }

    public BigDecimal getPickupLongitude() {
        return pickupLongitude;
    }

    public BigDecimal getDropoffLatitude() {
        return dropoffLatitude;
    }

    public BigDecimal getDropoffLongitude() {
        return dropoffLongitude;
    }

    public String getClientRequestId() {
        return clientRequestId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }

    public void setPickupLatitude(BigDecimal pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public void setPickupLongitude(BigDecimal pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public void setDropoffLatitude(BigDecimal dropoffLatitude) {
        this.dropoffLatitude = dropoffLatitude;
    }

    public void setDropoffLongitude(BigDecimal dropoffLongitude) {
        this.dropoffLongitude = dropoffLongitude;
    }

    public void setClientRequestId(String clientRequestId) {
        this.clientRequestId = clientRequestId;
    }
}