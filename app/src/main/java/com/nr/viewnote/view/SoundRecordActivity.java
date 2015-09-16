package com.nr.viewnote.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.nr.androidutils.ActivityUtils;
import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteEntity;
import com.nr.androidutils.ToastUtils;

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
        txtNoteText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onTextChangeListener();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
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

    @Override
    protected void onPause() {
        super.onPause();
        if(mNote != null){
            String newText = txtNoteText.getText().toString();
            if(!newText.isEmpty()){
                mNote.setText(newText);
            }
            DbAdapter.getInstance(this).updateEntryText(mNote);
        }
    }

    private void onRecordSound(){
        //TODO
    }

    private void onApplyNote(){
        if(mNote != null) {
            mNote.setText(txtNoteText.getText().toString());
            DbAdapter.getInstance(this).updateEntryText(mNote);
            ActivityUtils.goBackTo(this, MainActivity.class);
        }
    }

    private void onCancelNote(){
        if(mNote != null){
            DbAdapter.getInstance(this).removeEntry(mNote);
        }
        ActivityUtils.goBackTo(this, MainActivity.class);
    }

    private void onTextChangeListener(){
        boolean bEnableApplyButton = !txtNoteText.getText().toString().isEmpty();
        btnApplyNote.setEnabled(bEnableApplyButton);
    }
}
