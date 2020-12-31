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
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        final HandleData[] hd = new HandleData[1];
        final int[] orientation = {-1};

        if(sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null)
        {
            sm.registerListener(new SensorEventListener() {
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) { }
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if(orientation[0] != getResources().getConfiguration().orientation)
                    {
                        orientation[0] = getResources().getConfiguration().orientation;
                        hd[0] = new HandleData();
                    }
                    String xValue = event.values[0]+"";
                    String yValue = event.values[1]+"";
                    double degreeValue = Math.toDegrees(Math.atan2(Double.parseDouble(yValue),Double.parseDouble(xValue)));
                    double azimuth;
                    if(orientation[0] == Configuration.ORIENTATION_LANDSCAPE && Math.abs(event.values[2]) < 5.0)
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
                    else if(orientation[0] == Configuration.ORIENTATION_PORTRAIT && Math.abs(event.values[2]) < 5.0)
                    {
                        if(degreeValue > 0)
                            azimuth = degreeValue - 90;
                        else
                            azimuth = 90 + degreeValue;
                    }
                    else
                    {
                        azimuth = degreeValue;
                    }
                    if(Math.abs(azimuth) <= 10.0)
                    {
                        hd[0].record1DValues(azimuth);
                    }
                    String temp = "x: "+event.values[0]+"\ny: "+event.values[1]+"\nz: "+event.values[2]+"\nDegrees: "+hd[0].get1DLastData();
                    tv.setText(temp);
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
