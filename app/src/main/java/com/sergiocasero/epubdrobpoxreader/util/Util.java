package com.sergiocasero.epubdrobpoxreader.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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

    public static Bitmap stringToBitMap(String string) {
        byte[] encodeByte = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
    }

    public static String resourceToString(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}
