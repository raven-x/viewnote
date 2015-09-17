package com.nr.viewnote.view;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nr.androidutils.BitmapUtils;
import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteCursorLoader;
import com.nr.viewnote.db.NoteEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectView;


public class NoteListFragment extends RoboFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.lstNotes)
    private ListView lstNotes;

    private NoteListAdapter adapter;

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

    private class ViewHolder{
        public ImageView imageView;
        public TextView noteView;
        public TextView dateView;
    }

    private class NoteListAdapter extends CursorAdapter{

        private final DateFormat df = new SimpleDateFormat("yyyy-MMMM-dd HH:mm:ss");

        public NoteListAdapter(Context context, Cursor c) {
            super(context, c, true);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return inflater.inflate(R.layout.list_item_layout, null, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView imgView = (ImageView) view.findViewById(R.id.img_thumb);
            TextView txtNote = (TextView) view.findViewById(R.id.txtNoteText);
            TextView txtDate = (TextView) view.findViewById(R.id.txtDate);

            NoteEntity note = DbAdapter.extractEntityForNoteList(cursor);
            imgView.setImageBitmap(BitmapUtils.convertCompressedByteArrayToBitmap(note.getThumb()));
            txtNote.setText(note.getText());
            txtDate.setText(df.format(note.getDate()));
        }
    }
}
