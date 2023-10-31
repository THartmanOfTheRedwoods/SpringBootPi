package edu.redwoods.cis18.tomcatpi.models;

import edu.redwoods.cis18.tomcatpi.models.DeviceStatus.Status;

import com.diozero.ws281xj.LedDriverInterface;
import com.diozero.ws281xj.PixelAnimations;
import com.diozero.ws281xj.PixelColour;
import com.diozero.ws281xj.rpiws281x.WS281x;
// import com.fasterxml.jackson.annotation.JsonProperty;
// Logging imports
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LsDevice implements Device {
    private static final Logger logger = LoggerFactory.getLogger(LsDevice.class);

    // TODO: May want to annotate instance fields with @JsonProperty("fieldName") if we ever want to return this
    //  class as Json.
    private String deviceName;
    private final int gpioNum;
    private int brightness;
    private final int pixels;

    private Status status;
    private final List<Observer> observers;

    private final ModelMapper modelMapper = new ModelMapper();

    //@TODO Consider adding a property or method that defines/returns the current status of each pixel
    //    (i.e. an array of pixels and Hex color);

    private final LedDriverInterface ledDriver;

    public LsDevice(String deviceName, int gpioNum, int brightness, int pixels) {
        this.deviceName = deviceName;
        this.gpioNum = gpioNum;
        this.brightness = brightness;
        this.pixels = pixels;
        this.observers = new ArrayList<>();
        ledDriver = getLedDriver();
    }

    public LedDriverInterface getLedDriver() {
        if(ledDriver == null) {
            try {
                return new WS281x(gpioNum, brightness, pixels);
            } catch(java.lang.RuntimeException e) {
                // Catch the error where we don't have a native library for our arch
                logger.error(e.getMessage());
                this.status = Status.TERMINATED;
            }
        }
        return ledDriver;
    }

    public boolean execute(DeviceRequest deviceRequest) {
        notifyObservers(Status.STARTED);
        boolean result =  switch (deviceRequest.getDeviceCommand()) {
            case "animations" -> this.animations();
            case "alloff" -> this.allOff();
            case "gradualrgb" -> this.gradualRgb();
            case "hsbtest" -> this.hsbTest();
            case "hsltest" -> this.hslTest();
            case "rainbow" -> this.rainbowColors();
            case "setcolor" -> this.setColor(
                    modelMapper.map(deviceRequest.getCommandParameters(), DeviceColor.class)
            );
            default -> false;
        };
        if(result) {
            notifyObservers(Status.FINISHED);
            return true;
        }
        notifyObservers(Status.TERMINATED);
        return false;
    }

    private boolean animations() {
        if(ledDriver != null) { // No point in trying to manipulate the physical device if it was never loaded.
            notifyObservers(Status.RUNNING);
            PixelAnimations.demo(ledDriver);
            return true;
        }
        return false;
    }

    private boolean allOff() {
        if(ledDriver != null) { // No point in trying to manipulate the physical device if it was never loaded.
            notifyObservers(Status.RUNNING);
            ledDriver.allOff();
            return true;
        }
        return false;
    }

    private boolean hsbTest() {
        logger.debug("hsbTest()");
        if(ledDriver != null) { // No point in trying to manipulate the physical device if it was never loaded.
            notifyObservers(Status.RUNNING);
            float brightness = 0.5f;
            for (float hue = 0; hue < 1; hue += 0.05f) {
                for (float saturation = 0; saturation <= 1; saturation += 0.05f) {
                    for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
                        ledDriver.setPixelColourHSB(pixel, hue, saturation, brightness);
                    }
                    ledDriver.render();
                    PixelAnimations.delay(20);
                }
            }
            return true;
        }
        return false;
    }

    private boolean hslTest() {
        logger.debug("hslTest()");
        if(ledDriver != null) { // No point in trying to manipulate the physical device if it was never loaded.
            notifyObservers(Status.RUNNING);
            float luminance = 0.5f;
            for (float hue = 0; hue < 360; hue += 18) { //18 from (360 / 20)
                for (float saturation = 0; saturation <= 1; saturation += 0.05f) {
                    for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
                        ledDriver.setPixelColourHSL(pixel, hue, saturation, luminance);
                    }
                    ledDriver.render();
                    PixelAnimations.delay(20);
                }
            }
            return true;
        }
        return false;
    }

    private boolean rainbowColors() {
        if(ledDriver != null) { // No point in trying to manipulate the physical device if it was never loaded.
            notifyObservers(Status.RUNNING);
            int[] colors = PixelColour.RAINBOW;
            for (int i = 0; i < 250; i++) {
                for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
                    ledDriver.setPixelColour(pixel, colors[(i + pixel) % colors.length]);
                }
                ledDriver.render();
                PixelAnimations.delay(50);
            }
            return true;
        }
        return false;
    }

    private boolean setColor(DeviceColor color) {
        logger.debug(String.format("Setting color to: %d %d %d%nHex: %s%n",
                color.getRed(), color.getGreen(), color.getBlue(), color.getHexString())
        );
        if(ledDriver != null) { // No point in trying to manipulate the physical device if it was never loaded.
            notifyObservers(Status.RUNNING);
            for(int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
                ledDriver.setPixelColour(pixel, color.getHexInt());
            }
            ledDriver.render();
            return true;
        }
        return false;
    }

    private boolean gradualRgb() {
        logger.debug("gradualRgb()");
        if(ledDriver != null) { // No point in trying to manipulate the physical device if it was never loaded.
            notifyObservers(Status.RUNNING);
            // Set all off
            ledDriver.allOff();
            int delay = 20;
            // Gradually add red
            for (int i=0; i<256; i+=2) {
                for (int pixel=0; pixel<ledDriver.getNumPixels(); pixel++) {
                    ledDriver.setRedComponent(pixel, i);
                }
                ledDriver.render();
                PixelAnimations.delay(delay);
            }
            // Gradually add green
            for (int i=0; i<256; i+=2) {
                for (int pixel=0; pixel<ledDriver.getNumPixels(); pixel++) {
                    ledDriver.setGreenComponent(pixel, i);
                }
                ledDriver.render();
                PixelAnimations.delay(delay);
            }
            // Gradually add blue
            for (int i=0; i<256; i+=2) {
                for (int pixel=0; pixel<ledDriver.getNumPixels(); pixel++) {
                    ledDriver.setBlueComponent(pixel, i);
                }
                ledDriver.render();
                PixelAnimations.delay(delay);
            }
            // Set all off
            ledDriver.allOff();
            return true;
        }
        return false;
    }

    /* Start Getters and Setters */
    public int getBrightness() {
        return this.brightness;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public int getGpioNum() {
        return this.gpioNum;
    }

    public int getPixels() {
        return this.pixels;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /* Start Observer Pattern Subject methods */
    @Override
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(Status status) {
        this.status = status;
        for(Observer observer : this.observers) {
            observer.update(this);
        }
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
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
}
