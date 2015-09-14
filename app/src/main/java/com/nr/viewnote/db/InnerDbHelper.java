package com.nr.viewnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper
 */
public class InnerDbHelper extends SQLiteOpenHelper {

    /**
     * Constructor
     * @param context
     */
    public InnerDbHelper(Context context) {
        super(context, DbConst.DATABASE_NAME, null, DbConst.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConst.Q_CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbConst.Q_DROP_NOTES_TABLE);
        onCreate(db);
    }
}
