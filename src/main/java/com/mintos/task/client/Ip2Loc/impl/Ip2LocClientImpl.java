package com.mintos.task.client.Ip2Loc.impl;

import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.client.Ip2Loc.Ip2LocClient;
import com.mintos.task.client.Ip2Loc.response.Ip2LocResponse;
import com.mintos.task.transfer.LocationDataTO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;

@Log
@Component
public class Ip2LocClientImpl implements Ip2LocClient {

    @Value("${Ip2Loc.api.key}")
    private String Ip2LocApiKey;

    private final RestTemplate restTemplate;

    public Ip2LocClientImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GenericResponse<LocationDataTO> getLocationData(final String ipAddress) {
        final String url = String.format("https://api.ip2loc.com/%s/%s", Ip2LocApiKey, ipAddress);

        try {
            final ResponseEntity<Ip2LocResponse> responseEntity = restTemplate.getForEntity(url, Ip2LocResponse.class);
            final Ip2LocResponse ip2LocResponse = responseEntity.getBody();

            final GenericResponse<LocationDataTO> locationDataResponse;
            if (ip2LocResponse != null && ip2LocResponse.isSuccess()) {
                final LocationDataTO locationDataTO = mapLocationData(ip2LocResponse);
                locationDataResponse = GenericResponse.<LocationDataTO>builder().success(true).response(locationDataTO).build();

            } else {
                final String errorMessage = String.format("Response from Ip2Loc API was not successful: %s", ip2LocResponse);
                log.log(Level.SEVERE, errorMessage);
                locationDataResponse = GenericResponse.<LocationDataTO>builder().errorMessage(errorMessage).build();
            }

            return locationDataResponse;

        } catch (RestClientException | IllegalStateException e) {
            final String errorMessage = String.format("Ip2Loc API client exception: %s", e);
            log.log(Level.SEVERE, errorMessage, e);
            return GenericResponse.<LocationDataTO>builder().errorMessage(errorMessage).build();
        }
    }

    private LocationDataTO mapLocationData(final Ip2LocResponse ip2LocResponse) {
        final Ip2LocResponse.Location location = ip2LocResponse.getLocation();
        return LocationDataTO
                .builder()
                .city(location.getCity())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .country(location.getCountry().getName())
                .build();
    }
}
