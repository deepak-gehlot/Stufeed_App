package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetJoinBoardRequestResponse;
import com.stufeed.android.databinding.ActivityJoinRequestBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.NotificationListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class JoinRequestActivity extends AppCompatActivity {

    private ActivityJoinRequestBinding mBinding;
    private String mLoginUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_join_request);
        mLoginUserId = Utility.getLoginUserId(this);
        getJoinBoardList();
    }

    private void setRecyclerView(ArrayList<GetJoinBoardRequestResponse.Request> requestArrayList) {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NotificationListAdapter adapter = new NotificationListAdapter(JoinRequestActivity.this, requestArrayList);
        mBinding.recyclerView.setAdapter(adapter);
    }

    private void getJoinBoardList() {
        ProgressDialog.getInstance().showProgressDialog(JoinRequestActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetJoinBoardRequestResponse> responseCall = api.getJoinBoardRequestList(mLoginUserId);
        responseCall.enqueue(new Callback<GetJoinBoardRequestResponse>() {
            @Override
            public void onResponse(Call<GetJoinBoardRequestResponse> call,
                                   retrofit2.Response<GetJoinBoardRequestResponse> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetJoinBoardRequestResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(GetJoinBoardRequestResponse response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response == null) {
            Utility.showErrorMsg(JoinRequestActivity.this);
            return;
        } else if (response.getResponseCode().equalsIgnoreCase(Api.SUCCESS)) {
            if (response.getRequestArrayList() != null) {
                setRecyclerView(response.getRequestArrayList());
            }
        } else {
            Utility.showToast(JoinRequestActivity.this, response.getResponseMessage());
        }
    }
}
