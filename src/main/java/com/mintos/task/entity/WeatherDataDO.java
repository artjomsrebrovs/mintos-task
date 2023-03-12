package com.mintos.task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "weather_data")
public class WeatherDataDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_ip",length = 50, nullable = false)
    private String clientIp;

    @Column(name = "req_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "ip2loc_req_success", nullable = false)
    private boolean ip2LocRequestSuccess;

    @Column(name = "weather_api_req_success", nullable = false)
    private boolean weatherApiRequestSuccess;

    @Column(length = 50)
    private String city;

    private float latitude;

    private float longitude;

    @Column(length = 50)
    private String country;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "temp_c")
    private float temperatureCelsius;

    @Column(name = "temp_f")
    private float temperatureFahrenheit;

    @Column(name = "is_day")
    private boolean isDay;

    @Column(length = 50)
    private String condition;

    @Column(name = "wind_speed_miles")
    private float windSpeedMiles;

    @Column(name = "wind_speed_km")
    private float windSpeedKilometers;

    @Column(name = "wind_degree")
    private int windDegree;

    @Column(name = "wind_direction", length = 50)
    private String windDirection;

    @Column(name = "pressure_mb")
    private float pressureMillibars;

    @Column(name = "pressure_in")
    private float pressureInches;

    @Column(name = "precipitation_ml")
    private float precipitationMillimeters;

    @Column(name = "precipitation_in")
    private float precipitationInches;

    @Column(name = "humidity")
    private int humidity;

    @Column(name = "cloud")
    private int cloud;

    @Column(name = "feels_like_c")
    private float feelsLikeCelsius;

    @Column(name = "feels_like_f")
    private float feelsLikeFahrenheit;

    @Column(name = "vis_km")
    private float visibleSatelliteImageryKilometers;

    @Column(name = "vis_miles")
    private float visibleSatelliteImageryMiles;

    private float ultraviolet;

    @Column(name = "gust_miles")
    private float gustMiles;

    @Column(name = "gust_km")
    private float gustKilometers;
}
