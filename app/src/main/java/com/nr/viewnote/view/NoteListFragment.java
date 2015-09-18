package com.nr.viewnote.view;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nr.viewnote.R;
import com.nr.viewnote.db.NoteCursorLoader;

import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectView;


public class NoteListFragment extends RoboFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.lstNotes)
    private RecyclerView lstNotes;

    private NoteListCursorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Set cursor as null - it will be initialized later
          in cursor loader callback
        * */
        adapter = new NoteListCursorAdapter(getActivity(), null);
        //Default animations
        lstNotes.setItemAnimator(new DefaultItemAnimator());
        //As list
        lstNotes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        lstNotes.setAdapter(adapter);
        //lstNotes.addItemDecoration();

        //Init loader manager
        getActivity().getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new NoteCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
