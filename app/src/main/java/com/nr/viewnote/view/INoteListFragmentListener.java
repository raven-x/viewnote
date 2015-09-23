package com.nr.viewnote.view;

import android.view.View;
import android.widget.AdapterView;

import com.nr.viewnote.db.NoteEntity;

/**
 * Created by vkirillov on 21.09.2015.
 */
public interface INoteListFragmentListener {

    void onItemClick(AdapterView<?> parent, View view, NoteEntity entity, long id);

    void onItemLongClick(AdapterView<?> parent, View view, NoteEntity entity, long id);

    void onItemCheckStateChanged(View view, NoteEntity entity, boolean isChecked);
}
