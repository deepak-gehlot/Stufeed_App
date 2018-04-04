package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.CommentResponse;
import com.stufeed.android.api.response.GetAllCommentResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.databinding.ActivityCommentPostBinding;
import com.stufeed.android.util.Constant;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.CommentListAdapter;
import com.stufeed.android.view.viewmodel.CommentModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentPostActivity extends AppCompatActivity {

    public static final String TAG_POST = "post";
    public static final String TAG_POSITION = "position";

    private ActivityCommentPostBinding binding;
    private GetPostResponse.Post post;
    private ArrayList<GetAllCommentResponse.Comment> commentArrayList = new ArrayList<>();
    private UserDetail userDetail;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_post);
        setSupportActionBar(binding.toolbar);
        getDataFromBundle();
        binding.setActivity(this);
        CommentModel commentModel = new CommentModel();
        commentModel.setComment("");
        commentModel.setPostId(post.getPostId());
        commentModel.setUserId(Utility.getLoginUserDetail(CommentPostActivity.this).getUserId());
        binding.setModel(commentModel);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(TAG_POST, post);
        intent.putExtra(TAG_POSITION, position);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllCommentList();
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        post = bundle.getParcelable(TAG_POST);
        position = bundle.getInt(TAG_POSITION);
        userDetail = Utility.getLoginUserDetail(CommentPostActivity.this);
    }

    /**
     * Comment send button click method
     *
     * @param commentModel @CommentModel
     */
    public void onSendButtonClick(CommentModel commentModel) {
        Api api = APIClient.getClient().create(Api.class);
        Call<CommentResponse> responseCall = api.postComment(commentModel.getUserId(), commentModel.getPostId(),
                commentModel.getComment());
        addNewComment(binding.getModel());
        binding.getModel().setComment("");

        responseCall.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                handleCommentResponse(response.body());
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Utility.showErrorMsg(CommentPostActivity.this);
            }
        });
    }

    /**
     * Handle comment response method
     *
     * @param commentResponse {@link CommentResponse}
     */
    private void handleCommentResponse(CommentResponse commentResponse) {
        if (commentResponse == null) {
            Utility.showErrorMsg(CommentPostActivity.this);
        } else if (commentResponse.getResponseCode().equals(Api.SUCCESS)) {
        }
    }

    private void getAllCommentList() {
        Api api = APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(CommentPostActivity.this);
        Call<GetAllCommentResponse> responseCall = api.getAllComment(post.getPostId());
        responseCall.enqueue(new Callback<GetAllCommentResponse>() {
            @Override
            public void onResponse(Call<GetAllCommentResponse> call, Response<GetAllCommentResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleGetAllCommentResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetAllCommentResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(CommentPostActivity.this);
            }
        });
    }

    private void handleGetAllCommentResponse(GetAllCommentResponse allCommentResponse) {
        if (allCommentResponse == null) {
            Utility.showErrorMsg(CommentPostActivity.this);
        } else if (allCommentResponse.getResponseCode().equals(Api.SUCCESS)) {
            commentArrayList.clear();
            commentArrayList.addAll(allCommentResponse.getCommentArrayList());
            setCommentRecyclerView();
        } else {
            Utility.showToast(CommentPostActivity.this, "No comment.");
        }
    }

    private void addNewComment(CommentModel commentModel) {
        if (!TextUtils.isEmpty(post.getTotalComment())) {
            post.setTotalComment("" + (Integer.parseInt(post.getTotalComment()) + 1));
        }
        GetAllCommentResponse.Comment comment = new GetAllCommentResponse.Comment();
        comment.setComment(commentModel.getComment());
        comment.setUserId(commentModel.getUserId());
        comment.setEmail(userDetail.getEmail());
        comment.setFullName(userDetail.getFullName());
        comment.setPostId(commentModel.getPostId());
        comment.setProfilePic(userDetail.getProfilePic());
        comment.setDateTime(new SimpleDateFormat(Constant.FORMAT_DATE_TIME, Locale.US).format(new Date()));
        commentArrayList.add(comment);
        if (binding.recyclerView.getAdapter() != null) {
            binding.recyclerView.getAdapter().notifyDataSetChanged();
            scrollToBottom();
        } else {
            setCommentRecyclerView();
        }
    }

    private void setCommentRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(CommentPostActivity.this));
        CommentListAdapter adapter = new CommentListAdapter(CommentPostActivity.this, commentArrayList);
        binding.recyclerView.setAdapter(adapter);
        scrollToBottom();
    }

    private void scrollToBottom() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.smoothScrollToPosition(commentArrayList.size() - 1);
            }
        }, 100);

    }
}