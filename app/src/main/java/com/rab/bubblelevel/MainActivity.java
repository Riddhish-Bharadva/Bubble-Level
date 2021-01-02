package com.rab.bubblelevel;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.*;
import android.hardware.*;

import java.text.DecimalFormat;

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
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        if(sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null)
        {
            sm.registerListener(new SensorEventListener() {
                final HandleData[] hd = new HandleData[1];
                final int[] orientation = {-1};
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) { }
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if(Math.abs(event.values[2]) < 5.0)
                    {
                        OneD_Values oDV = new OneD_Values();
                        float xValue, yValue, zValue;
                        double degreeValue, minDegree = Double.MAX_VALUE, maxDegree = Double.MIN_VALUE, azimuth = 0;
                        String temp;
                        if(orientation[0] != getResources().getConfiguration().orientation)
                        {
                            orientation[0] = getResources().getConfiguration().orientation;
                            hd[0] = new HandleData();
                        }
                        oDV.setXValue(event.values[0]);
                        oDV.setYValue(event.values[1]);
                        oDV.setZValue(event.values[2]);
                        xValue = oDV.getXValue();
                        yValue = oDV.getYValue();
                        zValue = oDV.getZValue();
                        degreeValue = Math.toDegrees(Math.atan2(Double.parseDouble(yValue+""),Double.parseDouble(xValue+"")));
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
                        if(Math.abs(azimuth) <= 10.0)
                        {
                            oDV.setInclinationInDegrees(azimuth);
                            hd[0].record1DValues(oDV);
                            oDCV.postInvalidate();
                            temp = "Inclination in Degrees: "+Math.abs(oDV.getInclinationInDegrees());
                            valueInDegreesTV.setText(temp);
//                            if(Math.abs(azimuth)<Math.abs(minDegree))
//                            {
//                                minDegree = azimuth;
//                                temp = "Minimum Inclination in Degree: "+minDegree;
//                                minInDegreesTV.setText(temp);
//                            }
//                            else if(Math.abs(azimuth)>Math.abs(maxDegree))
//                            {
//                                maxDegree = azimuth;
//                                temp = "Maximum Inclination in Degree: "+maxDegree;
//                                maxInDegreesTV.setText(temp);
//                            }
                        }
                        if(hd[0].get1DLastData() != null)
                        {
                            temp = "x value: "+xValue;
                            xTV.setText(temp);
                            temp = "y value: "+yValue;
                            yTV.setText(temp);
                            temp = "z value: "+zValue;
                            zTV.setText(temp);
                        }
                        else
                        {
                            temp = "No data recorded.";
                            xTV.setText("");
                            yTV.setText("");
                            zTV.setText("");
                            valueInDegreesTV.setText(temp);
                        }
                    }
                }
            }, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            String temp = "Required sensor not present on your device.";
            xTV.setText(temp);
        }
    }
}
