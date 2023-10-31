package edu.redwoods.cis18.tomcatpi.models;

import java.io.Serializable;

public class DeviceResponse implements Serializable {
    private final String deviceName;
    private final String deviceCommand;
    private final String restRoute;

    public DeviceResponse(String deviceName, String deviceCommand, String restRoute) {
        this.deviceName = deviceName;
        this.deviceCommand = deviceCommand;
        this.restRoute = restRoute;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceCommand() {
        return deviceCommand;
    }

    public String getRestRoute() {
        return restRoute;
    }
}
