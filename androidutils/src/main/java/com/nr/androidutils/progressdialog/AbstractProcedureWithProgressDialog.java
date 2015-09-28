package com.nr.androidutils.progressdialog;

/**
 * Task with Object in and out parameters
 */
public abstract class AbstractProcedureWithProgressDialog extends AbstractTaskWithProgressDialog<Object, Object> {

    /**
     * Creates a new task with spinner style progress dialog
     * @param retainedFragment retained fragment to save activity reference
     * @param tag unique fragment tag
     * @param cancelable whether task cancelable or not
     */
    public AbstractProcedureWithProgressDialog(RetainedTaskFragment retainedFragment, String title,
                                               String message, String tag, boolean cancelable) {
        super(retainedFragment, title, message, tag, cancelable);
    }

    /**
     * Creates a new task with progress bar style dialog
     * @param retainedFragment retained fragment to save activity reference
     * @param tag unique fragment tag
     * @param cancelable whether task cancelable or not
     * @param maxValue progress bar upper
     */
    public AbstractProcedureWithProgressDialog(RetainedTaskFragment retainedFragment, String title,
                                               String message, String tag, boolean cancelable, int maxValue) {
        super(retainedFragment, title, message, tag, cancelable, maxValue);
    }
}
