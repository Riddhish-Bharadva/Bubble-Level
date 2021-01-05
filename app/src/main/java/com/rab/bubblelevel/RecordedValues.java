package com.rab.bubblelevel;

import java.text.DecimalFormat;

public class RecordedValues {
    private float xValue;
    private float yValue;
    private float zValue;
    private double inclinationInDegrees;
    private double horizontalInclination;
    private double verticalInclination;

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

    private float formatFloat(float number)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        return Float.parseFloat(df.format(number));
    }

    private double formatDouble(double number)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(number));
    }
}
