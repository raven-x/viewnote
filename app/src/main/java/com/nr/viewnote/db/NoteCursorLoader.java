package com.nr.viewnote.db;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

public class NoteCursorLoader extends CursorLoader {

    public NoteCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return DbAdapter.getInstance(getContext()).getAllDataToShowInListCursor();
    }
}
