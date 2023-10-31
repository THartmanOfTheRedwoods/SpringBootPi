package edu.redwoods.cis18.tomcatpi.models;

import java.io.Serializable;

public class DeviceStatus implements Serializable {
    private final String deviceName;
    private final String deviceCommand;
    private float percentageComplete;
    private Status status;
    private String resultUrl;

    public DeviceStatus(String deviceName, String deviceCommand, float percentageComplete, Status status, String resultUrl) {
        this.deviceName = deviceName;
        this.deviceCommand = deviceCommand;
        this.percentageComplete = percentageComplete;
        this.status = status;
        this.resultUrl = resultUrl;
    }

    public float getPercentageComplete() {
        return percentageComplete;
    }

    public void setPercentageComplete(float percentageComplete) {
        this.percentageComplete = percentageComplete;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    public String getDeviceCommand() {
        return deviceCommand;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public enum Status {
        SUBMITTED, STARTED, RUNNING, FINISHED, TERMINATED
    }
}