package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetFollowerListResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.ActivityFolloweListBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.FollowerListAdapter;
import com.stufeed.android.view.adapter.UserFollowerListAdapter;

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

        getFollowerList();
    }


    private void getFollowerList() {
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