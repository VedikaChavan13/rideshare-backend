package com.rideshare.dto.response;

public class RideResponse {

    private final Long rideId;
    private final Long riderId;
    private final Long driverId;
    private final String status;
    private final String clientRequestId;

    public RideResponse(Long rideId, Long riderId, Long driverId, String status, String clientRequestId) {
        this.rideId = rideId;
        this.riderId = riderId;
        this.driverId = driverId;
        this.status = status;
        this.clientRequestId = clientRequestId;
    }

    public Long getRideId() {
        return rideId;
    }

    public Long getRiderId() {
        return riderId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getStatus() {
        return status;
    }

    public String getClientRequestId() {
        return clientRequestId;
    }
}