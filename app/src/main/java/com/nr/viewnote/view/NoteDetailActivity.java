package com.nr.viewnote.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.nr.viewnote.R;

import roboguice.inject.InjectView;

public class NoteDetailActivity extends RoboGuiceAppCompatActivity {

    @InjectView(R.id.main_toolbar)
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        setSupportActionBar(mToolbar);
    }

}
