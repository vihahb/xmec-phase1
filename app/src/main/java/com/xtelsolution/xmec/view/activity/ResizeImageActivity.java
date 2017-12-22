package com.xtelsolution.xmec.view.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.xtelsolution.sdk.callback.DialogListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.ImagePicker;
import com.xtelsolution.sdk.utils.PermissionHelper;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.FileObject;

import java.io.File;

public class ResizeImageActivity extends BasicActivity {

    private CropImageView cropImageView;

    private String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private final int REQUEST_PERRMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resize_image);

        initToolbar(R.id.toolbar, null);

        initView();
    }

    private void initView() {
        FileObject file = null;

        try {
            file = (FileObject) getIntent().getSerializableExtra(Constants.OBJECT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file == null || file.getFile() == null) {
            showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.layout_ok), new DialogListener() {
                @Override
                public void negativeClicked() {

                }

                @Override
                public void positiveClicked() {
                    finish();
                }
            });

            return;
        }

        cropImageView = findViewById(R.id.cropImageView);
        Uri uri = Uri.fromFile(file.getFile());
        cropImageView.setImageUriAsync(uri);

        cropImageView.setShowCropOverlay(false);

        cropImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cropImageView.isShowCropOverlay())
                    cropImageView.setShowCropOverlay(false);
                else
                    cropImageView.setShowCropOverlay(true);
            }
        });
    }

    private void chooseImage() {
        Bitmap bitmap = ImagePicker.scaleBitmap(cropImageView.getCroppedImage(), 500, 500);
        File file = ImagePicker.saveBitmapToFile(bitmap);

        FileObject fileObject = new FileObject();
        fileObject.setFile(file);

        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, fileObject);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_resize_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_rota:
                cropImageView.rotateImage(90);
                break;
            case R.id.action_done:
                if (PermissionHelper.checkPermission(permission, this, REQUEST_PERRMISSION))
                    chooseImage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERRMISSION) {
            if (PermissionHelper.checkResult(grantResults))
                chooseImage();
            else
                showLongToast(getString(R.string.error_not_allow_permission));
        }
    }
}