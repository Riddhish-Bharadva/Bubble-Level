package com.rab.bubblelevel;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.*;
import android.hardware.*;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.TV1);
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
                        String xValue, yValue;
                        Float zValue;
                        double degreeValue, azimuth = 0;
                        if(orientation[0] != getResources().getConfiguration().orientation)
                        {
                            orientation[0] = getResources().getConfiguration().orientation;
                            hd[0] = new HandleData();
                        }
                        oDV.setXValue(event.values[0]);
                        oDV.setYValue(event.values[1]);
                        oDV.setZValue(event.values[2]);
                        xValue = oDV.getXValue()+"";
                        yValue = oDV.getYValue()+"";
                        zValue = oDV.getZValue();
                        degreeValue = Math.toDegrees(Math.atan2(Double.parseDouble(yValue),Double.parseDouble(xValue)));
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
                        }
                        String temp;
                        if(hd[0].get1DLastData() != null)
                        {
                            temp = "x: "+xValue+"\ny: "+yValue+"\nz: "+zValue+"\nDegrees: "+hd[0].get1DLastData().getInclinationInDegrees();
                        }
                        else
                        {
                            temp = "No Data recorded.";
                        }
                        tv.setText(temp);
                    }
                }
            }, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            String temp = "Required sensor not present on your device.";
            tv.setText(temp);
        }
    }
}
