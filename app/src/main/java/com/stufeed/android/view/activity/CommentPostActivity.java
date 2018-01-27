package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.CommentResponse;
import com.stufeed.android.api.response.GetAllCommentResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.databinding.ActivityCommentPostBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.CommentListAdapter;
import com.stufeed.android.view.viewmodel.CommentModel;

import org.w3c.dom.Comment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentPostActivity extends AppCompatActivity {

    public static final String TAG_POST = "post";

    private ActivityCommentPostBinding binding;
    private GetPostResponse.Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_post);
        setSupportActionBar(binding.toolbar);
        binding.setActivity(this);
        CommentModel commentModel = new CommentModel();
        commentModel.setComment("");
        commentModel.setPostId(post.getPostId());
        commentModel.setUserId(Utility.getLoginUserDetail(CommentPostActivity.this).getUserId());
        binding.setModel(commentModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllCommentList();
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
        ProgressDialog.getInstance().showProgressDialog(CommentPostActivity.this);
        responseCall.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleCommentResponse(response.body());
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
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
            binding.getModel().setComment("");
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
            setCommentRecyclerView(allCommentResponse.getCommentArrayList());
        } else {
            Utility.showErrorMsg(CommentPostActivity.this);
        }
    }

    private void setCommentRecyclerView(ArrayList<GetAllCommentResponse.Comment> commentArrayList) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(CommentPostActivity.this));
        CommentListAdapter adapter = new CommentListAdapter(CommentPostActivity.this, commentArrayList);
        binding.recyclerView.setAdapter(adapter);
    }
}