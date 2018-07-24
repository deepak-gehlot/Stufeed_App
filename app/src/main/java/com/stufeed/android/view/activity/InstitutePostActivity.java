package com.stufeed.android.view.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetEdukitResponse;
import com.stufeed.android.api.response.PostResponse;
import com.stufeed.android.databinding.ActivityInstitutePostBinding;
import com.stufeed.android.databinding.DialogInputBinding;
import com.stufeed.android.databinding.DialogInstitutePostSelectorBinding;
import com.stufeed.android.databinding.DialogPostSelectorBinding;
import com.stufeed.android.util.Extension;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.util.ValidationTemplate;
import com.stufeed.android.view.viewmodel.EdukitPostModel;
import com.stufeed.android.view.viewmodel.PostModel;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import id.zelory.compressor.Compressor;
import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;
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
    private final int RECORD_AUDIO = 101;
    private String audioFilePath = "";
    private MediaPlayer mp;
    Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdate();
        }
    };
    Handler seekHandler = new Handler();
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

        mBinding.attachmentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code == SELECT_BOARD) {
                    onAttachmentButtonClickBoard();
                } else {
                    onAttachmentButtonClickEdukit();
                }
            }
        });

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
                    mBinding.getModel().setPostType(1);

                    break;
                case RESULT_CANCELED:
                    Utility.showToast(InstitutePostActivity.this, "Canceled.");
                    break;
            }
        } else if (requestCode == RECORD_AUDIO) {
            switch (resultCode) {
                case RESULT_OK:
                    mBinding.audioCardLayout.setVisibility(View.VISIBLE);
                    File file = new File(audioFilePath);
                    String fileName = file.getName();
                    mBinding.audioText.setText(fileName);
                    audioPlayer();
                    mBinding.getModel().setPostType(5);
                    mBinding.getModel().setFile(file);
                    break;
                case RESULT_CANCELED:
                    Utility.showToast(InstitutePostActivity.this, "Recording canceled.");
                    break;
            }
        } else if (requestCode == SELECT_BOARD) {
            switch (resultCode) {
                case RESULT_OK:
                    EdukitPostModel postModel = mBinding.getModel();
                    String boardId = data.getStringExtra("board_id");
                    postModel.setBoardId(boardId);
                    postModel.setType("0");
                    //post(postModel);
                    postAll(postModel);
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
                        mBinding.getModel().setPostType(3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * Create Audio player
     */
    private void audioPlayer() {
        //set up MediaPlayer
        mp = new MediaPlayer();
        try {
            mp.setDataSource(audioFilePath);
            mp.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle Audio player play pause
     */
    public void playPauseAudio() {
        if (mp != null) {
            mBinding.audioSeekBar.setMax(mp.getDuration());
            if (mp.isPlaying()) {
                mp.pause();
                seekHandler.removeCallbacks(run);
                mBinding.audioStartStopImg.setImageResource(R.drawable.ic_video_play);
            } else {
                mp.start();
                mBinding.audioStartStopImg.setImageResource(R.drawable.ic_video_pause);
                seekUpdate();
            }
        }
    }

    /**
     * Handle Audio Player seek bar
     */
    public void seekUpdate() {
        mBinding.audioSeekBar.setProgress(mp.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }

    public void onCloseClick(int type) {
        switch (type) {
            case 1: // document
                mBinding.docImgLayout.setVisibility(View.GONE);
                mBinding.getModel().setFile(null);
                mBinding.getModel().setPostType(0);
                break;
            case 2: // audio/ video url
                mBinding.audioVideoImgLayout.setVisibility(View.GONE);
                mBinding.getModel().setFile(null);
                mBinding.getModel().setPostType(0);
                break;
            case 3: // image
                mBinding.selectedImgLayout.setVisibility(View.GONE);
                mBinding.getModel().setFile(null);
                mBinding.getModel().setPostType(0);
                break;
            case 4: // Poll
                mBinding.pollLayout.setVisibility(View.GONE);
                mBinding.getModel().setFile(null);
                mBinding.getModel().setPostType(0);
                break;
            case 5: // Recorded Audio
                mBinding.audioCardLayout.setVisibility(View.GONE);
                mBinding.getModel().setFile(null);
                mBinding.getModel().setPostType(0);
                break;
            case 6: // Remove all type
                mBinding.docImgLayout.setVisibility(View.GONE);
                mBinding.audioVideoImgLayout.setVisibility(View.GONE);
                mBinding.selectedImgLayout.setVisibility(View.GONE);
                mBinding.pollLayout.setVisibility(View.GONE);
                mBinding.audioCardLayout.setVisibility(View.GONE);
                mBinding.getModel().setFile(null);
                mBinding.getModel().setPostType(0);
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
        if (code == SELECT_BOARD) {
            if (validateBoardPost(edukitPostModel)) {
                Intent intent = new Intent(InstitutePostActivity.this, EdukitBoardActivity.class);
                startActivityForResult(intent, InstitutePostActivity.SELECT_BOARD);
            }
        } else {
            if (validate(edukitPostModel)) {
                Intent intent = new Intent(InstitutePostActivity.this, EdukitSelectionActivity.class);
                startActivityForResult(intent, InstitutePostActivity.SELECT_EDUKIT);
            }
        }
        //mBinding.postSelectionLayout.setVisibility(View.VISIBLE);
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
     * Validate post
     *
     * @param postModel @PostModel
     * @return true if valid else false
     */
    private boolean validateBoardPost(EdukitPostModel postModel) {
        Extension extension = Extension.getInstance();
        if (TextUtils.isEmpty(postModel.getDescription())) {
            Utility.showToast(InstitutePostActivity.this, getString(R.string.all_required));
            return false;
        } else if (!extension.executeStrategy(InstitutePostActivity.this, "", ValidationTemplate.INTERNET)) {
            Utility.setNoInternetPopup(InstitutePostActivity.this);
            return false;
        } else {
            return true;
        }
    }

    /**
     * On attachment button click for edukit post
     */
    public void onAttachmentButtonClickEdukit() {
        DialogInstitutePostSelectorBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(InstitutePostActivity.this), R
                .layout.dialog_institute_post_selector, null, false);
        final BottomSheetDialog dialog = new BottomSheetDialog(InstitutePostActivity.this);
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
     * On attachment button click for board post
     */
    public void onAttachmentButtonClickBoard() {
        DialogPostSelectorBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(InstitutePostActivity.this), R
                .layout.dialog_post_selector, null, false);
        final BottomSheetDialog dialog = new BottomSheetDialog(InstitutePostActivity.this);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setTitle("Select");
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onCloseClick(6);
                switch (v.getId()) {
                    case R.id.cameraButtonLayout:
                        openCamera();
                        break;
                    case R.id.galleryButtonLayout:
                        openGallery();
                        break;
                    case R.id.documentButtonLayout:
                        pickDocument();
                        break;
                    case R.id.audioButtonLayout:
                        recordAudio();
                        break;
                    case R.id.pollButtonLayout:
                        mBinding.pollLayout.setVisibility(View.VISIBLE);
                        mBinding.getModel().setPostType(4);
                        break;
                    case R.id.aarticalButtonLayout:
                        showLinkDialog(1);
                        break;
                    case R.id.videoButtonLayout:
                        showLinkDialog(2);
                        break;
                }
            }
        };

        dialogBinding.cameraButtonLayout.setOnClickListener(onClickListener);
        dialogBinding.galleryButtonLayout.setOnClickListener(onClickListener);
        dialogBinding.documentButtonLayout.setOnClickListener(onClickListener);
        dialogBinding.audioButtonLayout.setOnClickListener(onClickListener);
        dialogBinding.pollButtonLayout.setOnClickListener(onClickListener);
        dialogBinding.aarticalButtonLayout.setOnClickListener(onClickListener);
        dialogBinding.videoButtonLayout.setOnClickListener(onClickListener);

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
                                .setActivityTheme(R.style.FilePickerTheme)
                                .pickFile(InstitutePostActivity.this);
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(InstitutePostActivity.this, "Need permission to open gallery.");
            }
        }).execute(InstitutePostActivity.this);
    }

    public void showLinkDialog(final int type) {
        final DialogInputBinding inputBinding = DataBindingUtil.inflate(LayoutInflater.from(InstitutePostActivity.this), R
                .layout.dialog_input, null, false);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(InstitutePostActivity.this);
        bottomSheetDialog.setContentView(inputBinding.getRoot());

        inputBinding.buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = inputBinding.editTextLink.getText().toString().trim();
                if (!TextUtils.isEmpty(link)) {
                    if (Utility.isValidUrl(link)) {
                        bottomSheetDialog.dismiss();
                        if (type == 1) { // artical
                            getImageTitleFromUrl(link);
                        } else {  // video
                            getImageTitleFromVideoUrl(link);
                        }
                        mBinding.getModel().setPostType(2);
                        mBinding.audioVideoImgLayout.setVisibility(View.VISIBLE);
                    } else {
                        Utility.showToast(InstitutePostActivity.this, "Invalid Url.");
                    }
                }
            }
        });
        bottomSheetDialog.show();
    }

    private void getImageTitleFromUrl(final String url) {
        mBinding.getModel().setVideoUrl(url);
        ProgressDialog.getInstance().showProgressDialog(InstitutePostActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Connect to website
                    Document document = Jsoup.connect(url).get();

                    //Get the logo source of the website
                    Element img = document.select("img").first();
                    // Locate the src attribute
                    final String imgSrc = img.absUrl("src");
                    final String title = document.title();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialog.getInstance().dismissDialog();
                            mBinding.textAVTitle.setText(title);
                            mBinding.textAVUrl.setText(url);
                            aQuery.id(mBinding.audioVideoImg).image(imgSrc, true, true);
                            mBinding.getModel().setArticle_title(title);
                            mBinding.getModel().setArticle_thumbnail(imgSrc);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialog.getInstance().dismissDialog();
                            Utility.showToast(InstitutePostActivity.this, getString(R.string.wrong));
                        }
                    });
                }
            }
        }).start();
    }

    private void getImageTitleFromVideoUrl(final String videoURL) {
        mBinding.getModel().setVideoUrl(videoURL);
        try {
            if (videoURL.contains("www.youtube.com")) {
                String url = "https://img.youtube.com/vi/" + videoURL.split("\\=")[1] + "/0.jpg";
                Glide.with(InstitutePostActivity.this)
                        .load(url)
                        .into(mBinding.audioVideoImg);
                mBinding.textAVTitle.setText("YouTube");
                mBinding.textAVUrl.setText(videoURL);
                mBinding.getModel().setArticle_thumbnail(url);
                mBinding.getModel().setArticle_title("YouTube");
            } else {
                RichPreview richPreview = new RichPreview(new ResponseListener() {
                    @Override
                    public void onData(MetaData metaData) {
                        //Implement your Layout
                        getImageTitleFromVideoUrl(metaData.getUrl());
                    }

                    @Override
                    public void onError(Exception e) {
                        //handle error
                        e.printStackTrace();
                        Utility.showToast(InstitutePostActivity.this, "Invalid Url.");
                    }
                });
                richPreview.getPreview(videoURL);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            Utility.showToast(InstitutePostActivity.this, "Invalid Url.");
        }
    }

    /**
     * Open Audio recorder
     */
    private void recordAudio() {
        new Permissive.Request(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        if (permissions.length == 2) {
                            audioFilePath = Environment.getExternalStorageDirectory() + "/recorded_audio" + System
                                    .currentTimeMillis() + "" +
                                    ".mp3";
                            int color = getResources().getColor(R.color.colorPrimaryDark);
                            AndroidAudioRecorder.with(InstitutePostActivity.this)
                                    // Required
                                    .setFilePath(audioFilePath)
                                    .setColor(color)
                                    .setRequestCode(RECORD_AUDIO)
                                    .record();
                        }
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(InstitutePostActivity.this, "Need permission to open camera.");
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

    private void postAll(EdukitPostModel postModel) {
        if (postModel.getPostType() == 4) {
            postPoll(postModel);
            return;
        } else if (postModel.getPostType() == 2) {
            if (!TextUtils.isEmpty(postModel.getVideoUrl())) {
                postUrlType(postModel);
            } else {
                Utility.showToast(InstitutePostActivity.this, "Enter Url.");
            }
            return;
        } else if (postModel.getPostType() == 0) {
            postModel.setType("6");
            postModel.setPostType(6);
            postUrlType(postModel);
            return;
        } else if (postModel.getPostType() == 6) {
            postModel.setType("6");
            postUrlType(postModel);
            return;
        }
        if (postModel.getFile() == null) {
            Utility.showToast(InstitutePostActivity.this, "Select file.");
            return;
        }
        Api api = APIClient.getClient().create(Api.class);
        MultipartBody.Part part = postModel.prepareFilePart("file", postModel.getFile());

        Call<PostResponse> responseCall = api.post(postModel.getBoardPostBody(), part);
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
     * Post Poll method
     *
     * @param postModel @PostModel
     */
    public void postPoll(EdukitPostModel postModel) {
        if (validate(postModel)) {

            if (TextUtils.isEmpty(postModel.getPollQuestion())) {
                Utility.showToast(InstitutePostActivity.this, "Enter poll details.");
                return;
            } else if (TextUtils.isEmpty(postModel.getPollOption1())) {
                Utility.showToast(InstitutePostActivity.this, "Enter poll details.");
                return;
            } else if (TextUtils.isEmpty(postModel.getPollOption2())) {
                Utility.showToast(InstitutePostActivity.this, "Enter poll details.");
                return;
            }

            Api api = APIClient.getClient().create(Api.class);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(postModel.getPollOption1());
            jsonArray.put(postModel.getPollOption2());
            postModel.setPollOption(jsonArray.toString());

            Call<PostResponse> responseCall = api.post(postModel.getPostBody());
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
    }

    public void postUrlType(EdukitPostModel postModel) {
        Api api = APIClient.getClient().create(Api.class);

        Call<PostResponse> responseCall = api.post(postModel.getBoardPostBody());
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
