package com.nr.viewnote.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.inject.Key;
import com.nr.viewnote.R;

import java.util.HashMap;
import java.util.Map;

import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import roboguice.activity.event.OnContentChangedEvent;
import roboguice.activity.event.OnStopEvent;
import roboguice.context.event.OnCreateEvent;
import roboguice.context.event.OnDestroyEvent;
import roboguice.event.EventManager;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.inject.RoboInjector;
import roboguice.util.RoboContext;

public class NoteListActivity extends RoboGuiceAppCompatActivity implements INoteListFragmentListener {

    @InjectView(R.id.note_list_toolbar)
    private Toolbar toolbar;

    private NoteListFragment noteListFragment;

    private MenuItem removeMenuItem;

    private NoteListMode mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        setSupportActionBar(toolbar);
        noteListFragment = (NoteListFragment) getFragmentManager().findFragmentById(R.id.note_list_fragment);
        noteListFragment.addListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_list, menu);
        removeMenuItem = menu.findItem(R.id.menu_item_remove_note);
        startMode(NoteListMode.NORMAL);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_item_remove_note:
                startMode(NoteListMode.NORMAL);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startMode(NoteListMode mode){
        switch (mode){
            case REMOVE:
                removeMenuItem.setVisible(true);
                break;
            case NORMAL:
                removeMenuItem.setVisible(false);
                break;
        }
        mMode = mode;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        startMode(NoteListMode.REMOVE);
    }
}
