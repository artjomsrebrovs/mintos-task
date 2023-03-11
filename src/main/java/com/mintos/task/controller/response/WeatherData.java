package com.mintos.task.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WeatherData {

    private LocalDateTime lastUpdated;

    private boolean isDay;

    private String condition;

    private Location location;

    private Temperature temperature;

    private Wind wind;

    private Pressure pressure;

    private Visibility visibility;

    @Data
    @Builder
    public static class Location {

        private String city;

        private float latitude;

        private float longitude;

        private String country;
    }

    @Data
    @Builder
    public static class Temperature {

        private float temperatureCelsius;

        private float temperatureFahrenheit;

        private float feelsLikeCelsius;

        private float feelsLikeFahrenheit;
    }

    @Data
    @Builder
    public static class Wind {

        private float windSpeedMiles;

        private float windSpeedKilometers;

        private int windDegree;

        private String windDirection;

        private float gustMiles;

        private float gustKilometers;

    }

    @Data
    @Builder
    public static class Pressure {

        private float pressureMillibars;

        private float pressureInches;

        private float precipitationMillimeters;

        private float precipitationInches;
    }

    @Data
    @Builder
    public static class Visibility {

        private int humidity;

        private int cloud;

        private float visibleSatelliteImageryKilometers;

        private float visibleSatelliteImageryMiles;

        private float ultraviolet;
    }
}
