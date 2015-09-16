package com.nr.androidutils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast utils
 */
public final class ToastUtils {
    private ToastUtils(){}

    public static void showToastLong(Context context, int resId){
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(Context context, int resId){
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
