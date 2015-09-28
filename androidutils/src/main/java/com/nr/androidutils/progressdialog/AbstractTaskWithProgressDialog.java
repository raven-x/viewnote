package com.nr.androidutils.progressdialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * This does the job in the background thread
 * notifying progress dialog through a retained fragment
 * that always keeps a valid reference to a parent activity
 */
public abstract class AbstractTaskWithProgressDialog<Params, Result>
        extends AsyncTask<Params, Integer, Result>
        implements IProgressDialogCallback{

    private RetainedTaskFragment mRetainedFragment;
    private boolean mCancelable;
    private int mProgressDialogStyle;
    private int mMaxValue;
    private String mTag;
    private String mTitle;
    private String mMessage;

    /**
     * Creates a new task with spinner style progress dialog
     * @param retainedFragment retained fragment to save activity reference
     * @param tag unique fragment tag
     * @param cancelable whether task cancelable or not
     */
    public AbstractTaskWithProgressDialog(RetainedTaskFragment retainedFragment,
                                          String title, String message,
                                          String tag, boolean cancelable) {
        this(retainedFragment, title, message, ProgressDialog.STYLE_SPINNER, tag, cancelable);
    }

    /**
     * Creates a new task with progress bar style dialog
     * @param retainedFragment retained fragment to save activity reference
     * @param tag unique fragment tag
     * @param cancelable whether task cancelable or not
     * @param maxValue progress bar upper
     */
    public AbstractTaskWithProgressDialog(RetainedTaskFragment retainedFragment,
                                          String title, String message,
                                          String tag, boolean cancelable, int maxValue){
        this(retainedFragment, title, message, ProgressDialog.STYLE_HORIZONTAL, tag, cancelable);
        mMaxValue = maxValue;
    }

    /**
     * Base constructor
     * @param retainedFragment retained fragment to save activity reference
     * @param title progress dialog title
     * @param message progress dialog message
     * @param progressDialogStyle progress dialog style
     * @param tag unique fragment tag
     * @param cancelable whether task cancelable or not
     */
    private AbstractTaskWithProgressDialog(RetainedTaskFragment retainedFragment,
                                           String title, String message,
                                           int progressDialogStyle, String tag,
                                           boolean cancelable) {
        mRetainedFragment = retainedFragment;
        mTitle = title;
        mMessage = message;
        mProgressDialogStyle = progressDialogStyle;
        mTag = tag;
        mCancelable = cancelable;
    }

    @Override
    public void onDialogCancel() {
        cancel(false);
    }

    @Override
    protected void onPreExecute() {
        showDialogFragment();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if(mRetainedFragment.isUiReady()){
            setProgressOnProgressDialog(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Result integer) {
        //Conditions to take into account
        //1. activity not there
        //2. Activity is there but stopped
        //3. Activity is UI ready
        if(mRetainedFragment.isUiReady()){
            closeProgressDialog();
        }
    }

    /**
     * Sets the current progress value
     * @param progress current progress value
     */
    protected void setProgressOnProgressDialog(int progress){
        ProgressDialogFragment pdf = getDialog();
        if(pdf != null){
            pdf.setProgress(progress);
        }
    }

    /**
     * Shows progress dialog
     */
    private void showDialogFragment(){
        Activity act = mRetainedFragment.getActivity();
        ProgressDialogFragment pdf = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ProgressDialogFragment.DIALOG_STYLE_ID, mProgressDialogStyle);
        bundle.putInt(ProgressDialogFragment.MAX_VALUE_ID, mMaxValue);
        bundle.putBoolean(ProgressDialogFragment.CANCELABLE_ID, mCancelable);
        bundle.putString(ProgressDialogFragment.TITLE_ID, mTitle);
        bundle.putString(ProgressDialogFragment.MESSAGE_ID, mMessage);
        pdf.setArguments(bundle);
        pdf.setCallback(this);
        pdf.show(act.getFragmentManager(), mTag);
    }

    /**
     * Returns progress dialog if it exists
     * @return progress dialog object
     */
    private ProgressDialogFragment getDialog(){
        Activity act = mRetainedFragment.getActivity();
        if(act == null){
            return null;
        }
        return (ProgressDialogFragment) act
                .getFragmentManager()
                .findFragmentByTag(mTag);
    }

    /**
     * Closes progress dialog
     */
    private void closeProgressDialog(){
        DialogFragment dialogFragment = getDialog();
        if(dialogFragment != null){
            dialogFragment.dismiss();
        }
    }
}
