package com.nr.viewnote.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteEntity;
import com.nr.androidutils.ToastUtils;

import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_sound_record)
public class SoundRecordActivity extends RoboActivity {

    @InjectView(R.id.btnRecordSound)
    private Button btnRecord;

    @InjectView(R.id.btnApplyNoteText)
    private Button btnApplyNote;

    @InjectView(R.id.btnCancelNote)
    private Button btnCancelNote;

    @InjectView(R.id.txtNoteText)
    private EditText txtNoteText;

    private NoteEntity mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnApplyNote.setEnabled(false);
        btnRecord.setOnClickListener(v -> onRecordSound());
        btnApplyNote.setOnClickListener(v -> onApplyNote());
        btnCancelNote.setOnClickListener(v -> onCancelNote());
    }

    @Override
      protected void onPause() {
        super.onPause();
        //TODO
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNote = DbAdapter.getInstance(this).getLastEntry();
        if(mNote == null){
            ToastUtils.showToastLong(this, R.string.db_load_last_entry_error);
            btnRecord.setEnabled(false);
            btnApplyNote.setEnabled(false);
        }else{
            txtNoteText.setText(mNote.getText() == null ? "" : mNote.getText());
        }
    }

    private void onRecordSound(){
        //TODO
    }

    private void onApplyNote(){
        //TODO
    }

    private void onCancelNote(){
        if(mNote != null){
            DbAdapter.getInstance(this).removeEntry(mNote);
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
