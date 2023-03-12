package com.mintos.task.client.weatherApi.impl;

import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.client.weatherApi.WeatherApiClient;
import com.mintos.task.client.weatherApi.response.WeatherApiResponse;
import com.mintos.task.transfer.WeatherDataTO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;

@Log
@Component
public class WeatherApiClientImpl implements WeatherApiClient {

    @Value("${weather.api.key}")
    private String weatherApiKey;

    private final RestTemplate restTemplate;

    public WeatherApiClientImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GenericResponse<WeatherDataTO> getWeatherData(final float latitude, final float longitude) {

        final String url = String.format("https://api.weatherapi.com/v1/current.json?key=%s&q=%s,%s", weatherApiKey, latitude, longitude);

        try {
            final ResponseEntity<WeatherApiResponse> responseEntity = restTemplate.getForEntity(url, WeatherApiResponse.class);
            final WeatherApiResponse weatherApiResponse = responseEntity.getBody();

            final GenericResponse<WeatherDataTO> weatherDataResponse;
            if (weatherApiResponse != null) {
                final WeatherDataTO locationDataTO = mapWeatherData(weatherApiResponse);
                weatherDataResponse = GenericResponse.<WeatherDataTO>builder().success(true).response(locationDataTO).build();

            } else {
                final String errorMessage = "Response from Weather API was not successful: response body is null";
                log.log(Level.SEVERE, errorMessage);
                weatherDataResponse = GenericResponse.<WeatherDataTO>builder().errorMessage(errorMessage).build();
            }

            return weatherDataResponse;

        } catch (RestClientException | IllegalStateException e) {
            final String errorMessage = String.format("Weather API client exception: %s", e);
            log.log(Level.SEVERE, errorMessage, e);
            return GenericResponse.<WeatherDataTO>builder().errorMessage(errorMessage).build();
        }
    }

    private WeatherDataTO mapWeatherData(final WeatherApiResponse weatherApiResponse) {
        final WeatherApiResponse.Current weather = weatherApiResponse.getCurrent();

        return WeatherDataTO.builder()
                .lastUpdated(weather.getLast_updated())
                .temperatureCelsius(weather.getTemp_c())
                .temperatureFahrenheit(weather.getTemp_f())
                .isDay(weather.getIs_day() != 0)
                .condition(weather.getCondition().getText())
                .windSpeedMiles(weather.getWind_mph())
                .windSpeedKilometers(weather.getWind_kph())
                .windDegree(weather.getWind_degree())
                .windDirection(weather.getWind_dir())
                .pressureMillibars(weather.getPressure_mb())
                .pressureInches(weather.getPressure_in())
                .precipitationMillimeters(weather.getPrecip_mm())
                .precipitationInches(weather.getPrecip_in())
                .humidity(weather.getHumidity())
                .cloud(weather.getCloud())
                .feelsLikeCelsius(weather.getFeelslike_c())
                .feelsLikeFahrenheit(weather.getFeelslike_f())
                .visibleSatelliteImageryKilometers(weather.getVis_km())
                .visibleSatelliteImageryMiles(weather.getVis_miles())
                .ultraviolet(weather.getUv())
                .gustKilometers(weather.getGust_kph())
                .gustMiles(weather.getGust_mph())
                .build();
    }

}
