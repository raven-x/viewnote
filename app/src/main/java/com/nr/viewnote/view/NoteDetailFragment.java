package com.nr.viewnote.view;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.nr.androidutils.BitmapUtils;
import com.nr.viewnote.Const;
import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteEntity;

import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteDetailFragment extends RoboFragment {

    @InjectView(R.id.imgNoteImage)
    private ImageView imageView;

    @InjectView(R.id.txtNoteText)
    private EditText editText;

    private Integer id;

    public NoteDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText.setEnabled(false);
        //if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setOnCreate(savedInstanceState);
        //}
        imageView.setOnClickListener(v -> onImageClick(v));
    }

    private void setOnCreate(Bundle savedInstanceState) {
        if(getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            id = getActivity().getIntent().getExtras().getInt(Const.ENTITY_ID);
        }
        if(id == null && savedInstanceState != null){
            id = savedInstanceState.getInt(Const.ENTITY_ID);
        }

        if(id != null) {
            setView();
        }
    }

    private void setView() {
        NoteEntity entity = DbAdapter.getInstance(getActivity()).getEntityToView(id);
        imageView.setImageBitmap(BitmapUtils.convertCompressedByteArrayToBitmap(entity.getImage()));
        editText.setText(entity.getText());
        editText.setEnabled(true);
    }

    public void setView(int id){
        this.id = id;
        setView();
    }

    private void onImageClick(View v){
        //TODO open image in fullscreen system app
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(id != null) {
            outState.putInt(Const.ENTITY_ID, id);
        }
        super.onSaveInstanceState(outState);
    }
}
