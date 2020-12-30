package com.rab.bubblelevel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.*;
import android.hardware.*;
import android.util.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    List<Double> oneD = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.TV1);
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null)
        {
            sm.registerListener(new SensorEventListener() {
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) { }
                @Override
                public void onSensorChanged(SensorEvent event) {
                    int orientation = getResources().getConfiguration().orientation;
                    String xValue = event.values[0]+"";
                    String yValue = event.values[1]+"";
                    double degreeValue = Math.toDegrees(Math.atan2(Double.parseDouble(yValue),Double.parseDouble(xValue)));
                    double azimuth = 0;
                    if(orientation == Configuration.ORIENTATION_LANDSCAPE)
                    {
                        if(degreeValue > 0 && degreeValue < 90)
                            azimuth = degreeValue;
                        else if(degreeValue > 0 && degreeValue > 90)
                            azimuth = 180 - degreeValue;
                        else if(degreeValue < 0 && degreeValue > -90)
                            azimuth = Math.abs(degreeValue);
                        else
                            azimuth = 180 + degreeValue;
                    }
                    else
                    {
                        azimuth = degreeValue;
//                        if(degreeValue > 0 && degreeValue < 90)
//                            azimuth = degreeValue;
//                        else if(degreeValue > 0 && degreeValue > 90)
//                            azimuth = 180 - degreeValue;
//                        else if(degreeValue < 0 && degreeValue > -90)
//                            azimuth = Math.abs(degreeValue);
//                        else
//                            azimuth = 180 + degreeValue;
                    }
                    tv.setText("x: "+event.values[0]+"\ny: "+event.values[1]+"\nz: "+event.values[2]+"\nDegrees: "+azimuth);
                }
            }, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            tv.setText("Required sensor not present on your device.");
        }
    }
}
