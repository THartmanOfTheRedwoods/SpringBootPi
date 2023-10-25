package edu.redwoods.cis18.tomcatpi.controllers;

import edu.redwoods.cis18.tomcatpi.models.DeviceColor;
import edu.redwoods.cis18.tomcatpi.models.Device;
// Spring imports
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") // Allow requests from any origin
@RestController
@RequestMapping("/device")
public class DeviceController {

    @PostMapping(value = "/set/color", produces = "application/json")
    public ResponseEntity<Device> setColor(@RequestBody DeviceColor colorRequest) {
        Device d = new Device("PiZero", 18, 255, 99);
        d.setColor(colorRequest);
        return ResponseEntity.ok(d);
    }

    @GetMapping("/rainbow")
    public String rainbow() {
        Device d = new Device("PiZero", 18, 255, 99);
        try {
            d.rainbowColors();
        } catch(Throwable t) {
            return t.getMessage().toString();
        }
        return d.toString();
    }

    @GetMapping("/animations")
    public String animations() {
        Device d = new Device("PiZero", 18, 255, 99);
        try {
            d.animations();
        } catch(Throwable t) {
            return t.getMessage().toString();
        }
        return d.toString();
    }

    @GetMapping("/alloff")
    public String allOff() {
        Device d = new Device("PiZero", 18, 255, 99);
        try {
            d.allOff();
        } catch(Throwable t) {
            return t.getMessage().toString();
        }
        return d.toString();
    }
}
