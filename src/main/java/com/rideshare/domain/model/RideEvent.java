package com.rideshare.domain.model;

import com.rideshare.domain.enums.RideEventType;
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
import java.time.LocalDateTime;

@Entity
@Table(name = "ride_events")
public class RideEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 40)
    private RideEventType eventType;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "event_metadata", length = 1000)
    private String eventMetadata;

    protected RideEvent() {
    }

    public RideEvent(Ride ride, RideEventType eventType, LocalDateTime eventTime, String eventMetadata) {
        this.ride = ride;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.eventMetadata = eventMetadata;
    }

    public Long getId() {
        return id;
    }

    public Ride getRide() {
        return ride;
    }

    public RideEventType getEventType() {
        return eventType;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public String getEventMetadata() {
        return eventMetadata;
    }
}