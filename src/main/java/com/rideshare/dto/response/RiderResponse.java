package com.rideshare.dto.response;

public class RiderResponse {

    private final Long id;
    private final String fullName;
    private final String phoneNumber;

    public RiderResponse(Long id, String fullName, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
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
}