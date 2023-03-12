package com.mintos.task.service;

import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.controller.response.WeatherData;

import java.util.List;

public interface WeatherService {

    GenericResponse<WeatherData> getWeatherData(String ipAddress);

    List<WeatherData> getWeatherDataRecords();
}
