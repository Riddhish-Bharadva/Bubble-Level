package com.rab.bubblelevel;

import android.graphics.*;
import android.util.*;
import android.view.*;
import androidx.annotation.*;
import android.content.Context;

public class OneD_CV extends View {

    // Declaration of required variables starts here.
    private int deviceTotalWidth = 0;
    private int deviceTotalHeight = 0;
    private int blockWidth = 0;
    private Paint unitBackground, bubbleCircle; // These are colors of boxes on board.
    private Rect unitShape; // This is to make blocks on screen square.
    HandleData hd; // This will be used to fetch all data in onDraw method.
    // Declaration of required variables ends here.

    public OneD_CV(Context context) {
        super(context);
    }

    public OneD_CV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OneD_CV(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onMeasure(int oneDWidth, int oneDHeight)
    {
        deviceTotalWidth = MeasureSpec.getSize(oneDWidth); // Getting screen width.
        deviceTotalHeight = MeasureSpec.getSize(oneDHeight); // Getting screen height.
        setMeasuredDimension(oneDWidth,oneDHeight); // Assigning dimensions to Custom View.
        Log.i("Width",deviceTotalWidth+"");
        Log.i("Height",deviceTotalHeight+"");
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
        // Declaration of colors ends.
        blockWidth = deviceTotalWidth/10;
        if(deviceTotalHeight<1000)
            unitShape = new Rect(0, 0, blockWidth*8, blockWidth*3/2); // Creating box using rectangle.
        else
            unitShape = new Rect(0, 0, blockWidth*8, blockWidth*3); // Creating box using rectangle.
        hd = new HandleData();
    }

    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.translate(blockWidth,Float.parseFloat((deviceTotalHeight/4)+""));
        canvas.drawRect(unitShape,unitBackground);
        canvas.translate(-blockWidth,0);
        double i = 5;
        if(hd.get1DLastData() != null)
        {
            i = i-hd.get1DLastData().getInclinationInDegrees()/2.85;
        }
        canvas.translate(Float.parseFloat((blockWidth*i)+""),0);
        if(blockWidth>125)
        {
            canvas.drawCircle(0,blockWidth*3/4,Float.parseFloat((blockWidth/2)+""),bubbleCircle);
        }
        else
        {
            canvas.drawCircle(0,blockWidth*3/2,Float.parseFloat(blockWidth+""),bubbleCircle);
        }
    }
}
