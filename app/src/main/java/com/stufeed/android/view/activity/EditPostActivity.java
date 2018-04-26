package com.stufeed.android.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.PostResponse;
import com.stufeed.android.databinding.ActivityEditPostBinding;
import com.stufeed.android.util.Extension;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.util.ValidationTemplate;
import com.stufeed.android.view.viewmodel.EditPostModel;
import com.stufeed.android.view.viewmodel.PostModel;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostActivity extends AppCompatActivity {

    private ActivityEditPostBinding mBinding;
    private GetPostResponse.Post post;
    private EditPostModel editPostModel;
    private String settingStr[] = new String[]{"Allow Comment", "Allow Repost"};
    private boolean settingBool[] = {true, true};
    private final int SELECT_BOARD = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_post);
        getDataFromBundle();
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
        if (requestCode == SELECT_BOARD) {
            switch (resultCode) {
                case RESULT_OK:
                    EditPostModel postModel = mBinding.getModel();
                    String boardId = data.getStringExtra("board_id");
                    postModel.setBoardId(boardId);
                    postAll(postModel);
                    break;
                case RESULT_CANCELED:
                    Utility.showToast(EditPostActivity.this, "Selection canceled.");
                    break;
            }
        }
    }

    /**
     * Get item from bundle
     */
    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            post = bundle.getParcelable("item");
            editPostModel = new EditPostModel(post.getUserId(), post.getPostId(), post.getBoardId(), post.getTitle(), post.getDescription(), post.getAllowComment(), post.getAllowRePost());

            if (editPostModel.getAllowComment().equals("1")) {
                settingBool[0] = true;
            }

            if (editPostModel.getAllowRepost().equals("1")) {
                settingBool[1] = true;
            }

            mBinding.setModel(editPostModel);
        } else {
            finish();
        }
    }

    /**
     * Show setting dialog
     */
    public void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
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
                                mBinding.getModel().setAllowComment("1");
                            } else {
                                mBinding.getModel().setAllowComment("0");
                            }
                            break;
                        case 1:
                            if (checked) {
                                mBinding.getModel().setAllowRepost("1");
                            } else {
                                mBinding.getModel().setAllowRepost("0");
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
     * Validate post
     *
     * @param postModel @PostModel
     * @return true if valid else false
     */
    private boolean validatePost(EditPostModel postModel) {
        Extension extension = Extension.getInstance();
        if (TextUtils.isEmpty(postModel.getTitle()) ||
                TextUtils.isEmpty(postModel.getDescription())) {
            Utility.showToast(EditPostActivity.this, getString(R.string.all_required));
            return false;
        } else if (!extension.executeStrategy(EditPostActivity.this, "", ValidationTemplate.INTERNET)) {
            Utility.setNoInternetPopup(EditPostActivity.this);
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
    public void onPostButtonClick(final EditPostModel postModel) {
        if (validatePost(postModel)) {
            Intent intent = new Intent(EditPostActivity.this, BoardSelectionActivity.class);
            startActivityForResult(intent, SELECT_BOARD);
        }
    }

    private void postAll(EditPostModel postModel) {
        Api api = APIClient.getClient().create(Api.class);

        Call<PostResponse> responseCall = api.editPost(postModel.getUserId(), postModel.getPostId(), postModel.getBoardId(), postModel.getTitle(), postModel.getDescription(), postModel.getAllowComment(), post.getAllowRePost());
        ProgressDialog.getInstance().showProgressDialog(EditPostActivity.this);
        responseCall.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handlePostResponse(response.body());
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(EditPostActivity.this);
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
                Utility.showToast(EditPostActivity.this, "Edit Pos Successfully.");
                setResult(RESULT_OK);
                finish();
            } else {
                Utility.showToast(EditPostActivity.this, postResponse.getResponseMessage());
            }
        } else {
            Utility.showErrorMsg(EditPostActivity.this);
        }
    }
}