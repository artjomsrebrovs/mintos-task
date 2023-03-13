package com.mintos.task.controller.impl;

import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.controller.response.WeatherData;
import com.mintos.task.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    @DisplayName("Should return information about current weather conditions for remote address")
    void getWeatherDataRemoteAddress() throws Exception {
        when(weatherService.getWeatherData(anyString())).thenReturn(GenericResponse.<WeatherData>builder().success(true).build());

        mockMvc.perform(get("/weather"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));

        final InOrder inOrder = Mockito.inOrder(weatherService);
        inOrder.verify(weatherService, times(1)).getWeatherData(any(String.class));
        verifyNoMoreInteractions(weatherService);
    }

    @Test
    @DisplayName("Should return information about current weather conditions for given address")
    void getWeatherDataGivenAddress() throws Exception {
        when(weatherService.getWeatherData(anyString())).thenReturn(GenericResponse.<WeatherData>builder().success(true).build());

        mockMvc.perform(get("/weather/8.8.8.8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));

        final InOrder inOrder = Mockito.inOrder(weatherService);
        inOrder.verify(weatherService, times(1)).getWeatherData("8.8.8.8");
        verifyNoMoreInteractions(weatherService);
    }

    @Test
    @DisplayName("Should return two weather data historical records")
    void getWeatherDataRecords() throws Exception {
        when(weatherService.getWeatherDataRecords()).thenReturn(Arrays.asList(
                WeatherData.builder().isDay(true).build(),
                WeatherData.builder().isDay(true).build()
        ));

        mockMvc.perform(get("/weather_records"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].day").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].day").value(true));

        final InOrder inOrder = Mockito.inOrder(weatherService);
        inOrder.verify(weatherService, times(1)).getWeatherDataRecords();
        verifyNoMoreInteractions(weatherService);
    }
}