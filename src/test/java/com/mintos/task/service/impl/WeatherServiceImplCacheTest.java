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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class WeatherServiceImplCacheTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private WeatherService weatherService;

    @MockBean
    private Ip2LocClient ip2LocClient;

    @MockBean
    private WeatherApiClient weatherApiClient;

    @MockBean
    private WeatherDataRepository weatherDataRepository;

    private static final String IP_ADDRESS = "0.0.0.0";
    private static final float LATITUDE = 54;
    private static final float LONGITUDE = 26;

    @Test
    @DisplayName("Should successfully get the location and weather data, save it to database and cache")
    void getWeatherDataSuccessCache() {
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

        final GenericResponse<WeatherData> cachedResult = weatherService.getWeatherData(IP_ADDRESS);
        assertThat("Weather data cached result should not be null", cachedResult, is(notNullValue()));
        assertThat("Weather data result and cached weather data result should be the equal", result, is(cachedResult));
        verifyNoMoreInteractions(ip2LocClient, weatherApiClient, weatherDataRepository);

        final Cache cache = cacheManager.getCache("weather_data");
        if (cache != null) {
            final GenericResponse cachedItem = cache.get(IP_ADDRESS, GenericResponse.class);
            assertThat("Weather data cached result should not be null", cachedItem, is(notNullValue()));
            assertThat("Weather data result and cached weather data result should be the equal", result, is(cachedItem));

        } else {
            fail("Caffeine cache sgould not be null");
        }
    }
}