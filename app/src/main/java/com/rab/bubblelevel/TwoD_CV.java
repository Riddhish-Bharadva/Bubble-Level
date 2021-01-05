package com.rab.bubblelevel;

import android.content.Context;
import android.graphics.*;
import android.util.*;
import android.view.*;
import androidx.annotation.*;

public class TwoD_CV extends View {

    // Declaration of required variables starts here.
    private int deviceTotalWidth = 0;
    private int blockWidth = 0;
    private Paint unitBackground, bubbleCircle, directionLine; // These are colors of boxes on board.
    private Rect unitShape; // This is to make blocks on screen square.
    HandleData hd; // This will be used to fetch all data in onDraw method.
    RecordedValues rv; // This object will be used to fetch Magnetic field values.
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
        bubbleCircle.setStyle(Paint.Style.FILL); // This will fill rectangle.
        bubbleCircle.setColor(Color.WHITE); // Setting color to white.
        directionLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        directionLine.setStyle(Paint.Style.FILL); // This will fill rectangle.
        directionLine.setTextSize(30); // Setting text size to 30. This will make line darker as well.
        // Declaration of colors ends.
        blockWidth = deviceTotalWidth/10;
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
        canvas.drawCircle(0,0,Float.parseFloat((blockWidth)+""),bubbleCircle);
        canvas.translate(-Float.parseFloat((blockWidth*i)+""),-Float.parseFloat((blockWidth*j)+""));
        if(rv.isMagneticFieldValues())
        {
            double magX = rv.getMagneticX(), magY = rv.getMagneticY();
            double NorthInDegree = Math.atan2(magY,magX)*270/Math.PI; // We are diving here atan2 value by 270 as our device inclination will always be 90 degree more than actual value.
            Matrix m = new Matrix(); // Giving rotating effect to image.
            m.postRotate(Float.parseFloat(-NorthInDegree+""));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.needle); // Create bitmap.
            bitmap = Bitmap.createScaledBitmap(bitmap,blockWidth*5/4,blockWidth*3/2,false); // Rescaling image.
            bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),m,true); // Passing the rotating effect of matrix here to image.
            canvas.drawBitmap(bitmap,blockWidth*4,blockWidth*21/2,null); // drawing image on canvas.
        }
    }
}
