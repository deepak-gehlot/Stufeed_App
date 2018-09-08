package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetFollowerListResponse;
import com.stufeed.android.api.response.GetFollowingListResponse;
import com.stufeed.android.databinding.ActivityFolloweListBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.FollowerListAdapter;
import com.stufeed.android.view.adapter.UserFollowingListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class FolloweListActivity extends AppCompatActivity {

    private ActivityFolloweListBinding mBinding;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_followe_list);
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("user_id");
        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getFollowingList();
        //getFollowerList();
    }


    private void getFollowingList() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetFollowingListResponse> responseCall = api.getUserFollowing(userId);
        responseCall.enqueue(new Callback<GetFollowingListResponse>() {
            @Override
            public void onResponse(Call<GetFollowingListResponse> call, retrofit2.Response<GetFollowingListResponse>
                    response) {
                handleResponses(response.body());
            }

            @Override
            public void onFailure(Call<GetFollowingListResponse> call, Throwable t) {
                handleResponses(null);
            }
        });
    }

    private void handleResponses(GetFollowingListResponse response) {
        if (response == null) {
            Utility.showToast(FolloweListActivity.this, getString(R.string.wrong));
            finish();
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            if (response.getUserArrayList() != null && response.getUserArrayList().size() != 0) {
                mBinding.progressBar.setVisibility(View.GONE);
                mBinding.recyclerView.setVisibility(View.VISIBLE);
                setRecyclerViews(response.getUserArrayList());
            } else {
                mBinding.progressBar.setVisibility(View.GONE);
                Utility.showToast(FolloweListActivity.this, "No follower found.");
            }
        } else {
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.recyclerView.setVisibility(View.GONE);
            Utility.showToast(FolloweListActivity.this, response.getResponseMessage());
        }
    }

    private void setRecyclerViews(ArrayList<GetFollowingListResponse.User> userArrayList) {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(FolloweListActivity.this, 3));
        UserFollowingListAdapter adapter = new UserFollowingListAdapter(FolloweListActivity.this, userArrayList,
                userId);
        mBinding.recyclerView.setAdapter(adapter);
    }

    private void getFollowerList() {
        Api api = APIClient.getClient().create(Api.class);
        String loginUserid = Utility.getLoginUserId(FolloweListActivity.this);
        Call<GetFollowerListResponse> responseCall = api.getUserFollowers(userId, loginUserid);
        responseCall.enqueue(new Callback<GetFollowerListResponse>() {
            @Override
            public void onResponse(Call<GetFollowerListResponse> call, retrofit2.Response<GetFollowerListResponse>
                    response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetFollowerListResponse> call, Throwable t) {
                handleResponse(null);
            }
        });

    }

    private void handleResponse(GetFollowerListResponse getFollowerListResponse) {
        mBinding.recyclerView.setVisibility(View.VISIBLE);
        mBinding.progressBar.setVisibility(View.GONE);
        if (getFollowerListResponse == null) {
            Utility.showErrorMsg(FolloweListActivity.this);
        } else if (getFollowerListResponse.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(getFollowerListResponse.getUserArrayList());
        } else {
            Utility.showErrorMsg(FolloweListActivity.this);
        }
    }

    private void setRecyclerView(ArrayList<GetFollowerListResponse.User> userArrayList) {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(FolloweListActivity.this, 3));
        FollowerListAdapter adapter = new FollowerListAdapter(FolloweListActivity.this, userArrayList);
        mBinding.recyclerView.setAdapter(adapter);
    }
}