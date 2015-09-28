package com.nr.androidutils.progressdialog;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Retained fragment acts as a proxy to activity
 * which may be recreated during device configuration
 * changes
 */
public class RetainedTaskFragment extends Fragment {
    private static final String TAG = RetainedTaskFragment.class.getSimpleName();
    private boolean mUiReady = false;

    /**
     * Returns retained monitored fragment if it exists
     * or creates new retained fragment
     * @param act parent activity
     * @return retained fragment
     */
    public static RetainedTaskFragment establishRetainedMonitoredFragment(Activity act){
        Fragment fragment = getRetainedMonitoredFragment(act);
        if(fragment == null){
            return createRetainedMonitoredFragment(act);
        }
        return (RetainedTaskFragment) fragment;
    }

    /**
     * Returns if UI is ready for interaction
     * @return true if UI is ready for interaction;
     * false otherwise
     */
    public boolean isUiReady(){
        return mUiReady;
    }

    @Override
    public void onStart() {
        super.onStart();
        mUiReady = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        mUiReady = false;
    }

    static RetainedTaskFragment getInstance(){
        RetainedTaskFragment fragment = new RetainedTaskFragment();
        return fragment;
    }

    static RetainedTaskFragment createRetainedMonitoredFragment(Activity act){
        RetainedTaskFragment fragment = RetainedTaskFragment.getInstance();
        fragment.setRetainInstance(true);

        FragmentManager fm = act.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(fragment, RetainedTaskFragment.TAG);
        ft.commit();

        return fragment;
    }

    private static RetainedTaskFragment getRetainedMonitoredFragment(Activity act){
        Fragment fragment = act.getFragmentManager().findFragmentByTag(TAG);
        if(fragment == null){
            return null;
        }
        return (RetainedTaskFragment) fragment;
    }
}
