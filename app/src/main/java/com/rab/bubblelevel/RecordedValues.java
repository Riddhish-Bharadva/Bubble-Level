package com.rab.bubblelevel;

import java.text.DecimalFormat;

public class RecordedValues {
    private float xValue;
    private float yValue;
    private float zValue;
    private double inclinationInDegrees;
    private double horizontalInclination;
    private double verticalInclination;
    private static boolean magneticFieldValues = false;
    private static double magneticX;
    private static double magneticY;
    private static double magneticZ;

    public float getXValue() {
        return xValue;
    }

    public void setXValue(float xValue) {
        this.xValue = formatFloat(xValue);
    }

    public float getYValue() {
        return yValue;
    }

    public void setYValue(float yValue) {
        this.yValue = formatFloat(yValue);
    }

    public float getZValue() {
        return zValue;
    }

    public void setZValue(float zValue) {
        this.zValue = formatFloat(zValue);
    }

    public double getInclinationInDegrees() {
        return inclinationInDegrees;
    }

    public void setInclinationInDegrees(double inclinationInDegrees) {
        this.inclinationInDegrees = formatDouble(inclinationInDegrees);
    }

    public double getHorizontalInclination() {
        return horizontalInclination;
    }

    public void setHorizontalInclination(double horizontalInclination) {
        this.horizontalInclination = formatDouble(horizontalInclination);
    }

    public double getVerticalInclination() {
        return verticalInclination;
    }

    public void setVerticalInclination(double verticalInclination) {
        this.verticalInclination = formatDouble(verticalInclination);
    }

    public double getMagneticX() {
        return magneticX;
    }

    public void setMagneticX(double magneticX) {
        RecordedValues.magneticX = formatDouble(magneticX);
    }

    public double getMagneticY() {
        return magneticY;
    }

    public void setMagneticY(double magneticY) {
        RecordedValues.magneticY = formatDouble(magneticY);
    }

    public double getMagneticZ() {
        return magneticZ;
    }

    public void setMagneticZ(double magneticZ) {
        RecordedValues.magneticZ = formatDouble(magneticZ);
    }

    public boolean isMagneticFieldValues() {
        return magneticFieldValues;
    }

    public void setMagneticFieldValues(boolean magneticFieldValues) {
        this.magneticFieldValues = magneticFieldValues;
    }

    private float formatFloat(float number)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        return Float.parseFloat(df.format(number));
    }

    protected double formatDouble(double number)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(number));
    }
}
