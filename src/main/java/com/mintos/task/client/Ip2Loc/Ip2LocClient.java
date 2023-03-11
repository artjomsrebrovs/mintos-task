package com.mintos.task.client.Ip2Loc;

import com.mintos.task.common.api.GenericResponse;
import com.mintos.task.transfer.LocationDataTO;

public interface Ip2LocClient {

    GenericResponse<LocationDataTO> getLocationData(String ipAddress);
}
