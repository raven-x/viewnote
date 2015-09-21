package com.nr.viewnote.db;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nr.androidutils.BitmapUtils;
import com.nr.viewnote.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by vkirillov on 21.09.2015.
 */
public class NoteListAdapter extends CursorAdapter {

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
