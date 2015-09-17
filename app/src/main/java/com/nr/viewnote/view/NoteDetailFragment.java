package com.nr.viewnote.view;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nr.viewnote.R;

import roboguice.fragment.provided.RoboFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteDetailFragment extends RoboFragment {

    public NoteDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }


}
