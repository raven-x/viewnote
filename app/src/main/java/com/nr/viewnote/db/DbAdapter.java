package com.nr.viewnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Convenient database adapter class
 */
public class DbAdapter {
    private final Context mContext;
    private InnerDbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static DbAdapter mSelf;
    private static boolean isOpened;

    public static synchronized DbAdapter getInstance(Context context){
        if(mSelf == null){
            mSelf = new DbAdapter(context);
        }
        if(!isOpened){
            mSelf.open();
        }
        return mSelf;
    }

    /**
     * Constructor
     * @param context context to be initialized with
     */
    private DbAdapter(Context context) {
        mContext = context;
    }

    /**
     * Opens a connection
     * @return database adapter
     */
    private DbAdapter open(){
        mDbHelper = new InnerDbHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        isOpened = true;
        return this;
    }

    /**
     * Closes current connection
     */
    public synchronized void close(){
        mDbHelper.close();
        isOpened = false;
    }

    /**
     * Adds a new entry to database
     * @param image
     * @return
     */
    public synchronized long addEntry(byte[] image, byte[] thumb){
        return addEntry(image, thumb, "");
    }

    /**
     * Adds a new entry to database
     * @param image image data
     * @param thumb thumb data
     * @param text text data
     */
    public synchronized long addEntry(byte[] image, byte[] thumb, String text){
        ContentValues cv = new ContentValues();
        cv.put(DbConst.COLUMN_PICTURE, image);
        cv.put(DbConst.COLUMN_THUMBNAIL, thumb);
        cv.put(DbConst.COLUMN_TEXT, text);
        cv.put(DbConst.COLUMN_DATE, System.currentTimeMillis());
        return mDb.insert(DbConst.TABLE_NOTES, null, cv);
    }

    public synchronized long removeEntry(NoteEntity entity){
        return mDb.delete(DbConst.TABLE_NOTES,
                String.format(DbConst.EQUALS_EXPRESSION, DbConst.COLUMN_ID),
                new String[]{Integer.toString(entity.getId())});
    }

    public synchronized long removeEntries(Collection<NoteEntity> entities){
        StringBuilder ids = new StringBuilder("(");
        for(NoteEntity entity : entities){
            ids.append(entity.getId()).append(",");
        }
        ids.setLength(ids.length() - 1);
        ids.append(")");
        return mDb.delete(DbConst.TABLE_NOTES,
                String.format(DbConst.IN_EXPRESSION, DbConst.COLUMN_ID, ids.toString()), null);
    }

    public synchronized long updateEntryText(NoteEntity entity){
        ContentValues cv = new ContentValues();
        cv.put(DbConst.COLUMN_TEXT, entity.getText());
        return mDb.update(DbConst.TABLE_NOTES, cv,
                String.format(DbConst.EQUALS_EXPRESSION, DbConst.COLUMN_ID),
                new String[]{Integer.toString(entity.getId())});
    }

    /**
     * Returns all data as list
     * @return list of data
     */
    @Deprecated
    public synchronized List<NoteEntity> getAllData(){
        Cursor cursor = getAllCursor();
        if(cursor == null){
            return new ArrayList<>(0);
        }
        List<NoteEntity> result = new ArrayList<>(cursor.getCount());
        if(cursor.getCount() == 0){
            return result;
        }
        do{
            result.add(extractEntityForNoteList(cursor));
        }while (cursor.moveToNext());
        return result;
    }

    @Deprecated
    public synchronized Cursor getAllCursor(){
        Cursor cursor = mDb.rawQuery(DbConst.Q_GET_ALL_DATA, null);
        if(cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }

    public synchronized Cursor getAllDataToShowInListCursor(){
        Cursor cursor = mDb.rawQuery(DbConst.Q_GET_ALL_DATA, null);
        if(cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }

    public synchronized Cursor getFromAndToCursor(int from, int to){
        String query = String.format("%s %s, %s", DbConst.Q_SELECT_ENTRIES_FROM_AND_TO,
                Integer.toString(from), Integer.toString(to));
        Cursor cursor = mDb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }

    public synchronized long clearAll(){
        return mDb.delete(DbConst.TABLE_NOTES, null, null);
    }

    /**
     * Returns last entry to be edited.
     * Only id field is returned as it's assumed that image was already saved
     * on the previous step and now we are going to add a text note.
     * @return entry with only entry id loaded
     */
    public synchronized NoteEntity getLastEntry(){
        Cursor cursor = mDb.rawQuery(DbConst.Q_SELECT_LAST_ENTRY, null);
        if(cursor == null || cursor.getCount() != 1){
            return null;
        }
        cursor.moveToFirst();
        return new NoteEntity(cursor.getInt(0));
    }

    public synchronized NoteEntity getEntityToView(int id){
        Cursor cursor = mDb.rawQuery(DbConst.Q_SELECT_ENTRY_TO_VIEW + id, null);
        if(cursor == null || cursor.getCount() != 1){
            return null;
        }
        cursor.moveToFirst();
        NoteEntity result = new NoteEntity(id);
        result.setImage(cursor.getBlob(1));
        result.setText(cursor.getString(2));
        return result;
    }

    public static NoteEntity extractEntityForNoteList(Cursor cursor) {
        Calendar calendar = Calendar.getInstance();
        NoteEntity entity = new NoteEntity(cursor.getInt(0));
        entity.setThumb(cursor.getBlob(1));
        entity.setText(cursor.getString(2));
        calendar.setTimeInMillis(cursor.getLong(3));
        entity.setDate(calendar.getTime());
        return entity;
    }
}
