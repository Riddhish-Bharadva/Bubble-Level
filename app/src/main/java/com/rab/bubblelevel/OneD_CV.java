package com.rab.bubblelevel;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import androidx.annotation.*;

public class OneD_CV extends View {

    // Declaration of required variables starts here.
    private int deviceTotalWidth = 0;
    private int deviceTotalHeight = 0;
    private int blockWidth = 0;
    private Paint border, unitBackground, bubbleCircle; // These are colors of boxes on board.
    private Rect unitShape; // This is to make blocks on screen square.
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
        border = new Paint(Paint.ANTI_ALIAS_FLAG);
        border.setStyle(Paint.Style.STROKE); // This will fill rectangle.
        border.setColor(Color.BLACK); // Setting border color to black.
        unitBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        unitBackground.setStyle(Paint.Style.FILL); // This will fill rectangle.
        unitBackground.setColor(Color.rgb(45, 114, 178)); // Setting color to blue.
        bubbleCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        bubbleCircle.setStyle(Paint.Style.FILL); // This will fill rectangle.
        bubbleCircle.setColor(Color.WHITE); // Setting color to white.
        // Declaration of colors ends.
        blockWidth = deviceTotalWidth/10;
        if(deviceTotalHeight<1000)
            unitShape = new Rect(0, 0, blockWidth*8, deviceTotalHeight/2); // Creating box using rectangle.
        else
            unitShape = new Rect(0, 0, blockWidth*8, deviceTotalHeight/6); // Creating box using rectangle.
    }

    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.translate(blockWidth,deviceTotalHeight/4);
        canvas.drawRect(unitShape,unitBackground);
        if(blockWidth>125)
            canvas.drawCircle(blockWidth*4,deviceTotalHeight/4,blockWidth/2,bubbleCircle);
        else
            canvas.drawCircle(blockWidth*4,deviceTotalHeight/12,blockWidth,bubbleCircle);
    }
}
