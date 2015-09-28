package com.nr.androidutils.progressdialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Progress dialog fragment
 */
public class ProgressDialogFragment extends DialogFragment {
    public static final String DIALOG_STYLE_ID = "DIALOG_STYLE";
    public static final String MAX_VALUE_ID = "MAX_VALUE";
    public static final String CANCELABLE_ID = "CANCELABLE_ID";
    public static final String TITLE_ID = "TITLE";
    public static final String MESSAGE_ID = "MESSAGE";

    private ProgressDialog mProgressDialog;
    private IProgressDialogCallback mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int style = getArguments().getInt(DIALOG_STYLE_ID);
        setCancelable(getArguments().getBoolean(CANCELABLE_ID));
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getArguments().getString(TITLE_ID));
        mProgressDialog.setMessage(getArguments().getString(MESSAGE_ID));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(style);
        if(ProgressDialog.STYLE_HORIZONTAL == style){
            mProgressDialog.setMax(getArguments().getInt(MAX_VALUE_ID));
        }
        return mProgressDialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if(mCallback != null){
            mCallback.onDialogCancel();
        }
    }

    void setProgress(int progress){
        mProgressDialog.setProgress(progress);
    }

    void setCallback(IProgressDialogCallback callback) {
        mCallback = callback;
    }
}
