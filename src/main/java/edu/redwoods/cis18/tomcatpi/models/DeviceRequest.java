package edu.redwoods.cis18.tomcatpi.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class DeviceRequest {
    private final String deviceName;
    private final String deviceCommand;

    private final Map<String, String> commandParameters = new HashMap<>();

    public DeviceRequest(String deviceName, String deviceCommand) {
        this.deviceName = deviceName;
        this.deviceCommand = deviceCommand;
    }

    public String getDeviceCommand() {
        return deviceCommand;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public Map<String, String> getCommandParameters() {
        return commandParameters;
    }

    @JsonAnySetter
    public void addCommandParameters(String name, String value) {
        this.commandParameters.put(name, value);
    }
}
