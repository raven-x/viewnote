package com.nr.viewnote.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nr.viewnote.R;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_note_detail)
public class NoteDetailActivity extends RoboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
