package com.nr.viewnote.view;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nr.androidutils.BitmapUtils;
import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteCursorLoader;
import com.nr.viewnote.db.NoteEntity;
import com.nr.viewnote.db.NoteListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lstNotes.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                //TODO
            }

        });

        /*Set cursor as null - it will be initialized later
          in cursor loader callback
        * */
        adapter = new NoteListAdapter(getActivity(), null);
        lstNotes.setAdapter(adapter);
        lstNotes.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lstNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fireOnItemLongClick(parent, view, position, id);
                return false;
            }
        });
        lstNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fireOnItemClick(parent, view, position, id);
            }
        });

        //Init loader manager
        getActivity().getLoaderManager().initLoader(0, null, this);
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
        adapter.swapCursor(cursor);
    }

    private void clearListeners(){
        adapter.setListener(null);
        mListeners.clear();
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
        getLoaderManager().restartLoader(0, null, this);
        adapter.notifyDataSetChanged();
    }

    private void fireOnItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        for(INoteListFragmentListener listener : mListeners){
            listener.onItemLongClick(parent, view, position, id);
        }
    }

    private void fireOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        for(INoteListFragmentListener listener : mListeners){
            listener.onItemClick(parent, view, position, id);
        }
    }
}
