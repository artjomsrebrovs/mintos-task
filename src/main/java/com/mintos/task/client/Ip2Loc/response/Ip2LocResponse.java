package com.mintos.task.client.Ip2Loc.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ip2LocResponse {

    private boolean success;

    private Location location;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {

        private String city;

        private float latitude;

        private float longitude;

        private Country country;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Country {

            private String name;
        }
    }
}
