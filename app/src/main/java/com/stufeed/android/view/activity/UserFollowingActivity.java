package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetFollowerListResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.ActivityUserFollowingBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.UserFollowerListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class UserFollowingActivity extends AppCompatActivity {

    private String userId = "";
    private ActivityUserFollowingBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_following);
        getDatFromBundle();
        getFollowingList();
    }

    private void getDatFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Utility.showToast(UserFollowingActivity.this, getString(R.string.wrong));
            finish();
        } else {
            userId = bundle.getString("user_id");
        }
    }

    private void getFollowingList() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetFollowerListResponse> responseCall = api.getUserFollowers(userId);
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

    private void handleResponse(GetFollowerListResponse response) {
        if (response == null) {
            Utility.showToast(UserFollowingActivity.this, getString(R.string.wrong));
            finish();
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            if (response.getUserArrayList() != null && response.getUserArrayList().size() != 0) {
                mBinding.progressBar.setVisibility(View.GONE);
                mBinding.recyclerView.setVisibility(View.VISIBLE);
                setRecyclerView(response.getUserArrayList());
            } else {
                mBinding.progressBar.setVisibility(View.GONE);
                Utility.showToast(UserFollowingActivity.this, "No follower found.");
            }
        } else {
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.recyclerView.setVisibility(View.GONE);
            Utility.showToast(UserFollowingActivity.this, response.getResponseMessage());
        }
    }

    private void setRecyclerView(ArrayList<GetFollowerListResponse.User> userArrayList) {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(UserFollowingActivity.this, 3));
        UserFollowerListAdapter adapter = new UserFollowerListAdapter(UserFollowingActivity.this, userArrayList,
                userId);
        mBinding.recyclerView.setAdapter(adapter);
    }
}
