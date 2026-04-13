package com.rideshare.domain.model;

import com.rideshare.domain.enums.RideStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rider_id", nullable = false)
    private Rider rider;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "pickup_latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal pickupLatitude;

    @Column(name = "pickup_longitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal pickupLongitude;

    @Column(name = "dropoff_latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal dropoffLatitude;

    @Column(name = "dropoff_longitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal dropoffLongitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RideStatus status;

    @Column(name = "client_request_id", nullable = false, unique = true, length = 64)
    private String clientRequestId;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    protected Ride() {
    }

    public Ride(
            Rider rider,
            Driver driver,
            BigDecimal pickupLatitude,
            BigDecimal pickupLongitude,
            BigDecimal dropoffLatitude,
            BigDecimal dropoffLongitude,
            RideStatus status,
            String clientRequestId,
            LocalDateTime requestedAt
    ) {
        this.rider = rider;
        this.driver = driver;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.dropoffLatitude = dropoffLatitude;
        this.dropoffLongitude = dropoffLongitude;
        this.status = status;
        this.clientRequestId = clientRequestId;
        this.requestedAt = requestedAt;
    }

    public Long getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
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

    public RideStatus getStatus() {
        return status;
    }

    public String getClientRequestId() {
        return clientRequestId;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public Long getVersion() {
        return version;
    }
}