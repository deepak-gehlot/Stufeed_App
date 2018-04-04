package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.databinding.ActivityUsersPostBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.FeedListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersPostActivity extends AppCompatActivity {

    private ActivityUsersPostBinding mBinding;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_users_post);
        init();
    }

    private void init() {
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("user_id");
            getPostList();
        } else {
            Utility.showToast(UsersPostActivity.this, getString(R.string.wrong));
            onBackPressed();
        }
    }

    private void getPostList() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetPostResponse> responseCall = api.getUserAllPost(userId);
        responseCall.enqueue(new Callback<GetPostResponse>() {
            @Override
            public void onResponse(Call<GetPostResponse> call, Response<GetPostResponse> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetPostResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(GetPostResponse getPostResponse) {
        if (getPostResponse == null) {
            Utility.showErrorMsg(UsersPostActivity.this);
        } else if (getPostResponse.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(getPostResponse.getPost());
        } else {
            Utility.showToast(UsersPostActivity.this, getPostResponse.getResponseMessage());
        }
    }

    private void setRecyclerView(ArrayList<GetPostResponse.Post> postArrayList) {
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.msgTxt.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(UsersPostActivity.this));
     //   FeedListAdapter adapter = new FeedListAdapter(UsersPostActivity.this, postArrayList);
     //   mBinding.recyclerView.setAdapter(adapter);
    }
}