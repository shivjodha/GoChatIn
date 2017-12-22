package com.gochatin.gochatin;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Created by Dell on 6/15/2017.
 */

class UtilsMiscellaneous {
    public static int getThemeAttributeDimensionSize(Context context, int attr)
    {
        TypedArray a = null;
        try
        {
            a = context.getTheme().obtainStyledAttributes(new int[] { attr });
            return a.getDimensionPixelSize(0, 0);
        }
        finally
        {
            if(a != null)
            {
                a.recycle();
            }
        }
    }
}
