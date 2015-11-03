package com.nr.viewnote.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.nr.androidutils.BitmapUtils;
import com.nr.viewnote.Const;
import com.nr.viewnote.R;
import com.nr.viewnote.db.DbAdapter;
import com.nr.androidutils.ToastUtils;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

public class MainActivity extends RoboGuiceAppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_RESULT = 1;

    @InjectView(R.id.btn_create_new)
    private Button mBtnCreateNew;

    @InjectView(R.id.btn_view)
    private Button mBtnView;

    @InjectView(R.id.main_toolbar)
    private Toolbar toolbar;

    static {RoboGuice.setUseAnnotationDatabases(false);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        mBtnCreateNew.setOnClickListener(v -> onCreateNew());
        mBtnView.setOnClickListener(v -> onView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up main_button_default_layer, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                processBitmap(data);
            }
        }
    }

    private void processBitmap(Intent data) {
        Bitmap bitmap = getBitmap(data);
        if (bitmap != null) {
            Bitmap thumb = BitmapUtils.resizeBitmap(bitmap, Const.THUMB_SIZE, Const.THUMB_SIZE);
            long result = DbAdapter
                    .getInstance(this)
                    .addEntry(BitmapUtils.convertBitmapToByteArray(bitmap),
                            BitmapUtils.convertBitmapToByteArray(thumb));

            if (result == -1) {
                ToastUtils.showToastLong(this, R.string.db_save_error);
            } else {
                startActivity(new Intent(this, SoundRecordActivity.class));
            }
        }
    }

    private Bitmap getBitmap(Intent data) {
        Bundle extras = data.getExtras();
        return (Bitmap) extras.get("data");
    }

    private void onCreateNew(){
        int permissionCheckResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        //Android 6.0 permission check
        if(PackageManager.PERMISSION_GRANTED == permissionCheckResult){
            createNew();
        }else{
            requestCameraPermission();
        }
    }

    private void requestCameraPermission(){
        //In case user checked "Never ask again"
        if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            new AlertDialog.Builder(this)
                    .setMessage(R.string.camera_permission_rationale)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA},
                                CAMERA_PERMISSION_REQUEST_RESULT);
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create()
                    .show();
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_RESULT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(CAMERA_PERMISSION_REQUEST_RESULT == requestCode){
            // If request is cancelled, the result arrays are empty
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createNew();
            }else {
                //Disable functionality
                ToastUtils.showToastLong(this, R.string.camera_pemission_denied);
                mBtnCreateNew.setEnabled(false);
            }
        }
    }

    private void onView(){
        startActivity(new Intent(this, NoteListActivity.class));
    }

    private void createNew() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
