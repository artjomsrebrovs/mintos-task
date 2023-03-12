package com.mintos.task.client.Ip2Loc.impl;

import com.mintos.task.client.Ip2Loc.response.Ip2LocResponse;
import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.transfer.LocationDataTO;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
class Ip2LocClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private Ip2LocClientImpl ip2LocClient;

    private static final String IP2LOC_API_KEY = "123qwe456";
    private static final String IP_ADDRESS = "0.0.0.0";

    @BeforeEach
    void beforeEach() {
        ReflectionTestUtils.setField(ip2LocClient, "Ip2LocApiKey", IP2LOC_API_KEY);
    }

    @Test
    @DisplayName("Should return geographical data successfully")
    void getLocationDataSuccess() {
        final ResponseEntity responseEntityMock = mock(ResponseEntity.class);
        when(restTemplate.getForEntity(String.format("https://api.ip2loc.com/%s/%s", IP2LOC_API_KEY, IP_ADDRESS), Ip2LocResponse.class)).thenReturn(responseEntityMock);
        when(responseEntityMock.getBody()).thenReturn(Ip2LocResponse.builder()
                .success(true)
                .location(Ip2LocResponse.Location.builder()
                        .city("Riga")
                        .latitude(54)
                        .longitude(26)
                        .country(Ip2LocResponse.Location.Country.builder()
                                .name("Latvia")
                                .build())
                        .build())
                .build());

        final GenericResponse<LocationDataTO> result = ip2LocClient.getLocationData(IP_ADDRESS);
        assertThat("Location data result should not be null", result, is(notNullValue()));
        assertThat("Location data result success should be true", result.isSuccess(), is(true));
        assertThat("Location data result error message should be null", result.getErrorMessage(), is(nullValue()));
        assertThat("Location data result response should not be null", result.getResponse(), is(notNullValue()));

        final LocationDataTO response = result.getResponse();
        assertThat("Location data response city should be Riga", response.getCity(), is("Riga"));
        assertThat("Location data response latitude should be 54", response.getLatitude(), is(54.0F));
        assertThat("Location data response longitude should be 26", response.getLongitude(), is(26.0F));
        assertThat("Location data response country should be Latvia", response.getCountry(), is("Latvia"));
    }

    @Test
    @DisplayName("Should fail to return geographical data successfully")
    void getLocationDataFail() {
        final ResponseEntity responseEntityMock = mock(ResponseEntity.class);
        when(restTemplate.getForEntity(String.format("https://api.ip2loc.com/%s/%s", IP2LOC_API_KEY, IP_ADDRESS), Ip2LocResponse.class)).thenReturn(responseEntityMock);
        when(responseEntityMock.getBody()).thenReturn(Ip2LocResponse.builder()
                .success(false)
                .build());

        final GenericResponse<LocationDataTO> result = ip2LocClient.getLocationData(IP_ADDRESS);
        assertThat("Location data result should not be null", result, is(notNullValue()));
        assertThat("Location data result success should be false", result.isSuccess(), is(false));
        assertThat("Location data result error message should be not null", result.getErrorMessage(), is(notNullValue()));
        assertThat("Location data result response should be null", result.getResponse(), is(nullValue()));
    }

    @Test
    @DisplayName("Should throw RestClientException when trying to fetch geographical data")
    void getLocationDataThrowRestClientException() {
        when(restTemplate.getForEntity(String.format("https://api.ip2loc.com/%s/%s", IP2LOC_API_KEY, IP_ADDRESS), Ip2LocResponse.class)).thenThrow(RestClientException.class);

        final GenericResponse<LocationDataTO> result = ip2LocClient.getLocationData(IP_ADDRESS);
        assertThat("Location data result should not be null", result, is(notNullValue()));
        assertThat("Location data result success should be false", result.isSuccess(), is(false));
        assertThat("Location data result error message should be not null", result.getErrorMessage(), is(notNullValue()));
        assertThat("Location data result response should be null", result.getResponse(), is(nullValue()));
    }

    @Test
    @DisplayName("Should throw IllegalStateException when trying to fetch geographical data")
    void getLocationDataThrowIllegalStateException() {
        when(restTemplate.getForEntity(String.format("https://api.ip2loc.com/%s/%s", IP2LOC_API_KEY, IP_ADDRESS), Ip2LocResponse.class)).thenThrow(IllegalStateException.class);

        final GenericResponse<LocationDataTO> result = ip2LocClient.getLocationData(IP_ADDRESS);
        assertThat("Location data result should not be null", result, is(notNullValue()));
        assertThat("Location data result success should be false", result.isSuccess(), is(false));
        assertThat("Location data result error message should be not null", result.getErrorMessage(), is(notNullValue()));
        assertThat("Location data result response should be null", result.getResponse(), is(nullValue()));
    }
}