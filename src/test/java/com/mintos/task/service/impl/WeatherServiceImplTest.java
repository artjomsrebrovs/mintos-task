package com.mintos.task.service.impl;

import com.mintos.task.client.Ip2Loc.Ip2LocClient;
import com.mintos.task.client.weatherApi.WeatherApiClient;
import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.controller.response.WeatherData;
import com.mintos.task.entity.WeatherDataDO;
import com.mintos.task.repository.WeatherDataRepository;
import com.mintos.task.transfer.LocationDataTO;
import com.mintos.task.transfer.WeatherDataTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class WeatherServiceImplTest {

    @Mock
    private Ip2LocClient ip2LocClient;

    @Mock
    private WeatherApiClient weatherApiClient;

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    private static final String IP_ADDRESS = "0.0.0.0";
    private static final float LATITUDE = 54;
    private static final float LONGITUDE = 26;

    @Test
    @DisplayName("Should successfully get the location and weather data and save it to database")
    void getWeatherDataSuccess() {
        when(ip2LocClient.getLocationData(IP_ADDRESS)).thenReturn(GenericResponse.<LocationDataTO>builder()
                .success(true)
                .response(LocationDataTO.builder()
                        .latitude(LATITUDE)
                        .longitude(LONGITUDE)
                        .build())
                .build());

        when(weatherApiClient.getWeatherData(LATITUDE, LONGITUDE)).thenReturn(GenericResponse.<WeatherDataTO>builder()
                .success(true)
                .response(WeatherDataTO.builder()
                        .isDay(true)
                        .build())
                .build());

        when(weatherDataRepository.save(any(WeatherDataDO.class))).thenReturn(WeatherDataDO.builder()
                .id(1L)
                .build());

        final GenericResponse<WeatherData> result = weatherService.getWeatherData(IP_ADDRESS);
        assertThat("Weather data result should not be null", result, is(notNullValue()));
        assertThat("Weather data result should be success", result.isSuccess(), is(true));
        assertThat("Weather data error should be null", result.getErrorMessage(), is(nullValue()));
        assertThat("Weather data response should not be null", result.getResponse(), is(notNullValue()));

        final WeatherData response = result.getResponse();
        assertThat("Weather data response day property should not true", response.isDay(), is(true));

        final InOrder inOrder = Mockito.inOrder(ip2LocClient, weatherApiClient, weatherDataRepository);
        inOrder.verify(ip2LocClient, times(1)).getLocationData(IP_ADDRESS);
        inOrder.verify(weatherApiClient, times(1)).getWeatherData(LATITUDE, LONGITUDE);
        inOrder.verify(weatherDataRepository, times(1)).save(any(WeatherDataDO.class));
        verifyNoMoreInteractions(ip2LocClient, weatherApiClient, weatherDataRepository);
    }

    @Test
    @DisplayName("Should fail to get weather data but still save the request attempt to database")
    void getWeatherDataWeatherApiFail() {
        when(ip2LocClient.getLocationData(IP_ADDRESS)).thenReturn(GenericResponse.<LocationDataTO>builder()
                .success(true)
                .response(LocationDataTO.builder()
                        .latitude(LATITUDE)
                        .longitude(LONGITUDE)
                        .build())
                .build());

        when(weatherApiClient.getWeatherData(LATITUDE, LONGITUDE)).thenReturn(GenericResponse.<WeatherDataTO>builder()
                .success(false)
                .build());

        when(weatherDataRepository.save(any(WeatherDataDO.class))).thenReturn(WeatherDataDO.builder()
                .id(1L)
                .build());

        final GenericResponse<WeatherData> result = weatherService.getWeatherData(IP_ADDRESS);
        assertThat("Weather data result should not be null", result, is(notNullValue()));
        assertThat("Weather data result should be success", result.isSuccess(), is(false));
        assertThat("Weather data error should be null", result.getErrorMessage(), is(notNullValue()));
        assertThat("Weather data response should not be null", result.getResponse(), is(notNullValue()));

        final WeatherData response = result.getResponse();
        assertThat("Weather data response day property should not true", response.isDay(), is(false));

        final InOrder inOrder = Mockito.inOrder(ip2LocClient, weatherApiClient, weatherDataRepository);
        inOrder.verify(ip2LocClient, times(1)).getLocationData(IP_ADDRESS);
        inOrder.verify(weatherApiClient, times(1)).getWeatherData(LATITUDE, LONGITUDE);
        inOrder.verify(weatherDataRepository, times(1)).save(any(WeatherDataDO.class));
        verifyNoMoreInteractions(ip2LocClient, weatherApiClient, weatherDataRepository);
    }

    @Test
    @DisplayName("Should fail to get location data but still save the request attempt to database")
    void getWeatherDataIp2LocFail() {
        when(ip2LocClient.getLocationData(IP_ADDRESS)).thenReturn(GenericResponse.<LocationDataTO>builder()
                .success(false)
                .response(LocationDataTO.builder()
                        .build())
                .build());

        when(weatherDataRepository.save(any(WeatherDataDO.class))).thenReturn(WeatherDataDO.builder()
                .id(1L)
                .build());

        final GenericResponse<WeatherData> result = weatherService.getWeatherData(IP_ADDRESS);
        assertThat("Weather data result should not be null", result, is(notNullValue()));
        assertThat("Weather data result should be success", result.isSuccess(), is(false));
        assertThat("Weather data error should be null", result.getErrorMessage(), is(notNullValue()));
        assertThat("Weather data response should not be null", result.getResponse(), is(nullValue()));

        final InOrder inOrder = Mockito.inOrder(ip2LocClient, weatherApiClient, weatherDataRepository);
        inOrder.verify(ip2LocClient, times(1)).getLocationData(IP_ADDRESS);
        inOrder.verify(weatherDataRepository, times(1)).save(any(WeatherDataDO.class));
        verifyNoInteractions(weatherApiClient);
        verifyNoMoreInteractions(ip2LocClient, weatherDataRepository);
    }

    @Test
    @DisplayName("Should return two weather data historical records from database")
    void getWeatherDataRecords() {
        when(weatherDataRepository.findAll()).thenReturn(Arrays.asList(
                WeatherDataDO.builder().isDay(true).build(),
                WeatherDataDO.builder().isDay(true).build(),
                WeatherDataDO.builder().isDay(true).build()
        ));

        final List<WeatherData> result = weatherService.getWeatherDataRecords();
        assertThat("Weather data records result should not be null", result, is(notNullValue()));
        assertThat("Weather data records result size should be", result.size(), is(3));
        assertThat("Weather data records result [0] day should be true", result.get(0).isDay(), is(true));
        assertThat("Weather data records result [1] day should be true", result.get(1).isDay(), is(true));
        assertThat("Weather data records result [2] day should be true", result.get(2).isDay(), is(true));

        final InOrder inOrder = Mockito.inOrder(ip2LocClient, weatherApiClient, weatherDataRepository);
        inOrder.verify(weatherDataRepository, times(1)).findAll();
        verifyNoInteractions(ip2LocClient, weatherApiClient);
        verifyNoMoreInteractions(weatherDataRepository);
    }
}