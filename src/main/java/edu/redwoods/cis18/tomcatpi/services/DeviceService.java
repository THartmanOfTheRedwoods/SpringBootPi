package edu.redwoods.cis18.tomcatpi.services;

import edu.redwoods.cis18.tomcatpi.models.*;
import edu.redwoods.cis18.tomcatpi.models.DeviceStatus.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeviceService implements Observer {

    final static Logger logger = LoggerFactory.getLogger(DeviceService.class);
    private final Map<Device, DeviceStatus> deviceMap = new HashMap<>();

    @Async
    public void process(Device device, DeviceRequest deviceRequest, UriComponentsBuilder b) {
        deviceMap.put(device,
                new DeviceStatus(device.getDeviceName(), deviceRequest.getDeviceCommand(), 0.0f,
                        Status.SUBMITTED, null
                )
        );
        device.execute(deviceRequest); // Executing the request.
    }

    @Override
    public void update(Device device) {
        logger.info("Update from: %s%n  Status: %s" + device.getDeviceName(), device.getStatus());
        DeviceStatus ds = this.deviceMap.get(device);
        ds.setStatus(device.getStatus());
        // TODO calculate this percentage based of device attributes.
        if(device.getStatus() == Status.TERMINATED || device.getStatus() == Status.FINISHED) {
            ds.setPercentageComplete(100.0f);
            ds.setResultUrl("");  // TODO someday provide the results URL with UriComponentsBuilder passed to process.
        } else if(device.getStatus() == Status.STARTED) {
            ds.setPercentageComplete(25.0f);
        } else if(device.getStatus() == Status.RUNNING) {
            ds.setPercentageComplete(50.0f);
        }
    }
}