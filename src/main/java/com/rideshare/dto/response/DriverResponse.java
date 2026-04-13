package com.rideshare.dto.response;

public class DriverResponse {

    private final Long id;
    private final String fullName;
    private final String phoneNumber;
    private final String vehicleNumber;

    public DriverResponse(Long id, String fullName, String phoneNumber, String vehicleNumber) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.vehicleNumber = vehicleNumber;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }
}