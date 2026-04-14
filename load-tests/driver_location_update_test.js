import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  vus: 5,
  duration: "20s",
};

export default function () {
  const payload = JSON.stringify({
    latitude: 40.712776,
    longitude: -74.005974,
    availabilityStatus: "AVAILABLE",
  });

  const params = {
    headers: {
      "Content-Type": "application/json",
    },
  };

  const response = http.put(
    "http://localhost:8080/api/v1/drivers/1/location",
    payload,
    params
  );

  if (response.status !== 204) {
    console.log(`status=${response.status} body=${response.body}`);
  }

  check(response, {
    "status is 204 or 409": (r) => r.status === 204 || r.status === 409,
  });

  sleep(1);
}