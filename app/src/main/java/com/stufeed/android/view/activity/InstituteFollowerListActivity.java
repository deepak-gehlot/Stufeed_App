package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAllInstituteFollowersListResponse;
import com.stufeed.android.databinding.ActivityInstituteFollowerListBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.InstituteFollowerListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class InstituteFollowerListActivity extends AppCompatActivity {
    ActivityInstituteFollowerListBinding mBinding;
    private String instituteUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_institute_follower_list);
        Bundle bundle = getIntent().getExtras();
        instituteUserId = bundle.getString("instituteId");

        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getInstituteFollowerList();
    }

    private void getInstituteFollowerList() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetAllInstituteFollowersListResponse> responseCall = api.getInstituteFollower(instituteUserId);
        responseCall.enqueue(new Callback<GetAllInstituteFollowersListResponse>() {
            @Override
            public void onResponse(Call<GetAllInstituteFollowersListResponse> call, retrofit2.Response<GetAllInstituteFollowersListResponse> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetAllInstituteFollowersListResponse> call, Throwable t) {
                handleResponse(null);
            }
        });


    }

    private void handleResponse(GetAllInstituteFollowersListResponse response) {
        mBinding.recyclerView.setVisibility(View.VISIBLE);
        mBinding.progressBar.setVisibility(View.GONE);
        if (response == null) {
            Utility.showErrorMsg(InstituteFollowerListActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(response.getFollowerArrayList());
        } else {
            Utility.showErrorMsg(InstituteFollowerListActivity.this);
        }
    }

    private void setRecyclerView(ArrayList<GetAllInstituteFollowersListResponse.InstituteFollower> followersLists) {

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(InstituteFollowerListActivity.this, 3));
        InstituteFollowerListAdapter adapter = new InstituteFollowerListAdapter(InstituteFollowerListActivity.this, followersLists);
        mBinding.recyclerView.setAdapter(adapter);
    }
}
