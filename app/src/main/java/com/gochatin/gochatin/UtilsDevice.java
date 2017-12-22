package com.gochatin.gochatin;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Dell on 6/15/2017.
 */

public class UtilsDevice {

    /**
     * Returns the screen width in pixels
     *
     * @param context is the context to get the resources
     * @return the screen width in pixels
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return metrics.widthPixels;
    }
}
