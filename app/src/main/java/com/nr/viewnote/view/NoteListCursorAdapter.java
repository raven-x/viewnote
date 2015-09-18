package com.nr.viewnote.view;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nr.androidutils.BitmapUtils;
import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by vkirillov on 18.09.2015.
 */
class NoteListCursorAdapter extends RecyclerViewCursorAdapter<NoteListCursorAdapter.ViewHolder> {

    private final DateFormat df = new SimpleDateFormat("yyyy-MMMM-dd HH:mm:ss");

    public NoteListCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        NoteEntity note = DbAdapter.extractEntityForNoteList(cursor);
        viewHolder.imgView.setImageBitmap(BitmapUtils.convertCompressedByteArrayToBitmap(note.getThumb()));
        viewHolder.txtNote.setText(note.getText());
        viewHolder.txtDate.setText(df.format(note.getDate()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, null, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgView;
        public TextView txtNote;
        public TextView txtDate;

        public ViewHolder(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.img_thumb);
            txtNote = (TextView) itemView.findViewById(R.id.txtNoteText);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
        }
    }
}
