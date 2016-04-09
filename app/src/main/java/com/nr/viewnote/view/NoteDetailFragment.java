package com.nr.viewnote.view;


import android.app.Fragment;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nr.androidutils.BitmapUtils;
import com.nr.viewnote.Const;
import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.viewnote.db.NoteEntity;

import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectView;


/**
 * A simple {@link Fragment} subclass.
 *
 * Portions of image scaling code taken from
 * https://androidcookbook.com/Recipe.seam?recipeId=2273
 *
 */
public class NoteDetailFragment extends RoboFragment {

    @InjectView(R.id.imgNoteImage)
    private TouchImageView imageView;

    @InjectView(R.id.txtNoteText)
    private TextView editText;

    private Long id;

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
        editText.setMovementMethod(new ScrollingMovementMethod());
        setOnCreate(savedInstanceState);
    }

    private void setOnCreate(Bundle savedInstanceState) {
        if(getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            id = getActivity().getIntent().getExtras().getLong(Const.ENTITY_ID);
        }
        if(id == null && savedInstanceState != null){
            id = savedInstanceState.getLong(Const.ENTITY_ID);
        }

        if(id != null) {
            setView();
        }
    }

    private void setView() {
        NoteEntity entity = DbAdapter.getInstance(getActivity()).getEntityToView(id);
//        imageView.setZoomEnabled(true);
//        imageView.setPanEnabled(true);
//        imageView.setQuickScaleEnabled(true);
//        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        //imageView.setImage(ImageSource.bitmap(BitmapUtils.convertCompressedByteArrayToBitmap(entity.getImage())));
        imageView.setImageBitmap(BitmapUtils.convertCompressedByteArrayToBitmap(entity.getImage()));
        editText.setText(entity.getText());
        editText.setEnabled(true);
    }

    public void setView(long id){
        this.id = id;
        setView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(id != null) {
            outState.putLong(Const.ENTITY_ID, id);
        }
        super.onSaveInstanceState(outState);
    }
}
