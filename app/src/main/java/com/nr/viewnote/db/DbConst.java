package com.nr.viewnote.db;

/**
 * Database constants
 */
public class DbConst {
    public static final String DATABASE_NAME = "viewnote.db";
    public static final int VERSION = 1;
    public static final String TABLE_NOTES = "notes";

    //Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_THUMBNAIL = "thumbnail";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_DATE = "date";

    //Queries
    static final String Q_CREATE_NOTES_TABLE =
            String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s BLOB," +
                    " %s TEXT," +
                    " %s INTEGER)",
                    TABLE_NOTES, COLUMN_ID, COLUMN_PICTURE,
                    COLUMN_TEXT, COLUMN_DATE);
    static final String Q_DROP_NOTES_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NOTES);
    static final String Q_GET_ALL_DATA = String.format("SELECT * FROM %s", TABLE_NOTES);

    static final String IN_EXPRESSION = "%s IN %s";
    static final String EQUALS_EXPRESSION = "%s = ?";

    /**Returns last entry to be edited*/
    static final String Q_SELECT_LAST_ENTRY = String.format(
            "SELECT %s FROM %s WHERE %s = (SELECT MAX(%s) FROM %s)",
            COLUMN_ID, TABLE_NOTES, COLUMN_DATE, COLUMN_DATE, TABLE_NOTES);
}
