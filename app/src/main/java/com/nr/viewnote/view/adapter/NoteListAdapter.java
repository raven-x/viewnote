package com.nr.viewnote.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nr.androidutils.BitmapUtils;
import com.nr.viewnote.Const;
import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteEntity;
import com.nr.viewnote.view.INoteListFragmentListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by vkirillov on 21.09.2015.
 */
public class NoteListAdapter extends CursorAdapter implements CompoundButton.OnCheckedChangeListener{

    private INoteListFragmentListener listener;

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
        CheckBox checkBox  = (CheckBox) view.findViewById(R.id.chkNote);

        NoteEntity note = DbAdapter.extractEntityForNoteList(cursor);
        if(note != null) {
            imgView.setImageBitmap(BitmapUtils.convertCompressedByteArrayToBitmap(note.getThumb()));
            txtNote.setText(note.getText());
            txtDate.setText(Const.SMPL_DATE_FORMAT.format(note.getDate()));
            view.setTag(note);
            checkBox.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        View parent = (View) buttonView.getParent();
        NoteEntity entity = (NoteEntity) parent.getTag();
        if(listener != null){
            listener.onItemCheckStateChanged(parent, entity, isChecked);
        }
    }

    public void setListener(INoteListFragmentListener listener) {
        this.listener = listener;
    }
}
