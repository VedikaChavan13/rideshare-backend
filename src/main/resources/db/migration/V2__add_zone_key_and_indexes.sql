ALTER TABLE driver_locations
    ADD COLUMN zone_key VARCHAR(50) NOT NULL DEFAULT 'unknown';

CREATE INDEX idx_driver_locations_status_zone_last_updated
    ON driver_locations (availability_status, zone_key, last_updated_at);

CREATE INDEX idx_driver_locations_driver_zone
    ON driver_locations (driver_id, zone_key);