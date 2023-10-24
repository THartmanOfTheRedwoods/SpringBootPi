package edu.redwoods.cis18.tomcatpi.models;

import com.diozero.ws281xj.LedDriverInterface;
import com.diozero.ws281xj.PixelAnimations;
import com.diozero.ws281xj.PixelColour;
import com.diozero.ws281xj.rpiws281x.WS281x;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Device {
    @JsonProperty("deviceName")
    private String deviceName;
    @JsonProperty("gpioNum")
    private int gpioNum;
    @JsonProperty("brightness")
    private int brightness;
    @JsonProperty("pixels")
    private int pixels;

    //@TODO Consider adding a property or method that defines/returns the current state
    //    (i.e. an array of pxels and Hex color);

    private LedDriverInterface ledDriver;

    public Device(String deviceName, int gpioNum, int brightness, int pixels) {
        this.deviceName = deviceName;
        this.gpioNum = gpioNum;
        this.brightness = brightness;
        this.pixels = pixels;
        ledDriver = new WS281x(gpioNum, brightness, pixels);
    }

    public void setColor(DeviceColor color) {
        System.out.printf("Setting color to: %d %d %d%nHex: %s%n",
                color.getRed(), color.getGreen(), color.getBlue(), color.getHexString());
        for (int pixel=0; pixel<ledDriver.getNumPixels(); pixel++) {
            ledDriver.setPixelColour(pixel, color.getHexInt());
        }
        ledDriver.render();
        /*
         */
    }

    public void rainbowColors() throws Throwable {
        int[] colors = PixelColour.RAINBOW;

        for (int i=0; i<250; i++) {
            for (int pixel=0; pixel<ledDriver.getNumPixels(); pixel++) {
                ledDriver.setPixelColour(pixel, colors[(i+pixel) % colors.length]);
            }
            ledDriver.render();
            PixelAnimations.delay(50);
        }
    }

    public void animations() throws Throwable {
        PixelAnimations.demo(ledDriver);
    }

    public void allOff() throws Throwable {
        ledDriver.allOff();
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
