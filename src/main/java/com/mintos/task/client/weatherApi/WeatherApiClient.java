package com.mintos.task.client.weatherApi;

import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.transfer.WeatherDataTO;

public interface WeatherApiClient {

    GenericResponse<WeatherDataTO> getWeatherData(float latitude, float longitude);
}
