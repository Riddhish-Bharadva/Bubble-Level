package com.rab.bubblelevel;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.hardware.*;
import java.text.*;

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
                final HandleOneD_Data[] hodd = new HandleOneD_Data[1];
                final int[] orientation = {-1};
                double minDegree = Double.MAX_VALUE, maxDegree = Double.MIN_VALUE;
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) { }
                @Override
                public void onSensorChanged(SensorEvent event) {
                    OneD_Values oDV = new OneD_Values();
                    float xValue, yValue, zValue;
                    double degreeValue, azimuth = 0;
                    String temp;
                    oDV.setXValue(event.values[0]);
                    oDV.setYValue(event.values[1]);
                    oDV.setZValue(event.values[2]);
                    xValue = oDV.getXValue();
                    yValue = oDV.getYValue();
                    zValue = oDV.getZValue();
                    if(Math.abs(event.values[2]) < 8.0)
                    {
                        oDCV.setVisibility(View.VISIBLE);
                        tDCV.setVisibility(View.INVISIBLE);
                        degreeValue = Math.toDegrees(Math.atan2(Double.parseDouble(yValue+""),Double.parseDouble(xValue+"")));
                        if(orientation[0] != getResources().getConfiguration().orientation)
                        {
                            orientation[0] = getResources().getConfiguration().orientation;
                            hodd[0] = new HandleOneD_Data();
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
                        DecimalFormat df = new DecimalFormat("#.##");
                        azimuth = Double.parseDouble(df.format(azimuth));
                        if(Math.abs(azimuth) <= 10.0)
                        {
                            oDV.setInclinationInDegrees(azimuth);
                            hodd[0].record1DValues(oDV);
                            oDCV.postInvalidate();
                            temp = "Inclination in Degrees: "+Math.abs(oDV.getInclinationInDegrees());
                            valueInDegreesTV.setText(temp);
                        }
                        else if(azimuth > 10.0)
                        {
                            oDV.setInclinationInDegrees(10.0);
                            hodd[0].record1DValues(oDV);
                            oDCV.postInvalidate();
                        }
                        else
                        {
                            oDV.setInclinationInDegrees(-10.0);
                            hodd[0].record1DValues(oDV);
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
                        if(hodd[0].get1DLastData() != null)
                        {
                            temp = "X-axis value: "+xValue;
                            xTV.setText(temp);
                            temp = "Y-axis value: "+yValue;
                            yTV.setText(temp);
                            temp = "Z-axis value: "+zValue;
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
                    else
                    {
                        oDCV.setVisibility(View.INVISIBLE);
                        tDCV.setVisibility(View.VISIBLE);
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
