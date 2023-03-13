package com.mintos.task.service.impl;

import com.mintos.task.client.Ip2Loc.Ip2LocClient;
import com.mintos.task.client.weatherApi.WeatherApiClient;
import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.controller.response.WeatherData;
import com.mintos.task.entity.WeatherDataDO;
import com.mintos.task.repository.WeatherDataRepository;
import com.mintos.task.service.WeatherService;
import com.mintos.task.transfer.LocationDataTO;
import com.mintos.task.transfer.WeatherDataTO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Service
public class WeatherServiceImpl implements WeatherService {

    private final Ip2LocClient ip2LocClient;

    private final WeatherApiClient weatherApiClient;

    private final WeatherDataRepository weatherDataRepository;

    @Autowired
    public WeatherServiceImpl(final Ip2LocClient ip2LocClient, final WeatherApiClient weatherApiClient, final WeatherDataRepository weatherDataRepository) {
        this.ip2LocClient = ip2LocClient;
        this.weatherApiClient = weatherApiClient;
        this.weatherDataRepository = weatherDataRepository;
    }

    @Override
    @Cacheable(value = "weather_data", key = "#ipAddress")
    public GenericResponse<WeatherData> getWeatherData(final String ipAddress) {
        final GenericResponse<LocationDataTO> locationDataResponse = ip2LocClient.getLocationData(ipAddress);

        final GenericResponse<WeatherData> response;
        final WeatherDataDO weatherDataEntity;

        if (locationDataResponse.isSuccess()) {
            final LocationDataTO locationData = locationDataResponse.getResponse();
            final GenericResponse<WeatherDataTO> weatherDataResponse = weatherApiClient.getWeatherData(locationData.getLatitude(), locationData.getLongitude());

            if (weatherDataResponse.isSuccess()) {
                weatherDataEntity = mapWeatherDataEntity(ipAddress, locationData, weatherDataResponse.getResponse());
                response = prepareWeatherAndLocationDataResponse(ipAddress, weatherDataEntity);

            } else {
                weatherDataEntity = mapWeatherDataEntity(ipAddress, locationData);
                response = prepareLocationDataResponse(ipAddress, weatherDataEntity);
            }

        } else {
            weatherDataEntity = mapWeatherDataEntity(ipAddress);
            response = prepareEmptyDataResponse(ipAddress);
        }

        final Long weatherDataId = weatherDataRepository.save(weatherDataEntity).getId();
        log.info(String.format("Weather data stored successfully for ip address: %s, with id: %s", ipAddress, weatherDataId));

        return response;
    }

    private GenericResponse<WeatherData> prepareWeatherAndLocationDataResponse(final String ipAddress, final WeatherDataDO weatherDataEntity) {
        log.info(String.format("Weather and location data responses were success for ip address: %s", ipAddress));
        final WeatherData weatherData = mapWeatherAndLocationDataResponse(weatherDataEntity);
        return GenericResponse.<WeatherData>builder().success(true).response(weatherData).build();
    }

    private GenericResponse<WeatherData> prepareLocationDataResponse(final String ipAddress, final WeatherDataDO weatherDataEntity) {
        final String errorMessage = String.format("Weather data response was not successful, for ip: %s", ipAddress);
        log.warning(errorMessage);
        final WeatherData weatherData = mapLocationDataResponse(weatherDataEntity);
        return GenericResponse.<WeatherData>builder().errorMessage(errorMessage).response(weatherData).build();
    }

    private GenericResponse<WeatherData> prepareEmptyDataResponse(final String ipAddress) {
        final String errorMessage = String.format("Location data response was not successful, for ip: %s", ipAddress);
        log.warning(errorMessage);
        return GenericResponse.<WeatherData>builder().errorMessage(errorMessage).build();
    }

    @Override
    public List<WeatherData> getWeatherDataRecords() {
        final List<WeatherDataDO> weatherDataEntities = weatherDataRepository.findAll();
        return weatherDataEntities.stream().map(this::mapWeatherAndLocationDataResponse).collect(Collectors.toList());
    }

