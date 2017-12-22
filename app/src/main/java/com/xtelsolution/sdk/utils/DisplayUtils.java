package com.xtelsolution.sdk.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.xtelsolution.MyApplication;

/**
 * Created by vivu on 12/8/17
 * xtel.vn
 */

public class DisplayUtils {

    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = MyApplication.context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.scaledDensity / DisplayMetrics.DENSITY_MEDIUM));
    }

    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = MyApplication.context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.scaledDensity / DisplayMetrics.DENSITY_MEDIUM));
    }


    public static int convertDpToPixels(float dp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static int convertDpToSp(float dp, Context context) {
        int sp = (int) (convertDpToPixels(dp, context) / (float) convertSpToPixels(dp, context));
        return sp;
    }
}
