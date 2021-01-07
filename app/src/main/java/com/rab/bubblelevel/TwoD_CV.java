package com.rab.bubblelevel;

import android.content.Context;
import android.graphics.*;
import android.util.*;
import android.view.*;
import androidx.annotation.*;
import java.text.*;

public class TwoD_CV extends View {

    // Declaration of required variables starts here.
    private int deviceTotalWidth = 0;
    private int blockWidth = 0;
    private Paint unitBackground, bubbleCircle, directionLine; // These are colors of boxes on board.
    private Rect unitShape; // This is to make blocks on screen square.
    private HandleData hd; // This will be used to fetch all data in onDraw method.
    private RecordedValues rv; // This object will be used to fetch Magnetic field values.
    // Declaration of required variables ends here.

    public TwoD_CV(Context context) {
        super(context);
    }

    public TwoD_CV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoD_CV(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onMeasure(int oneDWidth, int oneDHeight)
    {
        deviceTotalWidth = MeasureSpec.getSize(oneDWidth); // Getting screen width.
        setMeasuredDimension(oneDWidth,oneDHeight); // Assigning dimensions to Custom View.
        init();
    }

    private void init()
    {
        // Declaration of colors start.
        unitBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        unitBackground.setStyle(Paint.Style.FILL); // This will fill rectangle.
        unitBackground.setColor(Color.rgb(45, 114, 178)); // Setting color to blue.
        bubbleCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        bubbleCircle.setStyle(Paint.Style.FILL); // This will fill bubble.
        bubbleCircle.setColor(Color.WHITE); // Setting color to white.
        directionLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        directionLine.setStyle(Paint.Style.FILL); // This will fill direction line.
        directionLine.setColor(Color.RED); // Setting color to white.
        directionLine.setStrokeWidth(5); // Setting stroke width to 5 so that direction line will be thick enough to be visible on screen.
        // Declaration of colors ends.
        blockWidth = deviceTotalWidth/10; // Calculating block width based on device total width.
        directionLine.setTextSize((float)blockWidth/3); // Changing font size based on block width.
        unitShape = new Rect(0, 0, blockWidth*8, blockWidth*8); // Creating box using rectangle.
        hd = new HandleData();
        rv = new RecordedValues();
    }

    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.translate(blockWidth,blockWidth*2);
        canvas.drawRect(unitShape,unitBackground);
        canvas.translate(-blockWidth,-blockWidth*2);
        double i = 5, j = 6; // These are initial positions on x and y axis.
        if(hd.getLastData() != null)
        {
            i = i+hd.getLastData().getHorizontalInclination()/3.3;
            j = j-hd.getLastData().getVerticalInclination()/3.3;
        }
        if(i>8.0)
        {
            i = 8.0;
        }
        else if(i<2.0)
        {
            i = 2.0;
        }
        if(j>9.0)
        {
            j = 9.0;
        }
        else if(j<3.0)
        {
            j = 3.0;
        }
        canvas.translate(Float.parseFloat((blockWidth*i)+""),Float.parseFloat((blockWidth*j)+""));
        canvas.drawCircle(0,0,(float)blockWidth,bubbleCircle);
        canvas.translate(-Float.parseFloat((blockWidth*i)+""),-Float.parseFloat((blockWidth*j)+""));
        if(rv.isMagneticFieldValues())
        {
            float[] points = plotNorth();
            canvas.drawLine(points[0],points[1],points[2],points[3],directionLine);
            canvas.drawCircle(points[0],points[1],30,bubbleCircle);
            if(points[0]<0)
            {
                points[0] = points[0]+13;
            }
            else if(points[0]>0)
            {
                points[0] = points[0]-13;
            }
            if(points[1]<0)
            {
                points[1] = points[1]-13;
            }
            else if(points[1]>0)
            {
                points[1] = points[1]+13;
            }
            canvas.drawText("N",points[0],points[1],directionLine);
        }
    }

    private float[] plotNorth()
    {
        double magX = rv.getMagneticX(), magY = rv.getMagneticY();
        double NorthInDegree = Math.atan2(magY,magX)*180/Math.PI-90;
        Matrix m = new Matrix(); // Giving rotating effect to image.
        float[] points = {blockWidth*5,(float)blockWidth*5/2,blockWidth*5,(float)blockWidth*19/2};
        DecimalFormat df = new DecimalFormat("#");
        NorthInDegree = Double.parseDouble(df.format(NorthInDegree));
        m.setRotate((float)-NorthInDegree,blockWidth*5,blockWidth*6);
        m.mapPoints(points);
        return points;
    }
}
