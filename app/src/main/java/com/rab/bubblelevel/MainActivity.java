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

        TextView xTV = findViewById(R.id.xValue); // This text view will display either message or X-axis values.
        TextView yTV = findViewById(R.id.yValue); // This text view will display Y-axis values.
        TextView zTV = findViewById(R.id.zValue); // This text view will display Z-axis values.
        TextView valueInDegreesTV = findViewById(R.id.valueInDegrees); // This text view will display inclination in degrees.
        TextView minInDegreesTV = findViewById(R.id.minInDegrees); // This text view will display minimum inclination recorded in degrees.
        TextView maxInDegreesTV = findViewById(R.id.maxInDegrees); // This text view will display maximum inclination recorded in degrees.
        final OneD_CV oDCV = findViewById(R.id.OneD_CV); // This is an object of 1D custom view.
        final TwoD_CV tDCV = findViewById(R.id.TwoD_CV); // This is an object of 2D custom view.
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE); // This is an object of sensor manager. This will be used later to detect sensor values.
        if(sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null) // Checking if accelerometer is detected in device.
        {
            sm.registerListener(new SensorEventListener() { // This is new class to listen sensor value changes for accelerometer.
                final HandleData[] hod = new HandleData[1]; // This is an object of class HandleData. This class will handle 500 values sensed data by sensor.
                final int[] orientation = {-1}; // Orientation is size 1 integer array used to check orientation type of device.
                // Below is declaration of required variables.
                double minDegree = Double.MAX_VALUE, maxDegree = Double.MIN_VALUE, minHorizontal = Double.MAX_VALUE, maxHorizontal = Double.MIN_VALUE, minVertical = Double.MAX_VALUE, maxVertical = Double.MIN_VALUE;
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) { }
                @Override
                public void onSensorChanged(SensorEvent event) { // In case there are changes in sensor data, do as below.
                    // Declaration of required variables starts here.
                    float xValue, yValue, zValue;
                    double degreeValue, horizontal, vertical, azimuth = 0;
                    String temp;
                    RecordedValues rv = new RecordedValues();
                    // Declaration of required variables ends here.
                    rv.setXValue(event.values[0]); // Setting X-axis value in RecordedValues object.
                    rv.setYValue(event.values[1]); // Setting Y-axis value in RecordedValues object.
                    rv.setZValue(event.values[2]); // Setting Z-axis value in RecordedValues object.
                    xValue = rv.getXValue(); // Fetching X-axis value from above object.
                    yValue = rv.getYValue(); // Fetching Y-axis value from above object.
                    zValue = rv.getZValue(); // Fetching Z-axis value from above object.
                    temp = "X value: "+xValue; // Preparing string to display on text view.
                    xTV.setText(temp); // Setting text view value.
                    temp = "Y value: "+yValue; // Preparing string to display on text view.
                    yTV.setText(temp); // Setting text view value.
                    temp = "Z value: "+zValue; // Preparing string to display on text view.
                    zTV.setText(temp); // Setting text view value.
                    if(Math.abs(event.values[2]) < 5.0) // In case phone is not placed on flat surface, do as below.
                    {
                        if(tDCV.getVisibility() == View.VISIBLE) // In case 2D view was visible, do as below.
                        {
                            minInDegreesTV.setText(""); // Reset text view value.
                            maxInDegreesTV.setText(""); // Reset text view value.
                            minDegree = Double.MAX_VALUE; // Reset variable.
                            maxDegree = Double.MIN_VALUE; // Reset variable.
                        }
                        oDCV.setVisibility(View.VISIBLE); // Enable visibility of 1D view.
                        tDCV.setVisibility(View.INVISIBLE); // Disable visibility of 2D view.
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR); // Set orientation sensor to default. This is required as we are disabling this sensor in 2D view.
                        degreeValue = Math.toDegrees(Math.atan2(Double.parseDouble(yValue+""),Double.parseDouble(xValue+""))); // Calculating inclination in degree.
                        degreeValue = rv.formatDouble(degreeValue); // Formating degree values to 2 digits after decimal point.
                        if(orientation[0] != getResources().getConfiguration().orientation) // Checking if current orientation is same as last recorded orientation or not.
                        {
                            orientation[0] = getResources().getConfiguration().orientation; // Set orientation in case it is not as previous one.
                            hod[0] = new HandleData(); // Create new object of Handle Data to reset all values.
                            minDegree = Double.MAX_VALUE; // Reset variable.
                            maxDegree = Double.MIN_VALUE; // Reset variable.
                        }
                        if(orientation[0] == Configuration.ORIENTATION_LANDSCAPE) // In case orientation is landscape, do as below.
                        {
                            if(degreeValue > 0 && degreeValue < 90) // Check if value recorded is in left half of top part of device.
                                azimuth = degreeValue;
                            else if(degreeValue > 0 && degreeValue > 90) // Check if value recorded is in right half of top part of device.
                                azimuth = degreeValue - 180;
                            else if(degreeValue < 0 && degreeValue < -90) // Check if value recorded is in right half of bottom part of device.
                                azimuth = degreeValue + 180;
                            else // If nothing from above is true, recorded value is in left half of bottom part of device.
                                azimuth = degreeValue;
                        }
                        else if(orientation[0] == Configuration.ORIENTATION_PORTRAIT) // In case orientation is portrait, do as below.
                        {
                            if(degreeValue > 0) // Check if recorded value is greater than 0, it is in top half of device.
                                azimuth = degreeValue - 90;
                            else // Else recorded value is in bottom part of device screen.
                                azimuth = 90 + degreeValue;
                        }
                        azimuth = rv.formatDouble(azimuth); // Converting azimuth to 2 digits after decimal point.
                        if(Math.abs(azimuth) <= 10.0) // In case recorded values is in range of [-10,10], do as below.
                        {
                            rv.setInclinationInDegrees(azimuth); // Set inclination in object.
                            hod[0].recordValues(rv); // Record value in HandleData class up to 500 values.
                            oDCV.postInvalidate(); // Refreshing canvas.
                            temp = "Inclination in Degrees: "+Math.abs(rv.getInclinationInDegrees()); // Preparing string to display on text view.
                            valueInDegreesTV.setText(temp); // Setting text view value.
                        }
                        else if(azimuth > 10.0) // In case recorded value is greater than 10 degrees, do as below.
                        {
                            rv.setInclinationInDegrees(10.0); // Set inclination to 10 so that bubble does not go out of our device.
                            hod[0].recordValues(rv); // Record value in HandleData class up to 500 values.
                            oDCV.postInvalidate(); // Refreshing canvas.
                        }
                        else // In case recorded value is less than -10 degrees, do as below.
                        {
                            rv.setInclinationInDegrees(-10.0); // Set inclination to -10 so that bubble does not go out of our device.
                            hod[0].recordValues(rv); // Record value in HandleData class up to 500 values.
                            oDCV.postInvalidate(); // Refreshing canvas.
                        }
                        if(azimuth<minDegree) // In case current azimuth value is less than minimum degree, do as below.
                        {
                            minDegree = azimuth; // Assign value to minimum degree.
                            temp = "Minimum Inclination in Degree: "+minDegree; // Preparing string to display on text view.
                            minInDegreesTV.setText(temp); // Setting text view value.
                        }
                        if(azimuth>maxDegree) // In case current azimuth value is greater than maximum degree, do as below.
                        {
                            maxDegree = azimuth; // Assign value to maximum degree.
                            temp = "Maximum Inclination in Degree: "+maxDegree; // Preparing string to display on text view.
                            maxInDegreesTV.setText(temp); // Setting text view value.
                        }
                    }
                    else // If Z-axis value of recorded values is more than above threshold, do as below.
                    {
                        if(oDCV.getVisibility() == View.VISIBLE) // Checking if visibility of 1D custom view was visible, do as below.
                        {
                            minHorizontal = Double.MAX_VALUE; // Resetting variable.
                            maxHorizontal = Double.MIN_VALUE; // Resetting variable.
                            minVertical = Double.MAX_VALUE; // Resetting variable.
                            maxVertical = Double.MIN_VALUE; // Resetting variable.
                        }
                        oDCV.setVisibility(View.INVISIBLE); // Changing visibility of 1D CV.
                        tDCV.setVisibility(View.VISIBLE); // Changing visibility of 2D CV.
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR); // Blocking orientation sensor to stop sensing orientation.
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Forcing orientation to be portrait mode.
                        hod[0] = new HandleData(); // Resetting object. This will also reset required variables.
                        horizontal = Math.toDegrees(Math.atan2(Double.parseDouble(xValue+""),Double.parseDouble(zValue+""))); // Calculating horizontal azimuth.
                        vertical = Math.toDegrees(Math.atan2(Double.parseDouble(yValue+""),Double.parseDouble(zValue+""))); // Calculating vertical azimuth.
                        horizontal = rv.formatDouble(horizontal); // Restricting value to 2 digits after decimal places.
                        vertical = rv.formatDouble(vertical); // Restricting value to 2 digits after decimal places.
                        if(Math.abs(horizontal) <= 10.0 || Math.abs(vertical) <= 10.0) // If horizontal or vertical values are in range of [-10,10], do as below.
                        {
                            rv.setHorizontalInclination(horizontal); // Setting horizontal value in object.
                            rv.setVerticalInclination(vertical); // Setting vertical value in object.
                            hod[0].recordValues(rv); // Recording value in HandleData class up to 500 records.
                            tDCV.postInvalidate(); // Refreshing canvas.
                            temp = "Vertical Inclination: "+vertical+"\nHorizontal Inclination: "+horizontal; // Preparing string to display on text view.
                            valueInDegreesTV.setText(temp); // Setting text view value.
                        }
                        if(horizontal<minHorizontal) // If minimum recorded value is less than current value, do as below.
                        {
                            minHorizontal = horizontal; // Assign current value to minimum variable.
                        }
                        if(horizontal>maxHorizontal) // If maximum recorded value is greater than current value, do as below.
                        {
                            maxHorizontal = horizontal; // Assign current value to maximum variable.
                        }
                        if(vertical<minVertical) // If minimum recorded value is less than current value, do as below.
                        {
                            minVertical = vertical; // Assign current value to minimum variable.
                        }
                        if(vertical>maxVertical) // If maximum recorded value is greater than current value, do as below.
                        {
                            maxVertical = vertical; // Assign current value to maximum variable.
                        }
                        temp = "Minimum Values:\nHorizontal: "+minHorizontal+"\t\tVertical: "+minVertical; // Preparing string to display on text view.
                        minInDegreesTV.setText(temp); // Setting text view value.
                        temp = "Maximum Values:\nHorizontal: "+maxHorizontal+"\t\tVertical: "+maxVertical; // Preparing string to display on text view.
                        maxInDegreesTV.setText(temp); // Setting text view value.
                    }
                }
            },sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL); // Sensing values for Accelerometer.
        }
        else // In case no accelerometer is detected, do as below.
        {
            String temp = "Required accelerometer sensor is not detected in your device."; // Preparing string to display on text view.
            xTV.setText(temp); // Setting text view value.
        }
        if(sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!=null) // If Magnetic field sensor is found on device, do as below.
        {
            sm.registerListener(new SensorEventListener() { // Registering sensor listener.
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) { }

                @Override
                public void onSensorChanged(SensorEvent event) { // In case there are changes in sensor values, do as below.
                    Runnable r = () -> { // Running below code in different thread.
                        RecordedValues rv = new RecordedValues(); // Creating object for recordedValues.
                        rv.setMagneticFieldValues(true); // Setting magnetic field values to true. This will identify if North needle is to be shown in 2D canvas or not.
                        rv.setMagneticX(event.values[0]); // Recording X-axis values.
                        rv.setMagneticY(event.values[1]); // Recording Y-axis values.
                    };
                    Thread t = new Thread(r); // Creating new thread.
                    t.start(); // Starting new thread.
                    try {
                        t.join(); // Joining previously started thread to wait till it's execution in complete.
                    } catch (Exception e) {
                        e.printStackTrace(); // Showing exception in case it's thrown.
                    }
                }
            },sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL); // Sensing values for Magnetic field sensor.
        }
        else // In case no magnetic field sensor is detected, do as below.
        {
            String temp = "Required magnetic field sensor is not detected on your device."; // Preparing string to display on text view.
            Toast.makeText( this, temp ,Toast.LENGTH_LONG).show(); // Show toast message on UI.
        }
    }
}
