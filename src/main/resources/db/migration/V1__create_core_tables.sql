CREATE TABLE riders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE drivers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    vehicle_number VARCHAR(30) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE driver_locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    driver_id BIGINT NOT NULL UNIQUE,
    latitude DECIMAL(9,6) NOT NULL,
    longitude DECIMAL(9,6) NOT NULL,
    availability_status VARCHAR(30) NOT NULL,
    last_updated_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_driver_locations_driver
        FOREIGN KEY (driver_id) REFERENCES drivers(id)
);

CREATE TABLE rides (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rider_id BIGINT NOT NULL,
    driver_id BIGINT NULL,
    pickup_latitude DECIMAL(9,6) NOT NULL,
    pickup_longitude DECIMAL(9,6) NOT NULL,
    dropoff_latitude DECIMAL(9,6) NOT NULL,
    dropoff_longitude DECIMAL(9,6) NOT NULL,
    status VARCHAR(30) NOT NULL,
    client_request_id VARCHAR(64) NOT NULL,
    requested_at TIMESTAMP NOT NULL,
    accepted_at TIMESTAMP NULL,
    started_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    cancelled_at TIMESTAMP NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_rides_rider
        FOREIGN KEY (rider_id) REFERENCES riders(id),
    CONSTRAINT fk_rides_driver
        FOREIGN KEY (driver_id) REFERENCES drivers(id),
    CONSTRAINT uq_rides_client_request_id UNIQUE (client_request_id)
);

CREATE TABLE ride_events (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ride_id BIGINT NOT NULL,
    event_type VARCHAR(40) NOT NULL,
    event_time TIMESTAMP NOT NULL,
    event_metadata VARCHAR(1000) NULL,
    CONSTRAINT fk_ride_events_ride
        FOREIGN KEY (ride_id) REFERENCES rides(id)
);

CREATE INDEX idx_driver_locations_status_last_updated
    ON driver_locations (availability_status, last_updated_at);

CREATE INDEX idx_rides_rider_status
    ON rides (rider_id, status);

CREATE INDEX idx_rides_driver_status
    ON rides (driver_id, status);

CREATE INDEX idx_ride_events_ride_time
    ON ride_events (ride_id, event_time);