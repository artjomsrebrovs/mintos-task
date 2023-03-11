package com.mintos.task.transfer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDataTO {

    private String city;

    private float latitude;

    private float longitude;

    private String country;
}
