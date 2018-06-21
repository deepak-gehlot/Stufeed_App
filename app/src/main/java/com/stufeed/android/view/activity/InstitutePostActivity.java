package com.stufeed.android.view.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.PostResponse;
import com.stufeed.android.databinding.ActivityInstitutePostBinding;
import com.stufeed.android.databinding.DialogInstitutePostSelectorBinding;
import com.stufeed.android.databinding.DialogPostSelectorBinding;
import com.stufeed.android.util.Extension;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.util.ValidationTemplate;
import com.stufeed.android.view.viewmodel.EdukitPostModel;
import com.stufeed.android.view.viewmodel.PostModel;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstitutePostActivity extends AppCompatActivity {

    private ActivityInstitutePostBinding mBinding;
    private int code;
    public static final int SELECT_BOARD = 102;
    public static final int SELECT_EDUKIT = 103;
    private AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_institute_post);
        MobileAds.initialize(this,
                getString(R.string.ad_mob_id));
        mBinding.setActivity(this);
        aQuery = new AQuery(this);
        EdukitPostModel edukitPostModel = new EdukitPostModel();
        edukitPostModel.setUserId(Utility.getLoginUserId(this));
        //edukitPostModel.setUserId("45");
        mBinding.setModel(edukitPostModel);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        getDataFromBundle();

    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
        } else {
            code = bundle.getInt("code");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerConst.REQUEST_CODE_DOC) {
            switch (resultCode) {
                case RESULT_OK:
                    mBinding.selectedImgLayout.setVisibility(View.GONE);
                    File file = new File(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0));
                    mBinding.getModel().setFile(file);
                    mBinding.getModel().setPostValue("1");
                    mBinding.docImgLayout.setVisibility(View.VISIBLE);
                    mBinding.docTitle.setText(file.getName());
                    String ext = FilenameUtils.getExtension(file.getPath());
                    setFileAsExtension(ext);
                    break;
                case RESULT_CANCELED:
                    Utility.showToast(InstitutePostActivity.this, "Canceled.");
                    break;
            }
        } else if (requestCode == SELECT_BOARD) {
            switch (resultCode) {
                case RESULT_OK:
                    EdukitPostModel postModel = mBinding.getModel();
                    String boardId = data.getStringExtra("board_id");
                    postModel.setBoardId(boardId);
                    postModel.setType("0");
                    post(postModel);
                    break;
                case RESULT_CANCELED:
                    Utility.showToast(InstitutePostActivity.this, "Selection canceled.");
                    break;
            }
        } else if (requestCode == SELECT_EDUKIT) {
            switch (resultCode) {
                case RESULT_OK:
                    EdukitPostModel postModel = mBinding.getModel();
                    String boardId = data.getStringExtra("edukit_id");
                    postModel.setEdukitId(boardId);
                    postModel.setType("1");
                    post(postModel);
                    break;
                case RESULT_CANCELED:
                    Utility.showToast(InstitutePostActivity.this, "Selection canceled.");
                    break;
            }
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                    try {
                        File file = imageFiles.get(0);
                        file = new Compressor(InstitutePostActivity.this).compressToFile(file);
                        mBinding.docImgLayout.setVisibility(View.GONE);
                        mBinding.selectedImgLayout.setVisibility(View.VISIBLE);
                        aQuery.id(mBinding.selectedImg).image(file, 300);
                        mBinding.getModel().setFile(file);
                        mBinding.getModel().setPostValue("3");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void onCloseClick(int type) {
        switch (type) {
            case 1: // image
                mBinding.getModel().setPostValue("");
                mBinding.getModel().setFile(null);
                mBinding.docImgLayout.setVisibility(View.GONE);
                break;
            case 2: // document
                mBinding.getModel().setPostValue("");
                mBinding.getModel().setFile(null);
                mBinding.docImgLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void setFileAsExtension(String extension) {
        switch (extension) {
            case "pdf":
                mBinding.docImg.setImageResource(R.drawable.icon_file_pdf);
                break;
            case "doc":
            case "docx":
                mBinding.docImg.setImageResource(R.drawable.icon_file_doc);
                break;
            case "xlsx":
            case "xls":
                mBinding.docImg.setImageResource(R.drawable.icon_file_xls);
                break;
            case "txt":
                mBinding.docImg.setImageResource(R.drawable.icon_file_unknown);
                break;
            case "ppt":
                mBinding.docImg.setImageResource(R.drawable.icon_file_ppt);
                break;
            default:
                mBinding.docImg.setImageResource(R.drawable.icon_file_unknown);
        }
    }

    /**
     * Post button click
     */
    public void onPostButtonClick(EdukitPostModel edukitPostModel) {
        // need to validate post
        if (validate(edukitPostModel)) {
            if (code == SELECT_BOARD) {
                Intent intent = new Intent(InstitutePostActivity.this, EdukitBoardActivity.class);
                startActivityForResult(intent, InstitutePostActivity.SELECT_BOARD);
            } else {
                Intent intent = new Intent(InstitutePostActivity.this, EdukitSelectionActivity.class);
                startActivityForResult(intent, InstitutePostActivity.SELECT_EDUKIT);
            }
            //mBinding.postSelectionLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * validate post
     */
    public boolean validate(EdukitPostModel edukitPostModel) {
        if (TextUtils.isEmpty(edukitPostModel.getTitle()) || TextUtils.isEmpty(edukitPostModel.getDescription()) ||
                TextUtils.isEmpty(edukitPostModel.getPostValue())) {
            Utility.showToast(InstitutePostActivity.this, "All required");
            return false;
        } else if (edukitPostModel.getPostValue().equals("3") && edukitPostModel.getFile() == null) {
            Utility.showToast(InstitutePostActivity.this, "All required");
            return false;
        } else if (edukitPostModel.getPostValue().equals("1") && edukitPostModel.getFile() == null) {
            Utility.showToast(InstitutePostActivity.this, "All required");
            return false;
        } else if (!Extension.getInstance().executeStrategy(InstitutePostActivity.this, "", ValidationTemplate.INTERNET)) {
            Utility.setNoInternetPopup(InstitutePostActivity.this);
            return false;
        } else {
            return true;
        }
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
                        pickDocument();
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

    /**
     * Pic document file
     */
    private void pickDocument() {
        new Permissive.Request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        FilePickerBuilder.getInstance().setMaxCount(1)
                                .setSelectedFiles(new ArrayList<String>())
                                .setActivityTheme(R.style.AppTheme)
                                .pickFile(InstitutePostActivity.this);
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(InstitutePostActivity.this, "Need permission to open gallery.");
            }
        }).execute(InstitutePostActivity.this);
    }

    private void post(EdukitPostModel edukitPostModel) {
        Api api = APIClient.getClient().create(Api.class);
        MultipartBody.Part part;
        if (edukitPostModel.getPostValue().equals("3")) {
            part = edukitPostModel.prepareFilePart("image", edukitPostModel.getFile());
        } else {
            part = edukitPostModel.prepareFilePart("file", edukitPostModel.getFile());
        }


        Call<PostResponse> responseCall = api.institutePost(edukitPostModel.getPostBody(), part);
        ProgressDialog.getInstance().showProgressDialog(InstitutePostActivity.this);
        responseCall.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handlePostResponse(response.body());
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(InstitutePostActivity.this);
            }
        });
    }

    /**
     * Handle post response
     *
     * @param postResponse @PostResponse
     */
    private void handlePostResponse(PostResponse postResponse) {
        if (postResponse != null) {
            if (postResponse.getResponseCode().equals(Api.SUCCESS)) {
                Utility.showToast(InstitutePostActivity.this, "Posted Successfully.");
                finish();
            } else {
                Utility.showToast(InstitutePostActivity.this, postResponse.getResponseMessage());
            }
        } else {
            Utility.showErrorMsg(InstitutePostActivity.this);
        }
    }
}
