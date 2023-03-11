package com.mintos.task.transfer;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WeatherDataTO {

    private LocalDateTime lastUpdated;

    private float temperatureCelsius;

    private float temperatureFahrenheit;

    private boolean isDay;

    private String condition;

    private float windSpeedMiles;

    private float windSpeedKilometers;

    private int windDegree;

    private String windDirection;

    private float pressureMillibars;

    private float pressureInches;

    private float precipitationMillimeters;

    private float precipitationInches;

    private int humidity;

    private int cloud;

    private float feelsLikeCelsius;

    private float feelsLikeFahrenheit;

    private float visibleSatelliteImageryKilometers;

    private float visibleSatelliteImageryMiles;

    private float ultraviolet;

    private float gustMiles;

    private float gustKilometers;
}
