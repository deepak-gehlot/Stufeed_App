package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.BlockedUserListResponse;
import com.stufeed.android.databinding.ActivityBlockedUserListBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.BlockedListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class BlockedUserListActivity extends AppCompatActivity {

    private ActivityBlockedUserListBinding mBinding;
    private String mLoginUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_blocked_user_list);

        init();
    }

    private void init() {
        mLoginUserId = Utility.getLoginUserId(this);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getBlockUserList();
    }

    private void setRecyclerView(ArrayList<BlockedUserListResponse.User> userArrayList) {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(BlockedUserListActivity.this));
        BlockedListAdapter adapter = new BlockedListAdapter(BlockedUserListActivity.this, userArrayList);
        mBinding.recyclerView.setAdapter(adapter);
    }

    private void getBlockUserList() {
        ProgressDialog.getInstance().showProgressDialog(BlockedUserListActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<BlockedUserListResponse> responseCall = api.blockedUserList(mLoginUserId);
        responseCall.enqueue(new Callback<BlockedUserListResponse>() {
            @Override
            public void onResponse(Call<BlockedUserListResponse> call, retrofit2.Response<BlockedUserListResponse>
                    response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<BlockedUserListResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(BlockedUserListResponse response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response == null) {
            Utility.showErrorMsg(BlockedUserListActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            if (response.getUserArrayList() != null) {
                setRecyclerView(response.getUserArrayList());
            }
        } else {
            Utility.showToast(BlockedUserListActivity.this, response.getResponseMessage());
        }
    }
}