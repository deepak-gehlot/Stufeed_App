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
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.GetSavedPostResponse;
import com.stufeed.android.databinding.ActivityMyBookmarkListBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.FeedListAdapter;
import com.stufeed.android.view.adapter.SavedFeedListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookmarkListActivity extends AppCompatActivity {

    private ActivityMyBookmarkListBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_bookmark_list);
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSavedPostList();
    }

    /**
     * Get User saved post list
     */
    private void getSavedPostList() {
        mBinding.container.progressBar.setVisibility(View.VISIBLE);
        mBinding.container.msgTxt.setVisibility(View.GONE);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetSavedPostResponse> responseCall = api.getSavedPost(
                Utility.getLoginUserId(MyBookmarkListActivity.this));
        responseCall.enqueue(new Callback<GetSavedPostResponse>() {
            @Override
            public void onResponse(Call<GetSavedPostResponse> call, Response<GetSavedPostResponse> response) {
                mBinding.container.progressBar.setVisibility(View.GONE);
                mBinding.container.pullToRefresh.setRefreshing(false);
                handleGetAllPostResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetSavedPostResponse> call, Throwable t) {
                mBinding.container.progressBar.setVisibility(View.GONE);
                mBinding.container.msgTxt.setVisibility(View.VISIBLE);
                mBinding.container.msgTxt.setText(getString(R.string.wrong));
                mBinding.container.pullToRefresh.setRefreshing(false);
                Utility.showToast(MyBookmarkListActivity.this, getString(R.string.wrong));
            }
        });
    }


    private void handleGetAllPostResponse(GetSavedPostResponse getPostResponse) {
        if (getPostResponse != null) {
            if (getPostResponse.getResponseCode().equals(Api.SUCCESS)) {
                setRecyclerView(getPostResponse.getPost());
            } else {
                mBinding.container.msgTxt.setVisibility(View.VISIBLE);
                mBinding.container.msgTxt.setText("No post found.");
                Utility.showToast(MyBookmarkListActivity.this, "No post found.");
            }
        } else {
            mBinding.container.msgTxt.setVisibility(View.VISIBLE);
            mBinding.container.msgTxt.setText(getString(R.string.wrong));
            Utility.showErrorMsg(MyBookmarkListActivity.this);
        }
    }

    private void setRecyclerView(ArrayList<GetSavedPostResponse.Post> postArrayList) {
        mBinding.container.progressBar.setVisibility(View.GONE);
        mBinding.container.msgTxt.setVisibility(View.GONE);
        mBinding.container.recyclerView.setVisibility(View.VISIBLE);
        mBinding.container.recyclerView.setLayoutManager(new LinearLayoutManager(MyBookmarkListActivity.this));
        SavedFeedListAdapter adapter = new SavedFeedListAdapter(MyBookmarkListActivity.this, postArrayList);
        mBinding.container.recyclerView.setAdapter(adapter);
    }
}