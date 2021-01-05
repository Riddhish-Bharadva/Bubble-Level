package com.rab.bubblelevel;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.hardware.*;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView xTV = findViewById(R.id.xValue);
        TextView yTV = findViewById(R.id.yValue);
        TextView zTV = findViewById(R.id.zValue);
        TextView valueInDegreesTV = findViewById(R.id.valueInDegrees);
        TextView minInDegreesTV = findViewById(R.id.minInDegrees);
        TextView maxInDegreesTV = findViewById(R.id.maxInDegrees);
        final OneD_CV oDCV = findViewById(R.id.OneD_CV);
        final TwoD_CV tDCV = findViewById(R.id.TwoD_CV);
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null)
        {
            sm.registerListener(new SensorEventListener() {
                final HandleData[] hod = new HandleData[1];
                final int[] orientation = {-1};
                double minDegree = Double.MAX_VALUE, maxDegree = Double.MIN_VALUE, minHorizontal = Double.MAX_VALUE, maxHorizontal = Double.MIN_VALUE, minVertical = Double.MAX_VALUE, maxVertical = Double.MIN_VALUE;
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) { }
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float xValue, yValue, zValue;
                    double degreeValue, horizontal, vertical, azimuth = 0;
                    String temp;
                    RecordedValues rv = new RecordedValues();
                    rv.setXValue(event.values[0]);
                    rv.setYValue(event.values[1]);
                    rv.setZValue(event.values[2]);
                    xValue = rv.getXValue();
                    yValue = rv.getYValue();
                    zValue = rv.getZValue();
                    temp = "X value: "+xValue;
                    xTV.setText(temp);
                    temp = "Y value: "+yValue;
                    yTV.setText(temp);
                    temp = "Z value: "+zValue;
                    zTV.setText(temp);
                    if(Math.abs(event.values[2]) < 5.0)
                    {
                        if(tDCV.getVisibility() == View.VISIBLE)
                        {
                            minInDegreesTV.setText("");
                            maxInDegreesTV.setText("");
                            minDegree = Double.MAX_VALUE;
                            maxDegree = Double.MIN_VALUE;
                        }
                        oDCV.setVisibility(View.VISIBLE);
                        tDCV.setVisibility(View.INVISIBLE);
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        degreeValue = Math.toDegrees(Math.atan2(Double.parseDouble(yValue+""),Double.parseDouble(xValue+"")));
                        degreeValue = rv.formatDouble(degreeValue);
                        if(orientation[0] != getResources().getConfiguration().orientation)
                        {
                            orientation[0] = getResources().getConfiguration().orientation;
                            hod[0] = new HandleData();
                            minDegree = Double.MAX_VALUE;
                            maxDegree = Double.MIN_VALUE;
                        }
                        if(orientation[0] == Configuration.ORIENTATION_LANDSCAPE)
                        {
                            if(degreeValue > 0 && degreeValue < 90)
                                azimuth = degreeValue;
                            else if(degreeValue > 0 && degreeValue > 90)
                                azimuth = degreeValue - 180;
                            else if(degreeValue < 0 && degreeValue < -90)
                                azimuth = degreeValue + 180;
                            else
                                azimuth = degreeValue;
                        }
                        else if(orientation[0] == Configuration.ORIENTATION_PORTRAIT)
                        {
                            if(degreeValue > 0)
                                azimuth = degreeValue - 90;
                            else
                                azimuth = 90 + degreeValue;
                        }
                        azimuth = rv.formatDouble(azimuth);
                        if(Math.abs(azimuth) <= 10.0)
                        {
                            rv.setInclinationInDegrees(azimuth);
                            hod[0].recordValues(rv);
                            oDCV.postInvalidate();
                            temp = "Inclination in Degrees: "+Math.abs(rv.getInclinationInDegrees());
                            valueInDegreesTV.setText(temp);
                        }
                        else if(azimuth > 10.0)
                        {
                            rv.setInclinationInDegrees(10.0);
                            hod[0].recordValues(rv);
                            oDCV.postInvalidate();
                        }
                        else
                        {
                            rv.setInclinationInDegrees(-10.0);
                            hod[0].recordValues(rv);
                            oDCV.postInvalidate();
                        }
                        if(azimuth<minDegree)
                        {
                            minDegree = azimuth;
                            temp = "Minimum Inclination in Degree: "+minDegree;
                            minInDegreesTV.setText(temp);
                        }
                        if(azimuth>maxDegree)
                        {
                            maxDegree = azimuth;
                            temp = "Maximum Inclination in Degree: "+maxDegree;
                            maxInDegreesTV.setText(temp);
                        }
                    }
                    else
                    {
                        if(oDCV.getVisibility() == View.VISIBLE)
                        {
                            minHorizontal = Double.MAX_VALUE;
                            maxHorizontal = Double.MIN_VALUE;
                            minVertical = Double.MAX_VALUE;
                            maxVertical = Double.MIN_VALUE;
                        }
                        oDCV.setVisibility(View.INVISIBLE);
                        tDCV.setVisibility(View.VISIBLE);
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        hod[0] = new HandleData();
                        horizontal = Math.toDegrees(Math.atan2(Double.parseDouble(xValue+""),Double.parseDouble(zValue+"")));
                        vertical = Math.toDegrees(Math.atan2(Double.parseDouble(yValue+""),Double.parseDouble(zValue+"")));
                        horizontal = rv.formatDouble(horizontal);
                        vertical = rv.formatDouble(vertical);
                        if(Math.abs(horizontal) <= 10.0 || Math.abs(vertical) <= 10.0)
                        {
                            rv.setHorizontalInclination(horizontal);
                            rv.setVerticalInclination(vertical);
                            hod[0].recordValues(rv);
                            tDCV.postInvalidate();
                            temp = "Vertical Inclination: "+vertical+"\nHorizontal Inclination: "+horizontal;
                            valueInDegreesTV.setText(temp);
                        }
                        if(horizontal<minHorizontal)
                        {
                            minHorizontal = horizontal;
                        }
                        if(horizontal>maxHorizontal)
                        {
                            maxHorizontal = horizontal;
                        }
                        if(vertical<minVertical)
                        {
                            minVertical = vertical;
                        }
                        if(vertical>maxVertical)
                        {
                            maxVertical = vertical;
                        }
                        temp = "Minimum Values:\nHorizontal: "+minHorizontal+"\t\tVertical: "+minVertical;
                        minInDegreesTV.setText(temp);
                        temp = "Maximum Values:\nHorizontal: "+maxHorizontal+"\t\tVertical: "+maxVertical;
                        maxInDegreesTV.setText(temp);
                    }
                }
            },sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            String temp = "Required accelerometer sensor is not detected in your device.";
            xTV.setText(temp);
        }
        if(sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!=null)
        {
            sm.registerListener(new SensorEventListener() {
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) { }

                @Override
                public void onSensorChanged(SensorEvent event) {
                    Runnable r = () -> {
                        RecordedValues rv = new RecordedValues();
                        rv.setMagneticFieldValues(true);
                        rv.setMagneticX(event.values[0]);
                        rv.setMagneticY(event.values[1]);
                    };
                    Thread t = new Thread(r);
                    t.start();
                    try {
                        t.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            String temp = "Required magnetic field sensor is not detected on your device.";
            Toast.makeText( this, temp ,Toast.LENGTH_LONG).show();
        }
    }
}
