package com.nr.androidutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Activity utils
 */
public final class ActivityUtils {
    private ActivityUtils(){}

    /**
     * Goes back to the specific activity if it was already created and clears stack
     * on top of it
     * @param context context to work in
     * @param activityToGoBack activity class to go back to
     */
    public static void goBackTo(Context context, Class<? extends Activity> activityToGoBack){
        Intent intent = new Intent(context, activityToGoBack);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
