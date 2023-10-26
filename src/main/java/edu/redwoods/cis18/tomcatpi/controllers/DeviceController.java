package edu.redwoods.cis18.tomcatpi.controllers;

import edu.redwoods.cis18.tomcatpi.models.DeviceColor;
import edu.redwoods.cis18.tomcatpi.models.Device;
// Spring imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") // Allow requests from any origin
@RestController
@RequestMapping("/device")
public class DeviceController implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private Device d;

    @PostMapping(value = "/set/color", produces = "application/json")
    public ResponseEntity<Device> setColor(@RequestBody DeviceColor colorRequest) {
        logger.debug("DeviceController setColor called.");
        d.setColor(colorRequest);
        return ResponseEntity.ok(d);
    }

    @GetMapping(value = "/rainbow", produces = "application/json")
    public ResponseEntity<Device> rainbow() {
        try {
            d.rainbowColors();
        } catch(Throwable t) {
            d.setState(t.getMessage());
        }
        return ResponseEntity.ok(d);
    }

    @GetMapping(value = "/animations", produces = "application/json")
    public ResponseEntity<Device> animations() {
        try {
            d.animations();
        } catch(Throwable t) {
            d.setState(t.getMessage());
        }
        return ResponseEntity.ok(d);
    }

    @GetMapping(value = "/alloff", produces = "application/json")
    public ResponseEntity<Device> allOff() {
        try {
            d.allOff();
        } catch(Throwable t) {
            d.setState(t.getMessage());
        }
        return ResponseEntity.ok(d);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.debug("Creating device since the application is ready.");
        d = new Device("PiZero", 18, 255, 99);
    }
}