    private WeatherDataDO mapWeatherDataEntity(final String ipAddress, final LocationDataTO locationData, final WeatherDataTO weatherDataTO) {
        return WeatherDataDO.builder()
                .clientIp(ipAddress)
                .requestDate(LocalDateTime.now())
                .ip2LocRequestSuccess(true)
                .weatherApiRequestSuccess(true)
                .city(locationData.getCity())
                .latitude(locationData.getLatitude())
                .longitude(locationData.getLongitude())
                .country(locationData.getCountry())
                .lastUpdated(weatherDataTO.getLastUpdated())
                .temperatureCelsius(weatherDataTO.getTemperatureCelsius())
                .temperatureFahrenheit(weatherDataTO.getTemperatureFahrenheit())
                .isDay(weatherDataTO.isDay())
                .condition(weatherDataTO.getCondition())
                .windSpeedMiles(weatherDataTO.getWindSpeedMiles())
                .windSpeedKilometers(weatherDataTO.getWindSpeedKilometers())
                .windDegree(weatherDataTO.getWindDegree())
                .windDirection(weatherDataTO.getWindDirection())
                .pressureMillibars(weatherDataTO.getPressureMillibars())
                .pressureInches(weatherDataTO.getPressureInches())
                .precipitationMillimeters(weatherDataTO.getPrecipitationMillimeters())
                .precipitationInches(weatherDataTO.getPrecipitationInches())
                .humidity(weatherDataTO.getHumidity())
                .cloud(weatherDataTO.getCloud())
                .feelsLikeCelsius(weatherDataTO.getFeelsLikeCelsius())
                .feelsLikeFahrenheit(weatherDataTO.getFeelsLikeFahrenheit())
                .visibleSatelliteImageryKilometers(weatherDataTO.getVisibleSatelliteImageryKilometers())
                .visibleSatelliteImageryMiles(weatherDataTO.getVisibleSatelliteImageryMiles())
                .gustMiles(weatherDataTO.getGustMiles())
                .gustKilometers(weatherDataTO.getGustKilometers())
                .build();
    }

    private WeatherDataDO mapWeatherDataEntity(final String ipAddress, final LocationDataTO locationData) {
        return WeatherDataDO.builder()
                .clientIp(ipAddress)
                .requestDate(LocalDateTime.now())
                .ip2LocRequestSuccess(true)
                .weatherApiRequestSuccess(false)
                .city(locationData.getCity())
                .latitude(locationData.getLatitude())
                .longitude(locationData.getLongitude())
                .country(locationData.getCountry())
                .build();
    }

    private WeatherDataDO mapWeatherDataEntity(final String ipAddress) {
        return WeatherDataDO.builder()
                .clientIp(ipAddress)
                .requestDate(LocalDateTime.now())
                .ip2LocRequestSuccess(false)
                .weatherApiRequestSuccess(false)
                .build();
    }

    private WeatherData mapWeatherAndLocationDataResponse(final WeatherDataDO weatherDataEntity) {
        return WeatherData.builder()
                .lastUpdated(weatherDataEntity.getLastUpdated())
                .isDay(weatherDataEntity.isDay())
                .condition(weatherDataEntity.getCondition())
                .location(WeatherData.Location.builder()
                        .city(weatherDataEntity.getCity())
                        .latitude(weatherDataEntity.getLatitude())
                        .longitude(weatherDataEntity.getLongitude())
                        .country(weatherDataEntity.getCountry())
                        .build())
                .temperature(WeatherData.Temperature.builder()
                        .temperatureCelsius(weatherDataEntity.getTemperatureCelsius())
                        .temperatureFahrenheit(weatherDataEntity.getTemperatureFahrenheit())
                        .feelsLikeCelsius(weatherDataEntity.getFeelsLikeCelsius())
                        .feelsLikeFahrenheit(weatherDataEntity.getFeelsLikeFahrenheit())
                        .build())
                .wind(WeatherData.Wind.builder()
                        .windSpeedMiles(weatherDataEntity.getWindSpeedMiles())
                        .windSpeedKilometers(weatherDataEntity.getWindSpeedKilometers())
                        .windDegree(weatherDataEntity.getWindDegree())
                        .windDirection(weatherDataEntity.getWindDirection())
                        .gustMiles(weatherDataEntity.getGustMiles())
                        .gustKilometers(weatherDataEntity.getGustKilometers())
                        .build())
                .pressure(WeatherData.Pressure.builder()
                        .pressureMillibars(weatherDataEntity.getPressureMillibars())
                        .pressureInches(weatherDataEntity.getPressureInches())
                        .precipitationMillimeters(weatherDataEntity.getPrecipitationMillimeters())
                        .precipitationInches(weatherDataEntity.getPrecipitationInches())
                        .build())
                .visibility(WeatherData.Visibility.builder()
                        .humidity(weatherDataEntity.getHumidity())
                        .cloud(weatherDataEntity.getCloud())
                        .visibleSatelliteImageryKilometers(weatherDataEntity.getVisibleSatelliteImageryKilometers())
                        .visibleSatelliteImageryMiles(weatherDataEntity.getVisibleSatelliteImageryMiles())
                        .ultraviolet(weatherDataEntity.getUltraviolet())
                        .build())
                .build();
    }

    private WeatherData mapLocationDataResponse(final WeatherDataDO weatherDataEntity) {
        return WeatherData.builder()
                .location(WeatherData.Location.builder()
                        .city(weatherDataEntity.getCity())
                        .latitude(weatherDataEntity.getLatitude())
                        .longitude(weatherDataEntity.getLongitude())
                        .country(weatherDataEntity.getCountry())
                        .build())
                .build();
    }
}
