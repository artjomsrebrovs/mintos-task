package com.mintos.task.controller.impl;

import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.controller.WeatherController;
import com.mintos.task.controller.response.WeatherData;
import com.mintos.task.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class WeatherControllerImpl implements WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherControllerImpl(final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    @GetMapping("/weather")
    public ResponseEntity<GenericResponse<WeatherData>> getWeatherData(final HttpServletRequest request) {
        final String clientIp = request.getRemoteAddr();
        final GenericResponse<WeatherData> weatherData = weatherService.getWeatherData(clientIp);
        return new ResponseEntity<>(weatherData, HttpStatus.OK);
    }

    @Override
    @GetMapping("/weather_records")
    public ResponseEntity<List<WeatherData>> getWeatherDataRecords() {
        return new ResponseEntity<>(weatherService.getWeatherDataRecords(), HttpStatus.OK);
    }
}
