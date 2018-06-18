package com.stufeed.android.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.util.Linkify;
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
import com.stufeed.android.api.response.PostResponse;
import com.stufeed.android.databinding.ActivityPostBinding;
import com.stufeed.android.databinding.DialogInputBinding;
import com.stufeed.android.databinding.DialogPostSelectorBinding;
import com.stufeed.android.util.Extension;
import com.stufeed.android.util.MyMovementMethod;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.util.ValidationTemplate;
import com.stufeed.android.view.viewmodel.PostModel;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {

    //Audio player variables
    private final int RECORD_AUDIO = 101;
    private final int SELECT_BOARD = 102;
    Handler seekHandler = new Handler();
    private ActivityPostBinding binding;
    private String settingStr[] = new String[]{"Allow Comment", "Allow Repost"};
    private boolean settingBool[] = {true, true};
    private AQuery aQuery;
    private String audioFilePath = "";
    private MediaPlayer mp;
    Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_post);
        MobileAds.initialize(this,
                getString(R.string.ad_mob_id));

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

        addDescriptionTextChangeListener();

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerConst.REQUEST_CODE_DOC) {
            switch (resultCode) {
                case RESULT_OK:
                    File file = new File(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0));
                    binding.docImgLayout.setVisibility(View.VISIBLE);
                    binding.docTitle.setText(file.getName());
                    binding.getModel().setFile(file);
                    binding.getModel().setType(1);
                    String ext = FilenameUtils.getExtension(file.getPath());
                    setFileAsExtension(ext);
                    break;
                case RESULT_CANCELED:
                    Utility.showToast(PostActivity.this, "Recording canceled.");
                    break;
            }
        } else if (requestCode == RECORD_AUDIO) {
            switch (resultCode) {
                case RESULT_OK:
                    binding.audioCardLayout.setVisibility(View.VISIBLE);
                    File file = new File(audioFilePath);
                    String fileName = file.getName();
                    binding.audioText.setText(fileName);
                    audioPlayer();
                    binding.getModel().setType(5);
                    binding.getModel().setFile(file);
                    break;
                case RESULT_CANCELED:
                    Utility.showToast(PostActivity.this, "Recording canceled.");
                    break;
            }
        } else if (requestCode == SELECT_BOARD) {
            switch (resultCode) {
                case RESULT_OK:
                    PostModel postModel = binding.getModel();
                    String boardId = data.getStringExtra("board_id");
                    postModel.setBoardId(boardId);
                    postAll(postModel);
                    break;
                case RESULT_CANCELED:
                    Utility.showToast(PostActivity.this, "Selection canceled.");
                    break;
            }
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                    try {
                        File file = imageFiles.get(0);
                        file = new Compressor(PostActivity.this).compressToFile(file);
                        binding.selectedImgLayout.setVisibility(View.VISIBLE);
                        aQuery.id(binding.selectedImg).image(file, 300);
                        binding.getModel().setFile(file);
                        binding.getModel().setType(3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void setFileAsExtension(String extension) {
        switch (extension) {
            case "pdf":
                binding.docImg.setImageResource(R.drawable.icon_file_pdf);
                break;
            case "doc":
            case "docx":
                binding.docImg.setImageResource(R.drawable.icon_file_doc);
                break;
            case "xlsx":
            case "xls":
                binding.docImg.setImageResource(R.drawable.icon_file_xls);
                break;
            case "txt":
                binding.docImg.setImageResource(R.drawable.icon_file_unknown);
                break;
            case "ppt":
                binding.docImg.setImageResource(R.drawable.icon_file_ppt);
                break;
            default:
                binding.docImg.setImageResource(R.drawable.icon_file_unknown);
        }
    }

    public void onCloseClick(int type) {
        switch (type) {
            case 1: // document
                binding.docImgLayout.setVisibility(View.GONE);
                binding.getModel().setFile(null);
                binding.getModel().setType(0);
                break;
            case 2: // audio/ video url
                binding.audioVideoImgLayout.setVisibility(View.GONE);
                binding.getModel().setFile(null);
                binding.getModel().setType(0);
                break;
            case 3: // image
                binding.selectedImgLayout.setVisibility(View.GONE);
                binding.getModel().setFile(null);
                binding.getModel().setType(0);
                break;
            case 4: // image
                binding.pollLayout.setVisibility(View.GONE);
                binding.getModel().setFile(null);
                binding.getModel().setType(0);
                break;
            case 5: // image
                binding.audioCardLayout.setVisibility(View.GONE);
                binding.getModel().setFile(null);
                binding.getModel().setType(0);
                break;
            case 6: // Remove all type
                binding.docImgLayout.setVisibility(View.GONE);
                binding.audioVideoImgLayout.setVisibility(View.GONE);
                binding.selectedImgLayout.setVisibility(View.GONE);
                binding.pollLayout.setVisibility(View.GONE);
                binding.audioCardLayout.setVisibility(View.GONE);
                binding.getModel().setFile(null);
                binding.getModel().setType(0);
                break;
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
            binding.audioSeekBar.setMax(mp.getDuration());
            if (mp.isPlaying()) {
                mp.pause();
                seekHandler.removeCallbacks(run);
                binding.audioStartStopImg.setImageResource(R.drawable.ic_video_play);
            } else {
                mp.start();
                binding.audioStartStopImg.setImageResource(R.drawable.ic_video_pause);
                seekUpdate();
            }
        }
    }

    /**
     * Handle Audio Player seek bar
     */
    public void seekUpdate() {
        binding.audioSeekBar.setProgress(mp.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }

    /**
     * Description EditText text change listener
     */
    private void addDescriptionTextChangeListener() {
        binding.edtDescription.setLinksClickable(true);
        binding.edtDescription.setAutoLinkMask(Linkify.WEB_URLS);
        binding.edtDescription.setMovementMethod(MyMovementMethod.getInstance());
        binding.edtDescription.addTextChangedListener(new TextWatcher() {
            String videoURL = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = binding.edtDescription.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    ArrayList<String> arrayList = Utility.extractUrls(content);
                    if (arrayList.size() != 0) {
                        videoURL = arrayList.get(0);
                        if (Utility.isValidUrl(videoURL)) {
                            Linkify.addLinks(s, Linkify.WEB_URLS);
                            try {
                                if (videoURL.contains("www.youtube.com")) {
                                    String url = "https://img.youtube.com/vi/" + videoURL.split("\\=")[1] + "/0.jpg";
                                    Glide.with(PostActivity.this)
                                            .load(url)
                                            .into(binding.videoImg);
                                } else {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Bitmap bitmap = Utility.retriveVideoFrameFromVideo(videoURL);
                                                Glide.with(PostActivity.this)
                                                        .load(bitmap)
                                                        .into(binding.videoImg);
                                            } catch (Throwable throwable) {
                                                throwable.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    }
                }
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
        final BottomSheetDialog dialog = new BottomSheetDialog(PostActivity.this);
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
                        binding.pollLayout.setVisibility(View.VISIBLE);
                        binding.getModel().setType(4);
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
                                .pickFile(PostActivity.this);
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(PostActivity.this, "Need permission to open gallery.");
            }
        }).execute(PostActivity.this);
    }

    public void showLinkDialog(final int type) {
        final DialogInputBinding inputBinding = DataBindingUtil.inflate(LayoutInflater.from(PostActivity.this), R
                .layout.dialog_input, null, false);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PostActivity.this);
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
                        binding.getModel().setType(2);
                        binding.audioVideoImgLayout.setVisibility(View.VISIBLE);
                    } else {
                        Utility.showToast(PostActivity.this, "Invalid Url.");
                    }
                }
            }
        });
        bottomSheetDialog.show();
    }

    private void getImageTitleFromUrl(final String url) {
        binding.getModel().setVideoUrl(url);
        ProgressDialog.getInstance().showProgressDialog(PostActivity.this);
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
                    InputStream input = new java.net.URL(imgSrc).openStream();
                    // Decode Bitmap
                    final Bitmap bitmap = BitmapFactory.decodeStream(input);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialog.getInstance().dismissDialog();
                            binding.textAVTitle.setText(title);
                            binding.textAVUrl.setText(url);
                            aQuery.id(binding.audioVideoImg).image(imgSrc, true, true);
                            binding.getModel().setArticle_title(title);
                            binding.getModel().setArticle_thumbnail(imgSrc);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialog.getInstance().dismissDialog();
                            Utility.showToast(PostActivity.this, getString(R.string.wrong));
                        }
                    });
                }
            }
        }).start();
    }

    private void getImageTitleFromVideoUrl(final String videoURL) {
        binding.getModel().setVideoUrl(videoURL);
        try {
            if (videoURL.contains("www.youtube.com")) {
                String url = "https://img.youtube.com/vi/" + videoURL.split("\\=")[1] + "/0.jpg";
                Glide.with(PostActivity.this)
                        .load(url)
                        .into(binding.audioVideoImg);
                binding.textAVTitle.setText("YouTube");
                binding.textAVUrl.setText(videoURL);
                binding.getModel().setArticle_thumbnail(url);
                binding.getModel().setArticle_title("YouTube");
            } else {
                Utility.showToast(PostActivity.this, "Invalid Url.");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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
                            AndroidAudioRecorder.with(PostActivity.this)
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
                Utility.showToast(PostActivity.this, "Need permission to open camera.");
            }
        }).execute(PostActivity.this);
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
        if (TextUtils.isEmpty(postModel.getDescription())) {
            Utility.showToast(PostActivity.this, getString(R.string.all_required));
            return false;
        } else if (postModel.getType() == 5 && postModel.getFile() == null) {
            Utility.showToast(PostActivity.this, "Audio file not found.");
            return false;
        } else if (postModel.getType() == 0) {
            Utility.showToast(PostActivity.this, "Select any post type.");
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
    public void onPostButtonClick(final PostModel postModel) {
        if (validatePost(postModel)) {
            Intent intent = new Intent(PostActivity.this, BoardSelectionActivity.class);
            startActivityForResult(intent, SELECT_BOARD);

        }
    }

    private void postAll(PostModel postModel) {
        if (postModel.getType() == 4) {
            postPoll(postModel);
            return;
        } else if (postModel.getType() == 2) {
            postUrlType(postModel);
            return;
        }
        if (postModel.getFile() == null) {
            Utility.showToast(PostActivity.this, "Select file.");
            return;
        }
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

    /**
     * Post Poll method
     *
     * @param postModel @PostModel
     */
    public void postPoll(PostModel postModel) {
        if (validatePost(postModel)) {

            if (TextUtils.isEmpty(postModel.getPollQuestion())) {
                Utility.showToast(PostActivity.this, "Enter poll details.");
                return;
            } else if (TextUtils.isEmpty(postModel.getPollOption1())) {
                Utility.showToast(PostActivity.this, "Enter poll details.");
                return;
            } else if (TextUtils.isEmpty(postModel.getPollOption2())) {
                Utility.showToast(PostActivity.this, "Enter poll details.");
                return;
            }

            Api api = APIClient.getClient().create(Api.class);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(postModel.getPollOption1());
            jsonArray.put(postModel.getPollOption2());
            postModel.setPollOption(jsonArray.toString());

            Call<PostResponse> responseCall = api.post(postModel.getPostBody());
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

    public void postUrlType(PostModel postModel) {
        if (!TextUtils.isEmpty(postModel.getVideoUrl())) {
            Api api = APIClient.getClient().create(Api.class);

            Call<PostResponse> responseCall = api.post(postModel.getPostBody());
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
//https://medium.com/@princessdharmy/getting-started-with-jsoup-in-android-594e89dc891f

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
