package com.nr.viewnote;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.util.Arrays;
import java.util.List;

import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteEntity;

/**
 * Database API test
 */
public class DatabaseTest extends AndroidTestCase {
    private DbAdapter mDbAdapter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //Performs database operations with database renamed (with prefix added)
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        mDbAdapter = DbAdapter.getInstance(context);
        //Drops all existing data
        mDbAdapter.clearAll();
    }

    public void testRemoveEntry(){
        mDbAdapter.addEntry(TestConst.ENTRY1, TestConst.ENTRY1, TestConst.TEXT1);

        List<NoteEntity> result = mDbAdapter.getAllData();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        long ret = mDbAdapter.removeEntry(result.get(0));
        assertTrue(ret > 0);

        result = mDbAdapter.getAllData();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    public void testRemoveEntries(){
        mDbAdapter.addEntry(TestConst.ENTRY1, TestConst.ENTRY1, TestConst.TEXT1);

        List<NoteEntity> result = mDbAdapter.getAllData();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        long ret = mDbAdapter.removeEntries(Arrays.asList(result.get(0)));
        assertTrue(ret > 0);

        result = mDbAdapter.getAllData();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    public void testUpdateEntry(){
        mDbAdapter.addEntry(TestConst.ENTRY1, TestConst.ENTRY1, TestConst.TEXT1);

        List<NoteEntity> result = mDbAdapter.getAllData();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        NoteEntity entity = result.get(0);
        entity.setText("new text");

        mDbAdapter.updateEntryText(entity);

        result = mDbAdapter.getAllData();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        entity = result.get(0);

        assertEquals("new text", entity.getText());
    }

    public void testGetAllCursor(){
        mDbAdapter.addEntry(TestConst.ENTRY1, TestConst.ENTRY1, TestConst.TEXT1);

        Cursor cursor = mDbAdapter.getAllCursor();

        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
    }

    public void testGetLastData(){
        mDbAdapter.addEntry(TestConst.ENTRY1, TestConst.ENTRY1, TestConst.TEXT1);
        mDbAdapter.addEntry(TestConst.ENTRY2, TestConst.ENTRY1, TestConst.TEXT2);

        NoteEntity result = mDbAdapter.getLastEditedEntry();

        assertNotNull(result);
    }

    @Override
    protected void tearDown() throws Exception {
        mDbAdapter.close();
        super.tearDown();
    }

}