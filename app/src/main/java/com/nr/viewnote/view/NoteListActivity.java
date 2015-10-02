package com.nr.viewnote.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
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
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;
    private NoteListMode mMode;
    private RetainedTaskFragment mRetainedTaskFragment;
    private String filterText;
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
        mRemoveMenuItem.setVisible(false);
        mSearchMenuItem = menu.findItem(R.id.menu_item_search_note);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchViewListener());
        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, new ActionExpandListener());
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
        new DeleteItemTask().execute();
    }

    private void startMode(NoteListMode mode){
        switch (mode){
            case NORMAL:
                mSearchMenuItem.setVisible(true);
                break;
            case FILTER:
                break;
        }
        mMode = mode;
        updateViewState();
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
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.chkNote);
        checkBox.setChecked(!checkBox.isChecked());
        updateViewState();
    }

    @Override
    public void onItemCheckStateChanged(View view, NoteEntity entity, boolean isChecked) {
        if(isChecked){
            mCheckedNotes.add(entity);
        }else{
            mCheckedNotes.remove(entity);
        }
        updateViewState();
    }

    @Override
    public void onBackPressed() {
        if(NoteListMode.FILTER == mMode){
            mSearchMenuItem.collapseActionView();
        }else{
            super.onBackPressed();
        }
    }

    private void updateViewState() {
        mRemoveMenuItem.setVisible(mNoteListFragment.hasChecked());
    }

    /**
     * Action search item expand listener
     */
    private class ActionExpandListener implements MenuItemCompat.OnActionExpandListener{

        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {
            startMode(NoteListMode.FILTER);
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            startMode(NoteListMode.NORMAL);
            return true;
        }
    }

    /**
     * Search view listener
     */
    private class SearchViewListener implements SearchView.OnQueryTextListener{

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            filterText = newText;
            mNoteListFragment.filter(newText);
            return false;
        }
    }

    /**
     * Delete item task
     */
    private class DeleteItemTask extends AbstractProcedureWithProgressDialog{

        public DeleteItemTask() {
            super(mRetainedTaskFragment,
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
            mCheckedNotes.clear();
            if(NoteListMode.FILTER == mMode){
                mNoteListFragment.clearChecked();
                mNoteListFragment.filter(filterText);
            }else {
                mNoteListFragment.notifyDataSetChanged();
            }
            startMode(NoteListMode.NORMAL);
        }
    }
}
