package com.mintos.task.client.Ip2Loc.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ip2LocResponse {

    private boolean success;

    private Location location;

    @Data
    @Builder
    public static class Location {

        private String city;

        private float latitude;

        private float longitude;

        private Country country;

        @Data
        @Builder
        public static class Country {

            private String name;
        }
    }
}
