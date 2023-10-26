package edu.redwoods.cis18.tomcatpi.models;

import com.diozero.ws281xj.LedDriverInterface;
import com.diozero.ws281xj.PixelAnimations;
import com.diozero.ws281xj.PixelColour;
import com.diozero.ws281xj.rpiws281x.WS281x;
import com.fasterxml.jackson.annotation.JsonProperty;
// Logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Device {
    private static final Logger logger = LoggerFactory.getLogger(Device.class);
    @JsonProperty("deviceName")
    private String deviceName;
    @JsonProperty("gpioNum")
    private final int gpioNum;
    @JsonProperty("brightness")
    private int brightness;
    @JsonProperty("pixels")
    private final int pixels;

    // @TODO: Make this state part of an ENUM of possible states rather than a string.
    @JsonProperty("state")
    private String state;

    //@TODO Consider adding a property or method that defines/returns the current status of each pixel
    //    (i.e. an array of pixels and Hex color);

    private final LedDriverInterface ledDriver;

    public Device(String deviceName, int gpioNum, int brightness, int pixels) {
        this.deviceName = deviceName;
        this.gpioNum = gpioNum;
        this.brightness = brightness;
        this.pixels = pixels;
        ledDriver = getLedDriver();
    }

    public LedDriverInterface getLedDriver() {
        if(ledDriver == null) {
            try {
                return new WS281x(gpioNum, brightness, pixels);
            } catch(java.lang.RuntimeException e) {
                // Catch the error where we don't have a native library for our arch
                this.state = e.getMessage();
                logger.error(e.getMessage());
            }
        }
        return ledDriver;
    }

    public void setColor(DeviceColor color) {
        logger.debug(String.format("Setting color to: %d %d %d%nHex: %s%n",
                color.getRed(), color.getGreen(), color.getBlue(), color.getHexString())
        );
        if(ledDriver != null) { // No point in trying to manipulate the physical device if it was never loaded.
            for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
                ledDriver.setPixelColour(pixel, color.getHexInt());
            }
            ledDriver.render();
        }
    }

    public void rainbowColors() throws Throwable {
        if(ledDriver != null) { // No point in trying to manipulate the physical device if it was never loaded.
            int[] colors = PixelColour.RAINBOW;
            for (int i = 0; i < 250; i++) {
                for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
                    ledDriver.setPixelColour(pixel, colors[(i + pixel) % colors.length]);
                }
                ledDriver.render();
                PixelAnimations.delay(50);
            }
        }
    }

    public void animations() throws Throwable {
        if(ledDriver != null) { // No point in trying to manipulate the physical device if it was never loaded.
            PixelAnimations.demo(ledDriver);
        }
    }

    public void allOff() throws Throwable {
        if(ledDriver != null) { // No point in trying to manipulate the physical device if it was never loaded.
            ledDriver.allOff();
        }
    }

    @Override
    public String toString() {
        return String.format("""
                Device: {
                  Name: %s
                  GPIO: %d
                  Brightness: %d
                  Pixels: %d
                  Status: success }""", deviceName, gpioNum, brightness, pixels);
    }

    public String getState() {
        // @TODO: Change when I change state
        return state;
    }

    public void setState(String state) {
        // @TODO: Change when I change state
        this.state = state;
    }
}
