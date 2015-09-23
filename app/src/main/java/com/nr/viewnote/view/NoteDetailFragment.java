package com.nr.viewnote.view;


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

        if(getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            id = getActivity().getIntent().getExtras().getInt(Const.ENTITY_ID);
        }else{
            if(savedInstanceState != null){
                id = savedInstanceState.getInt(Const.ENTITY_ID);
            }
        }

        if(id != null) {
            NoteEntity entity = DbAdapter.getInstance(getActivity()).getEntityToView(id);
            imageView.setImageBitmap(BitmapUtils.convertCompressedByteArrayToBitmap(entity.getImage()));
            editText.setText(entity.getText());
        }
    }

    @Override
    public void onPause() {
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ENTITY_ID, id);
        super.onPause();
    }
}
