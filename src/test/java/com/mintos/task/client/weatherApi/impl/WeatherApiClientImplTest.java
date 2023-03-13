package com.mintos.task.client.weatherApi.impl;

import com.mintos.task.client.weatherApi.response.WeatherApiResponse;
import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.transfer.WeatherDataTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
class WeatherApiClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherApiClientImpl weatherApiClient;

    private static final String WEATHER_API_KEY = "123qwe456";
    private static final float LATITUDE = 54;
    private static final float LONGITUDE = 26;

    @BeforeEach
    void beforeEach() {
        ReflectionTestUtils.setField(weatherApiClient, "weatherApiKey", WEATHER_API_KEY);
    }

    @Test
    @DisplayName("Should return weather data successfully")
    void getWeatherDataSuccess() {
        final ResponseEntity responseEntityMock = mock(ResponseEntity.class);
        when(restTemplate.getForEntity(String.format("https://api.weatherapi.com/v1/current.json?key=%s&q=%s,%s", WEATHER_API_KEY, LATITUDE, LONGITUDE), WeatherApiResponse.class)).thenReturn(responseEntityMock);
        when(responseEntityMock.getBody()).thenReturn(WeatherApiResponse.builder()
                .current(WeatherApiResponse.Current.builder()
                        .last_updated(LocalDateTime.now())
                        .temp_c(18)
                        .temp_f(40)
                        .is_day(1)
                        .condition(WeatherApiResponse.Current.Condition.builder()
                                .text("sunny")
                                .build())
                        .wind_mph(2)
                        .wind_kph(5)
                        .wind_degree(90)
                        .wind_dir("SW")
                        .pressure_mb(900)
                        .pressure_in(10)
                        .precip_mm(10)
                        .precip_in(3)
                        .humidity(80)
                        .cloud(1)
                        .feelslike_c(20)
                        .feelslike_f(45)
                        .vis_km(10)
                        .vis_miles(3)
                        .uv(20)
                        .gust_kph(5)
                        .gust_mph(2)
                        .build())
                .build());

        final GenericResponse<WeatherDataTO> result = weatherApiClient.getWeatherData(LATITUDE, LONGITUDE);
        assertThat("Weather result data should not be null", result, is(notNullValue()));
        assertThat("Weather result success should true", result.isSuccess(), is(true));
        assertThat("Weather result error message should be null", result.getErrorMessage(), is(nullValue()));
        assertThat("Weather result response should not be null", result.getResponse(), is(notNullValue()));

        final WeatherDataTO response = result.getResponse();
        assertThat("Weather result last updated date should not be null", response.getLastUpdated(), is(notNullValue()));
        assertThat("Weather result temperature celsius should be 18", response.getTemperatureCelsius(), is(18.0F));
        assertThat("Weather result temperature fahrenheit should be 40", response.getTemperatureFahrenheit(), is(40.0F));
        assertThat("Weather result day should be true", response.isDay(), is(true));
        assertThat("Weather result condition data should be sunny", response.getCondition(), is("sunny"));
        assertThat("Weather result wind speed miles should be 2", response.getWindSpeedMiles(), is(2.0F));
        assertThat("Weather result wind speed kilometers should be 5", response.getWindSpeedKilometers(), is(5.0F));
        assertThat("Weather result wind degree should be 90", response.getWindDegree(), is(90));
        assertThat("Weather result wind direction should be SW", response.getWindDirection(), is("SW"));
        assertThat("Weather result pressure millibars should be 900", response.getPressureMillibars(), is(900.0F));
        assertThat("Weather result pressure inches should be 10", response.getPressureInches(), is(10.0F));
        assertThat("Weather result precipitation millimeters should be 10", response.getPrecipitationMillimeters(), is(10.0F));
        assertThat("Weather result precipitation inches should be 3", response.getPrecipitationInches(), is(3.0F));
        assertThat("Weather result humidity should be 80", response.getHumidity(), is(80));
        assertThat("Weather result cloud should be 1", response.getCloud(), is(1));
        assertThat("Weather result feels like celsius should be 20", response.getFeelsLikeCelsius(), is(20.0F));
        assertThat("Weather result feels like fahrenheit should be 45", response.getFeelsLikeFahrenheit(), is(45.0F));
        assertThat("Weather result visible satellite imagery kilometers should be 10", response.getVisibleSatelliteImageryKilometers(), is(10.0F));
        assertThat("Weather result visible satellite imagery miles should be 3", response.getVisibleSatelliteImageryMiles(), is(3.0F));
        assertThat("Weather result ultraviolet should be 20", response.getUltraviolet(), is(20.0F));
        assertThat("Weather result gust kilometers should be 5", response.getGustKilometers(), is(5.0F));
        assertThat("Weather result gust miles should be 2", response.getGustMiles(), is(2.0F));
    }

    @Test
    @DisplayName("Should not return weather data successfully")
    void getWeatherDataFail() {
        final ResponseEntity responseEntityMock = mock(ResponseEntity.class);
        when(restTemplate.getForEntity(String.format("https://api.weatherapi.com/v1/current.json?key=%s&q=%s,%s", WEATHER_API_KEY, LATITUDE, LONGITUDE), WeatherApiResponse.class)).thenReturn(responseEntityMock);
        when(responseEntityMock.getBody()).thenReturn(null);

        final GenericResponse<WeatherDataTO> result = weatherApiClient.getWeatherData(LATITUDE, LONGITUDE);
        assertThat("Weather result data should not be null", result, is(notNullValue()));
        assertThat("Weather result success should false", result.isSuccess(), is(false));
        assertThat("Weather result error message should not be null", result.getErrorMessage(), is(notNullValue()));
        assertThat("Weather result response should not null", result.getResponse(), is(nullValue()));
    }

    @Test
    @DisplayName("Should throw RestClientException when trying to fetch weather data")
    void getWeatherDataThrowRestClientException() {
        when(restTemplate.getForEntity(String.format("https://api.weatherapi.com/v1/current.json?key=%s&q=%s,%s", WEATHER_API_KEY, LATITUDE, LONGITUDE), WeatherApiResponse.class)).thenThrow(RestClientException.class);

        final GenericResponse<WeatherDataTO> result = weatherApiClient.getWeatherData(LATITUDE, LONGITUDE);
        assertThat("Weather result data should not be null", result, is(notNullValue()));
        assertThat("Weather result success should false", result.isSuccess(), is(false));
        assertThat("Weather result error message should not be null", result.getErrorMessage(), is(notNullValue()));
        assertThat("Weather result response should not null", result.getResponse(), is(nullValue()));
    }

    @Test
    @DisplayName("Should throw llegalStateException when trying to fetch weather data")
    void getWeatherDataThrowIllegalStateException() {
        when(restTemplate.getForEntity(String.format("https://api.weatherapi.com/v1/current.json?key=%s&q=%s,%s", WEATHER_API_KEY, LATITUDE, LONGITUDE), WeatherApiResponse.class)).thenThrow(IllegalStateException.class);

        final GenericResponse<WeatherDataTO> result = weatherApiClient.getWeatherData(LATITUDE, LONGITUDE);
        assertThat("Weather result data should not be null", result, is(notNullValue()));
        assertThat("Weather result success should false", result.isSuccess(), is(false));
        assertThat("Weather result error message should not be null", result.getErrorMessage(), is(notNullValue()));
        assertThat("Weather result response should not null", result.getResponse(), is(nullValue()));
    }
}