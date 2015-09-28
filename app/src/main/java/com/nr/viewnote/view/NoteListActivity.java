package com.nr.viewnote.view;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;

import com.nr.androidutils.progressdialog.AbstractProcedureWithProgressDialog;
import com.nr.androidutils.progressdialog.RetainedTaskFragment;
import com.nr.viewnote.Const;
import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteEntity;

import java.util.HashSet;
import java.util.Set;

import roboguice.inject.InjectView;

public class NoteListActivity extends RoboGuiceAppCompatActivity implements INoteListFragmentListener {

    @InjectView(R.id.note_list_toolbar)
    private Toolbar mToolbar;
    private NoteListFragment mNoteListFragment;
    private MenuItem mRemoveMenuItem;
    private NoteListMode mMode;
    private RetainedTaskFragment mRetainedTaskFragment;
    private final Set<NoteEntity> mCheckedNotes = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRetainedTaskFragment = RetainedTaskFragment.establishRetainedMonitoredFragment(this);
        setContentView(R.layout.activity_note_list);
        setSupportActionBar(mToolbar);
        mNoteListFragment = (NoteListFragment) getFragmentManager().findFragmentById(R.id.note_list_fragment);
        mNoteListFragment.addListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_list, menu);
        mRemoveMenuItem = menu.findItem(R.id.menu_item_remove_note);
        startMode(NoteListMode.NORMAL);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_item_remove_note:
                onItemsRemoved();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onItemsRemoved() {
        new DeleteItemTask(mRetainedTaskFragment).execute();
    }

    private void startMode(NoteListMode mode){
        switch (mode){
            case REMOVE:
                mRemoveMenuItem.setVisible(true);
                break;
            case NORMAL:
                mRemoveMenuItem.setVisible(false);
                break;
        }
        mMode = mode;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, NoteEntity entity, long id) {
        if(Configuration.ORIENTATION_PORTRAIT == getResources().getConfiguration().orientation) {
            Intent intent = new Intent(this, NoteDetailActivity.class);
            intent.putExtra(Const.ENTITY_ID, entity.getId());
            startActivity(intent);
        }else{
            NoteDetailFragment detailFragment = (NoteDetailFragment) getFragmentManager()
                    .findFragmentById(R.id.note_details_fragment);
            detailFragment.setView(entity.getId());
        }
    }

    @Override
    public void onItemLongClick(AdapterView<?> parent, View view, NoteEntity entity, long id) {
        startMode(NoteListMode.REMOVE);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.chkNote);
        checkBox.setChecked(!checkBox.isChecked());
    }

    @Override
    public void onItemCheckStateChanged(View view, NoteEntity entity, boolean isChecked) {
        if(isChecked){
            mCheckedNotes.add(entity);
        }else{
            mCheckedNotes.remove(entity);
        }
        if(mCheckedNotes.isEmpty()){
            startMode(NoteListMode.NORMAL);
        }else{
            startMode(NoteListMode.REMOVE);
        }
    }

    /**
     * Delete item task
     */
    private class DeleteItemTask extends AbstractProcedureWithProgressDialog{

        public DeleteItemTask(RetainedTaskFragment retainedFragment) {
            super(retainedFragment,
                    getResources().getString(R.string.title_removing_entries),
                    getResources().getString(R.string.title_removing_entries),
                    DeleteItemTask.class.getSimpleName(), false);
        }

        @Override
        protected Object doInBackground(Object ... params) {
            DbAdapter.getInstance(NoteListActivity.this).removeEntries(mCheckedNotes);
            return null;
        }

        @Override
        protected void onPostExecute(Object integer) {
            super.onPostExecute(integer);
            mNoteListFragment.notifyDataSetChanged();
            mCheckedNotes.clear();
            startMode(NoteListMode.NORMAL);
        }
    }
}
