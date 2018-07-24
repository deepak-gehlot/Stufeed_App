package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetJoinBoardRequestResponse;
import com.stufeed.android.api.response.UnVerifyUserListResponse;
import com.stufeed.android.databinding.ActivityVerificationRequestBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.NotificationListAdapter;
import com.stufeed.android.view.adapter.VerificationListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationRequestActivity extends AppCompatActivity {

    private ActivityVerificationRequestBinding mBinding;
    private String collegeId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_verification_request);

        collegeId = Utility.getLoginUserDetail(this).getCollegeId();

        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getUnVerifyUsers();
    }

    private void setRecyclerView(ArrayList<UnVerifyUserListResponse.Data> dataArrayList) {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VerificationListAdapter adapter = new VerificationListAdapter(this, dataArrayList);
        mBinding.recyclerView.setAdapter(adapter);
    }

    private void getUnVerifyUsers() {
        ProgressDialog.getInstance().showProgressDialog(VerificationRequestActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<UnVerifyUserListResponse> call = api.getUnVerifyUsers(collegeId);
        call.enqueue(new Callback<UnVerifyUserListResponse>() {
            @Override
            public void onResponse(Call<UnVerifyUserListResponse> call, Response<UnVerifyUserListResponse> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<UnVerifyUserListResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(UnVerifyUserListResponse response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response != null) {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                setRecyclerView(response.getData());
            }
        } else {
            Utility.showToast(VerificationRequestActivity.this, getString(R.string.wrong));
        }
    }

}
