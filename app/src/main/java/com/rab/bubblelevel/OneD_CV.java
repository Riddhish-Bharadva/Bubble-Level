package com.rab.bubblelevel;

import android.content.Context;
import android.os.Build;
import android.util.*;
import android.view.*;
import androidx.annotation.*;

public class OneD_CV extends View {

    public OneD_CV(Context context) {
        super(context);
    }

    public OneD_CV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OneD_CV(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OneD_CV(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void onMeasure(int oneDWidth, int oneDHeight)
    {

    }
}
