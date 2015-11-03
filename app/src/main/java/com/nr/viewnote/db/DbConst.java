package com.nr.viewnote.db;

/**
 * Database constants
 */
public final class DbConst {
    private DbConst(){}

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "viewnote.db";
    public static final String TABLE_NOTES = "notes";

    //Columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_THUMBNAIL = "thumbnail";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_DATE = "date";

    //Queries
    static final String Q_CREATE_NOTES_TABLE =
            String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s BLOB," +
                    " %s BLOB," +
                    " %s TEXT," +
                    " %s INTEGER)",
                    TABLE_NOTES, COLUMN_ID, COLUMN_PICTURE,
                    COLUMN_THUMBNAIL, COLUMN_TEXT, COLUMN_DATE);

    static final String Q_DROP_NOTES_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NOTES);

    static final String Q_GET_ALL_DATA = String.format("SELECT %s, %s, %s, %s FROM %s",
            COLUMN_ID, COLUMN_THUMBNAIL, COLUMN_TEXT, COLUMN_DATE, TABLE_NOTES);

    static final String IN_EXPRESSION = "%s IN %s";
    static final String EQUALS_EXPRESSION = "%s = ?";

    /**Returns last entry to be edited*/
    static final String Q_SELECT_LAST_ENTRY = String.format(
            "SELECT %s FROM %s WHERE %s = (SELECT MAX(%s) FROM %s)",
            COLUMN_ID, TABLE_NOTES, COLUMN_DATE, COLUMN_DATE, TABLE_NOTES);

    /**Returns entry to view and edit*/
    static final String Q_SELECT_ENTRY_TO_VIEW = String.format(
            "SELECT %s, %s, %s FROM %s WHERE %s = ",
            COLUMN_ID, COLUMN_PICTURE, COLUMN_TEXT, TABLE_NOTES,
            COLUMN_ID);
}
