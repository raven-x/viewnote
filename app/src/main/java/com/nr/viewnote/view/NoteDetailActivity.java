package com.nr.viewnote.view;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nr.viewnote.Const;
import com.nr.viewnote.R;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

public class NoteDetailActivity extends RoboGuiceAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        setContentView(R.layout.activity_note_detail);
    }

}
