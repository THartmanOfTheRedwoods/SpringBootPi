package edu.redwoods.cis18.tomcatpi.controllers;

import edu.redwoods.cis18.tomcatpi.models.*;
// Spring imports
import edu.redwoods.cis18.tomcatpi.services.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*") // Allow requests from any origin
@RestController
@RequestMapping("/devices")
public class DeviceController implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    DeviceService deviceService;

    private final Map<String, Device> devices = new HashMap<>();

    @PostMapping(value = "/process", produces = "application/json")
    public ResponseEntity<DeviceResponse> process(@RequestBody DeviceRequest deviceRequest, UriComponentsBuilder b) {
        logger.debug("DeviceController process called.");
        try {
            Device d = this.devices.get(deviceRequest.getDeviceName());
            //String deviceId = UUID.randomUUID().toString();
            UriComponents progressURL = b.path("/device/{id}/status").buildAndExpand(deviceRequest.getDeviceName());
            deviceService.process(d, deviceRequest, b);
            //return ResponseEntity.accepted().location(progressURL.toUri()).build();
            return ResponseEntity.ok(
                    new DeviceResponse(d.getDeviceName(), deviceRequest.getDeviceCommand(), progressURL.toUri().toString())
            );
        } catch(NullPointerException npe) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.debug("Initializing attached devices since the application is ready.");
        // TODO: Add more devices and make this dynamically definable via external file or database.
        this.devices.put("PiZero", new LsDevice("PiZero", 18, 255, 99));
    }
}
