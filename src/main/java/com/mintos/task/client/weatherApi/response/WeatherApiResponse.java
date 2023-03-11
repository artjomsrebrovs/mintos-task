package com.mintos.task.client.weatherApi.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WeatherApiResponse {

    private Current current;

    @Data
    public static class Current {

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime last_updated;

        private float temp_c;

        private float temp_f;

        private int is_day;

        private Condition condition;

        private float wind_mph;

        private float wind_kph;

        private int wind_degree;

        private String wind_dir;

        private float pressure_mb;

        private float pressure_in;

        private float precip_mm;

        private float precip_in;

        private int humidity;

        private int cloud;

        private float feelslike_c;

        private float feelslike_f;

        private float vis_km;

        private float vis_miles;

        private float uv;

        private float gust_mph;

        private float gust_kph;

        @Data
        public static class Condition {

            private String text;

        }
    }

}
