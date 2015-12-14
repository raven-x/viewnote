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

    /**
     * Singleton getInstance method
     * @param context current context
     * @return singleton database adapter object
     */
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
     * Closes the current connection
     */
    public synchronized void close(){
        mDbHelper.close();
        isOpened = false;
    }

    /**
     * Adds a new entry to database with empty text by default
     * @param image image data
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

    /**
     * Removes a single entity
     * @param entity entity to remove
     */
    public synchronized long removeEntry(NoteEntity entity){
        return mDb.delete(DbConst.TABLE_NOTES,
                String.format(DbConst.EQUALS_EXPRESSION, DbConst.COLUMN_ID),
                new String[]{Long.toString(entity.getId())});
    }

    /**
     * Remove collection of entities
     * @param entities collection of entities
     */
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

    /**
     * Updates current note text
     * @param entity note entity
     */
    public synchronized long updateEntryText(NoteEntity entity){
        ContentValues cv = new ContentValues();
        cv.put(DbConst.COLUMN_TEXT, entity.getText());
        return mDb.update(DbConst.TABLE_NOTES, cv,
                String.format(DbConst.EQUALS_EXPRESSION, DbConst.COLUMN_ID),
                new String[]{Long.toString(entity.getId())});
    }

    /**
     * Returns all data as list
     * It's a waste of resources to load all fields at once
     * so it's newer used but only in tests
     * @return list of note entities
     */
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

    /**
     * Return cursor with a complete set of data
     * It's a waste of resources to load all fields at once
     * so it's newer used but only in tests
     * @return cursor with data
     */
    public synchronized Cursor getAllCursor(){
        Cursor cursor = mDb.rawQuery(DbConst.Q_GET_ALL_DATA, null);
        if(cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }

    /**
     * Returns cursor for viewing data in listview
     * @return cursor cursor with data
     */
    public synchronized Cursor getAllDataToShowInListCursor(){
        return mDb.query(
                false,
                DbConst.TABLE_NOTES,
                new String[]{
                        DbConst.COLUMN_ID, DbConst.COLUMN_THUMBNAIL,
                        DbConst.COLUMN_TEXT, DbConst.COLUMN_DATE},
                null, null, null, null, null, null);
    }

    /**
     * Filters data using message text as constraint
     * @param constraint message text
     * @return cursor with filtered data
     */
    public synchronized Cursor getFilteredData(String constraint){
        return mDb.query(
                false,
                DbConst.TABLE_NOTES,
                new String[]{
                        DbConst.COLUMN_ID, DbConst.COLUMN_THUMBNAIL,
                        DbConst.COLUMN_TEXT, DbConst.COLUMN_DATE},
                DbConst.COLUMN_TEXT + " LIKE ?",
                new String[]{"%" + constraint + "%"},
                null, null, null, null);
    }

    /**
     * Clears all data
     * @return query result
     */
    public synchronized long clearAll(){
        return mDb.delete(DbConst.TABLE_NOTES, null, null);
    }

    /**
     * Returns last entry to be edited.
     * Only id field is returned as it's assumed that image was already saved
     * on the previous step and now we are going to add a text note.
     * @return entry with only entry id loaded
     */
    public synchronized NoteEntity getLastEditedEntry(){
        Cursor cursor = mDb.rawQuery(DbConst.Q_SELECT_LAST_ENTRY, null);
        if(cursor == null || cursor.getCount() != 1){
            return null;
        }
        cursor.moveToFirst();
        return new NoteEntity(cursor.getInt(0));
    }

    /**
     * Extracts set of fields needed to make a detail view
     * @param id entity id
     * @return note entity with id, image and text fields filled
     */
    public synchronized NoteEntity getEntityToView(long id){
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

    /**
     * Extracts set of fields to add item into listview
     * @param cursor cursor
     * @return note entity with id, thumb, text and date fields filled
     */
    public static NoteEntity extractEntityForNoteList(Cursor cursor) {
        Calendar calendar = Calendar.getInstance();
        if(cursor.getCount() > 0) {
            NoteEntity entity = new NoteEntity(cursor.getInt(0));
            entity.setThumb(cursor.getBlob(1));
            entity.setText(cursor.getString(2));
            calendar.setTimeInMillis(cursor.getLong(3));
            entity.setDate(calendar.getTime());
            return entity;
        }else{
            return null;
        }
    }
}
