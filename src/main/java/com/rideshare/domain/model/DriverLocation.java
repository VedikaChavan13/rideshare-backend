package com.rideshare.domain.model;

import com.rideshare.domain.enums.DriverAvailabilityStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "driver_locations")
public class DriverLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "driver_id", nullable = false, unique = true)
    private Driver driver;

    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", nullable = false, length = 30)
    private DriverAvailabilityStatus availabilityStatus;

    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    protected DriverLocation() {
    }

    public DriverLocation(
            Driver driver,
            BigDecimal latitude,
            BigDecimal longitude,
            DriverAvailabilityStatus availabilityStatus,
            LocalDateTime lastUpdatedAt
    ) {
        this.driver = driver;
        this.latitude = latitude;
        this.longitude = longitude;
        this.availabilityStatus = availabilityStatus;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Long getId() {
        return id;
    }

    public Driver getDriver() {
        return driver;
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

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public Long getVersion() {
        return version;
    }
}