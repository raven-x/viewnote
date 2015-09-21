package com.nr.viewnote.view;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by vkirillov on 21.09.2015.
 */
public interface INoteListFragmentListener {

    void onItemClick(AdapterView<?> parent, View view, int position, long id);

    void onItemLongClick(AdapterView<?> parent, View view, int position, long id);
}
