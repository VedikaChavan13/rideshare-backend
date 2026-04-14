ALTER TABLE rides
ADD CONSTRAINT uk_rides_client_request_id UNIQUE (client_request_id);