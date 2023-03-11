package com.mintos.task.controller;

import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.controller.response.WeatherData;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface WeatherController {

    ResponseEntity<GenericResponse<WeatherData>> getWeatherData(HttpServletRequest request);

    ResponseEntity<GenericResponse<List<WeatherData>>> getWeatherDataRecords();
}
