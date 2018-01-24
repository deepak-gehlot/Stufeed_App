package com.stufeed.android.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityPostBinding;
import com.stufeed.android.databinding.DialogPostSelectorBinding;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;
    private String settingStr[] = new String[]{"Allow Comment", "Allow Repost"};
    private boolean settingBool[] = {true, true};
    private AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post);
        binding.setActivity(this);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        aQuery = new AQuery(PostActivity.this);
    }

    /**
     * Show setting dialog
     */
    public void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Settings");
        builder.setMultiChoiceItems(settingStr, settingBool, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                settingBool[which] = isChecked;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                for (int i = 0; i < settingBool.length; i++) {
                    boolean checked = settingBool[i];
                    if (checked) {

                    }
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * On attachment button click
     */
    public void onAttachmentButtonClick() {
        DialogPostSelectorBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(PostActivity.this), R
                .layout.dialog_post_selector, null, false);
        final Dialog dialog = new Dialog(PostActivity.this);
        dialog.setContentView(dialogBinding.getRoot());

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
                        break;
                }
            }
        };

        dialogBinding.cameraButton.setOnClickListener(onClickListener);
        dialogBinding.galleryButton.setOnClickListener(onClickListener);
        dialogBinding.documentButton.setOnClickListener(onClickListener);

        dialog.show();
    }


    private void openCamera() {
        new Permissive.Request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        if (permissions.length == 2) {
                            EasyImage.openCamera(PostActivity.this, 1);
                        }
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                // given permissions are refused
            }
        }).execute(PostActivity.this);
    }

    private void openGallery() {
        new Permissive.Request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        EasyImage.openGallery(PostActivity.this, 1);
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                // given permissions are refused
            }
        }).execute(PostActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                File file = imageFiles.get(0);
                binding.selectedImgLayout.setVisibility(View.VISIBLE);
                aQuery.id(binding.selectedImg).image(file, 100);
            }
        });
    }
}
