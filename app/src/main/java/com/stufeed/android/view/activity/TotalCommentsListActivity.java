package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAllCommentResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.databinding.ActivityCommentPostBinding;
import com.stufeed.android.databinding.ActivityTotalCommentsListBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.AllCommentsListAdapter;
import com.stufeed.android.view.adapter.CommentListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TotalCommentsListActivity extends AppCompatActivity {
    public static final String TAG_POST = "post";
    public static final String TAG_POSITION = "position";
    private ActivityTotalCommentsListBinding binding;
    private GetPostResponse.Post post;
    private ArrayList<GetAllCommentResponse.Comment> commentArrayList = new ArrayList<>();
    private UserDetail userDetail;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_total_comments_list);
        binding.setActivity(this);
        getDataFromBundle();
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getAllCommentList();

    }
    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        post = bundle.getParcelable(TAG_POST);
        position = bundle.getInt(TAG_POSITION);
        userDetail = Utility.getLoginUserDetail(TotalCommentsListActivity.this);
    }

    private void getAllCommentList() {
        Api api = APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(TotalCommentsListActivity.this);
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
                Utility.showErrorMsg(TotalCommentsListActivity.this);
            }
        });
    }

    private void handleGetAllCommentResponse(GetAllCommentResponse allCommentResponse) {
        if (allCommentResponse == null) {
            Utility.showErrorMsg(TotalCommentsListActivity.this);
        } else if (allCommentResponse.getResponseCode().equals(Api.SUCCESS)) {
            commentArrayList.clear();
            commentArrayList.addAll(allCommentResponse.getCommentArrayList());
            setCommentRecyclerView();
        } else {
            Utility.showToast(TotalCommentsListActivity.this, "No comment.");
        }
    }
    private void setCommentRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(TotalCommentsListActivity.this));
        AllCommentsListAdapter adapter = new AllCommentsListAdapter(TotalCommentsListActivity.this, commentArrayList);
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
