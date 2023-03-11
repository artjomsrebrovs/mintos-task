package com.mintos.task.client.Ip2Loc.response;

import lombok.Data;

@Data
public class Ip2LocResponse {

    private boolean success;

    private Location location;

    @Data
    public static class Location {

        private String city;

        private float latitude;

        private float longitude;

        private Country country;

        @Data
        public static class Country {

            private String name;
        }
    }
}
