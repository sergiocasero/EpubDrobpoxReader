package com.sergiocasero.epubdrobpoxreader.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by sergiocasero on 17/2/16.
 */
public class Util {
    final static public String APP_KEY = "ems6qzx0bzwm2ce";
    final static public String APP_SECRET = "rip1onpfuze41x5\n";

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
}
