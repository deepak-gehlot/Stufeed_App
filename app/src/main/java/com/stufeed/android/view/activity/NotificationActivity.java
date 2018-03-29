package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetJoinBoardRequestResponse;
import com.stufeed.android.databinding.ActivityNotificationBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.NotificationListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    private String mLoginUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        mLoginUserId = Utility.getLoginUserId(this);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getJoinBoardList();
    }

    private void getJoinBoardList() {
        ProgressDialog.getInstance().showProgressDialog(NotificationActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetJoinBoardRequestResponse> responseCall = api.getJoinBoardRequestList(mLoginUserId);
        responseCall.enqueue(new Callback<GetJoinBoardRequestResponse>() {
            @Override
            public void onResponse(Call<GetJoinBoardRequestResponse> call,
                                   retrofit2.Response<GetJoinBoardRequestResponse> response) {

            }

            @Override
            public void onFailure(Call<GetJoinBoardRequestResponse> call, Throwable t) {

            }
        });
    }

    private void handleResponse(GetJoinBoardRequestResponse response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response == null) {
            Utility.showErrorMsg(NotificationActivity.this);
            return;
        } else if (response.getResponseCode().equalsIgnoreCase(Api.SUCCESS)) {
            if (response.getRequestArrayList() != null) {
                setRecyclerView(response.getRequestArrayList());
            }
        } else {
            Utility.showToast(NotificationActivity.this, response.getResponseMessage());
        }
    }

    private void setRecyclerView(ArrayList<GetJoinBoardRequestResponse.Request> requestArrayList) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NotificationListAdapter adapter = new NotificationListAdapter(NotificationActivity.this, requestArrayList);
        binding.recyclerView.setAdapter(adapter);
    }
}