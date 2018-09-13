package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetFollowerListResponse;
import com.stufeed.android.api.response.GetFollowingListResponse;
import com.stufeed.android.databinding.ActivityUserFollowingBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.FollowerListAdapter;
import com.stufeed.android.view.adapter.UserFollowingListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class UserFollowingActivity extends AppCompatActivity {

    private String userId = "";
    private ActivityUserFollowingBinding mBinding;
    String loginUserid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_following);
        MobileAds.initialize(this,
                getString(R.string.ad_mob_id));
        getDatFromBundle();
        loginUserid = Utility.getLoginUserId(UserFollowingActivity.this);
        if (userId.equals(loginUserid)) {
            getLoginFollowerList();
        } else {
            getFollowerList();
        }
        // getFollowingList();

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    private void setRecyclerView(ArrayList<GetFollowingListResponse.User> userArrayList) {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(UserFollowingActivity.this, 3));
        UserFollowingListAdapter adapter = new UserFollowingListAdapter(UserFollowingActivity.this, userArrayList,
                userId);
        mBinding.recyclerView.setAdapter(adapter);
    }

    private void getFollowerList() {
        Api api = APIClient.getClient().create(Api.class);
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

    private void getLoginFollowerList() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetFollowerListResponse> responseCall = api.getLoginuserfollowers(userId, loginUserid);
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
            Utility.showErrorMsg(UserFollowingActivity.this);
        } else if (getFollowerListResponse.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerViews(getFollowerListResponse.getUserArrayList());
        } else {
            Utility.showErrorMsg(UserFollowingActivity.this);
        }
    }

    private void setRecyclerViews(ArrayList<GetFollowerListResponse.User> userArrayList) {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(UserFollowingActivity.this, 3));
        FollowerListAdapter adapter = new FollowerListAdapter(UserFollowingActivity.this, userArrayList);
        mBinding.recyclerView.setAdapter(adapter);
    }
}
