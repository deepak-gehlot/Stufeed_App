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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.PostResponse;
import com.stufeed.android.databinding.ActivityPostBinding;
import com.stufeed.android.databinding.DialogPostSelectorBinding;
import com.stufeed.android.util.Extension;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.util.ValidationTemplate;
import com.stufeed.android.view.viewmodel.PostModel;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        PostModel postModel = new PostModel();
        postModel.setUserId(Utility.getLoginUserDetail(PostActivity.this).getUserId());
        binding.setModel(postModel);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        aQuery = new AQuery(PostActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                File file = imageFiles.get(0);
                binding.selectedImgLayout.setVisibility(View.VISIBLE);
                aQuery.id(binding.selectedImg).image(file, 300);
                binding.getModel().setFile(file);
            }
        });
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

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                for (int i = 0; i < settingBool.length; i++) {
                    boolean checked = settingBool[i];
                    switch (i) {
                        case 0:
                            if (checked) {
                                binding.getModel().setAllowComment(1);
                            } else {
                                binding.getModel().setAllowComment(0);
                            }
                            break;
                        case 1:
                            if (checked) {
                                binding.getModel().setAllowRePost(1);
                            } else {
                                binding.getModel().setAllowRePost(0);
                            }
                            break;
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
                            EasyImage.openCamera(PostActivity.this, 1);
                        }
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(PostActivity.this, "Need permission to open camera.");
            }
        }).execute(PostActivity.this);
    }

    /**
     * Request gallery(READ_EXTERNAL_STORAGE) permission, if permission granted then open gallery
     */
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
                Utility.showToast(PostActivity.this, "Need permission to open gallery.");
            }
        }).execute(PostActivity.this);
    }

    /**
     * Validate post
     *
     * @param postModel @PostModel
     * @return true if valid else false
     */
    private boolean validatePost(PostModel postModel) {
        Extension extension = Extension.getInstance();
        if (TextUtils.isEmpty(postModel.getTitle()) ||
                TextUtils.isEmpty(postModel.getDescription()) ||
                (postModel.getFile() == null)) {
            Utility.showToast(PostActivity.this, getString(R.string.all_required));
            return false;
        } else if (!extension.executeStrategy(PostActivity.this, "", ValidationTemplate.INTERNET)) {
            Utility.setNoInternetPopup(PostActivity.this);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Post button click method
     *
     * @param postModel @PostModel
     */
    public void onPostButtonClick(PostModel postModel) {
        if (validatePost(postModel)) {
            Api api = APIClient.getClient().create(Api.class);
            MultipartBody.Part part = postModel.prepareFilePart("file", postModel.getFile());
            Call<PostResponse> responseCall = api.post(postModel.getPostBody(), part);
            ProgressDialog.getInstance().showProgressDialog(PostActivity.this);
            responseCall.enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    ProgressDialog.getInstance().dismissDialog();
                    handlePostResponse(response.body());
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    ProgressDialog.getInstance().dismissDialog();
                    Utility.showErrorMsg(PostActivity.this);
                }
            });
        }
    }

    /**
     * Handle post response
     *
     * @param postResponse @PostResponse
     */
    private void handlePostResponse(PostResponse postResponse) {
        if (postResponse != null) {
            if (postResponse.getResponseCode().equals(Api.SUCCESS)) {
                Utility.showToast(PostActivity.this, "Posted Successfully.");
                finish();
            } else {
                Utility.showToast(PostActivity.this, postResponse.getResponseMessage());
            }
        } else {
            Utility.showErrorMsg(PostActivity.this);
        }
    }
}
