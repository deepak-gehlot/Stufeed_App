package com.stufeed.android.view.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityInstitutePostBinding;
import com.stufeed.android.databinding.DialogInstitutePostSelectorBinding;
import com.stufeed.android.databinding.DialogPostSelectorBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.viewmodel.PostModel;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class InstitutePostActivity extends AppCompatActivity {

    private ActivityInstitutePostBinding mBinding;
    private final int SELECT_DOC = 101;
    private final int SELECT_BOARD = 102;
    private final int SELECT_EDUKIT = 103;
    private AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_institute_post);
        mBinding.setActivity(this);
        aQuery = new AQuery(this);

        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_DOC) {
            switch (resultCode) {
                case RESULT_OK:

                    break;
                case RESULT_CANCELED:
                    Utility.showToast(InstitutePostActivity.this, "Recording canceled.");
                    break;
            }
        } else if (requestCode == SELECT_BOARD) {
            switch (resultCode) {
                case RESULT_OK:

                    break;
                case RESULT_CANCELED:
                    Utility.showToast(InstitutePostActivity.this, "Selection canceled.");
                    break;
            }
        } else if (requestCode == SELECT_EDUKIT) {
            switch (resultCode) {
                case RESULT_OK:

                    break;
                case RESULT_CANCELED:
                    Utility.showToast(InstitutePostActivity.this, "Selection canceled.");
                    break;
            }
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                    File file = imageFiles.get(0);
                    mBinding.selectedImgLayout.setVisibility(View.VISIBLE);
                    aQuery.id(mBinding.selectedImg).image(file, 300);
                    //mBinding.getModel().setFile(file);
                }
            });
        }
    }

    public void onPostButtonClick() {
        // need to validate post
        mBinding.postSelectionLayout.setVisibility(View.VISIBLE);

    }

    public void onClickBoard() {
        Intent intent = new Intent(InstitutePostActivity.this, EdukitBoardActivity.class);
        startActivityForResult(intent, SELECT_BOARD);
        mBinding.postSelectionLayout.setVisibility(View.GONE);
    }

    public void onClickEdukit() {
        Intent intent = new Intent(InstitutePostActivity.this, EdukitSelectionActivity.class);
        startActivityForResult(intent, SELECT_EDUKIT);
        mBinding.postSelectionLayout.setVisibility(View.GONE);
    }


    /**
     * On attachment button click
     */
    public void onAttachmentButtonClick() {
        DialogInstitutePostSelectorBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(InstitutePostActivity.this), R
                .layout.dialog_institute_post_selector, null, false);
        final Dialog dialog = new Dialog(InstitutePostActivity.this);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setTitle("Select");
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switch (v.getId()) {
                    case R.id.cameraButton:
                        openCamera();
                        break;
                    case R.id.galleryButton:
                        openGallery();
                        break;
                    case R.id.documentButton:
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("file/*");
                        startActivityForResult(intent, SELECT_DOC);
                        break;
                }
            }
        };

        dialogBinding.cameraButton.setOnClickListener(onClickListener);
        dialogBinding.galleryButton.setOnClickListener(onClickListener);
        dialogBinding.documentButton.setOnClickListener(onClickListener);

        dialog.show();
    }


    /**
     * Request camera permission, if permission granted then open camera
     */
    private void openCamera() {
        new Permissive.Request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        if (permissions.length == 2) {
                            EasyImage.openCamera(InstitutePostActivity.this, 1);
                        }
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(InstitutePostActivity.this, "Need permission to open camera.");
            }
        }).execute(InstitutePostActivity.this);
    }

    /**
     * Request gallery(READ_EXTERNAL_STORAGE) permission, if permission granted then open gallery
     */
    private void openGallery() {
        new Permissive.Request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        EasyImage.openGallery(InstitutePostActivity.this, 1);
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(InstitutePostActivity.this, "Need permission to open gallery.");
            }
        }).execute(InstitutePostActivity.this);
    }
}
