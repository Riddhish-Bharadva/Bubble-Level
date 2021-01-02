package com.rab.bubblelevel;

import java.text.DecimalFormat;

public class OneD_Values {
    private float xValue;
    private float yValue;
    private float zValue;
    private double inclinationInDegrees;

    public float getXValue() {
        return xValue;
    }

    public void setXValue(float xValue) {
        this.xValue = convertFormat(xValue);
    }

    public float getYValue() {
        return yValue;
    }

    public void setYValue(float yValue) {
        this.yValue = convertFormat(yValue);
    }

    public float getZValue() {
        return zValue;
    }

    public void setZValue(float zValue) {
        this.zValue = convertFormat(zValue);
    }

    public double getInclinationInDegrees() {
        return inclinationInDegrees;
    }

    public void setInclinationInDegrees(double inclinationInDegrees) {
        this.inclinationInDegrees = inclinationInDegrees;
    }

    private float convertFormat(float number)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        return Float.parseFloat(df.format(number));
    }
}
