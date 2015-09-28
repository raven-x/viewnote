package com.nr.viewnote.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.nr.androidutils.ActivityUtils;
import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteEntity;
import com.nr.androidutils.ToastUtils;

import java.util.List;
import java.util.Locale;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_sound_record)
public class SoundRecordActivity extends RoboActivity {
    private static final int REQ_CODE_SPEECH_INPUT = 100;

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
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.title_dictate_note));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException e){
            ToastUtils.showToastShort(this, R.string.speech_activity_error);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(RESULT_OK == resultCode && REQ_CODE_SPEECH_INPUT == requestCode && data != null){
            processSpeechApiResult(data);
        }
    }

    private void processSpeechApiResult(Intent data) {
        List<String> textResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if(textResult != null) {
            String rr = Stream.of(textResult).collect(Collectors.joining());
            txtNoteText.setText(rr);
            updateViewState();
        }
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
        updateViewState();
    }

    private void updateViewState() {
        btnApplyNote.setEnabled(!txtNoteText.getText().toString().isEmpty());
    }
}
