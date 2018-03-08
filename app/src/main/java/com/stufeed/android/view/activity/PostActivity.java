package com.stufeed.android.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.PostResponse;
import com.stufeed.android.databinding.ActivityPostBinding;
import com.stufeed.android.databinding.DialogPostSelectorBinding;
import com.stufeed.android.util.Extension;
import com.stufeed.android.util.MyMovementMethod;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.util.ValidationTemplate;
import com.stufeed.android.view.viewmodel.PostModel;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import okhttp3.MultipartBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {

    //Audio player variables
    private final int RECORD_AUDIO = 101;
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

        addDescriptionTextChangeListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECORD_AUDIO) {
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
        } else {
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
                        recordAudio();
                        break;
                    case R.id.pollButton:
                        binding.pollLayout.setVisibility(View.VISIBLE);
                        binding.getModel().setType(4);
                        break;
                }
            }
        };

        dialogBinding.cameraButton.setOnClickListener(onClickListener);
        dialogBinding.galleryButton.setOnClickListener(onClickListener);
        dialogBinding.documentButton.setOnClickListener(onClickListener);
        dialogBinding.pollButton.setOnClickListener(onClickListener);

        dialog.show();
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
                                    ".wav";
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
        if (TextUtils.isEmpty(postModel.getTitle()) ||
                TextUtils.isEmpty(postModel.getDescription())) {
            Utility.showToast(PostActivity.this, getString(R.string.all_required));
            return false;
        } else if (postModel.getType() == 5 && postModel.getFile() == null) {
            Utility.showToast(PostActivity.this, "Audio file not found.");
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
            if (postModel.getType() == 4) {
                postPoll(postModel);
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
