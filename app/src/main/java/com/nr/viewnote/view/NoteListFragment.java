package com.nr.viewnote.view;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteCursorLoader;
import com.nr.viewnote.db.NoteEntity;
import com.nr.viewnote.view.adapter.NoteListAdapter;

import java.util.LinkedList;
import java.util.List;

import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectView;


public class NoteListFragment extends RoboFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.lstNotes)
    private ListView lstNotes;

    private NoteListAdapter adapter;

    private final List<INoteListFragmentListener> mListeners = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
          Set cursor as null - it will be initialized later
          in cursor loader callback
        * */
        adapter = new NoteListAdapter(getActivity(), null);
        adapter.setFilterQueryProvider(constraint -> DbAdapter
                .getInstance(NoteListFragment.this.getActivity())
                .getFilteredData(constraint.toString()));
        lstNotes.setAdapter(adapter);
        lstNotes.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lstNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return NoteListFragment.this.onItemLongClick(parent, view, position, id);
            }
        });
        lstNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteListFragment.this.onItemClick(parent, view, position, id);
            }
        });

        //Init loader manager
        getActivity().getLoaderManager().initLoader(0, null, this);
    }

    private boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        fireOnItemLongClick(parent, view, position, id);
        return false;
    }

    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fireOnItemClick(parent, view, position, id);
    }

    private NoteEntity getNoteEntity(int position) {
        Cursor currentCursor = (Cursor) lstNotes.getItemAtPosition(position);
        return DbAdapter.extractEntityForNoteList(currentCursor);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearListeners();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new NoteCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.changeCursor(cursor);
    }

    private void clearListeners(){
        adapter.setListener(null);
        mListeners.clear();
    }

    public boolean hasChecked(){
        return adapter.hasChecked();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    public void addListener(INoteListFragmentListener listener){
        adapter.setListener(listener);
        mListeners.add(listener);
    }

    public void removeListener(INoteListFragmentListener listener){
        adapter.setListener(null);
        mListeners.remove(listener);
    }

    public void notifyDataSetChanged(){
        clearChecked();
        getLoaderManager().restartLoader(0, null, this);
        adapter.notifyDataSetChanged();
    }

    public void clearChecked(){
        adapter.clearChecked();
    }

    public void filter(CharSequence constraint){
        adapter.getFilter().filter(constraint);
    }

    private void fireOnItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        for(INoteListFragmentListener listener : mListeners){
            listener.onItemLongClick(parent, view, getNoteEntity(position), id);
        }
    }

    private void fireOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        for(INoteListFragmentListener listener : mListeners){
            listener.onItemClick(parent, view, getNoteEntity(position), id);
        }
    }
}
