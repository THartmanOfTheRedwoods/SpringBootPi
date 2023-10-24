package edu.redwoods.cis18.tomcatpi.models;

public class DeviceColor {
    private int red;
    private int green;
    private int blue;

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public String getHexString() {
        // Ensure that the RGB values are within the valid range (0-255)
        red = Math.min(255, Math.max(0, red));
        green = Math.min(255, Math.max(0, green));
        blue = Math.min(255, Math.max(0, blue));

        // Convert each RGB component to hexadecimal and concatenate
        String hexRed = Integer.toHexString(red);
        String hexGreen = Integer.toHexString(green);
        String hexBlue = Integer.toHexString(blue);

        // Ensure that each component is represented as a two-digit hexadecimal value
        hexRed = hexRed.length() == 1 ? "0" + hexRed : hexRed;
        hexGreen = hexGreen.length() == 1 ? "0" + hexGreen : hexGreen;
        hexBlue = hexBlue.length() == 1 ? "0" + hexBlue : hexBlue;

        // Concatenate the components to form the hexadecimal RGB value as a string
        return "#" + hexRed + hexGreen + hexBlue;
    }

    public int getHexInt() {
        // Decode it to an int.
        return Integer.decode(this.getHexString());
    }
}
