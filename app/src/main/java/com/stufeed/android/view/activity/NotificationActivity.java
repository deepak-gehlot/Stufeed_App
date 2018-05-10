package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetJoinBoardRequestResponse;
import com.stufeed.android.api.response.GetNotificationResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.ActivityNotificationBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.NotificationListAdapter;
import com.stufeed.android.view.adapter.NotificationListItemAdapter;

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
        getRequestList();

        binding.layoutRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestClick();
            }
        });
    }

    public void onRequestClick() {
        startActivity(new Intent(this, JoinRequestActivity.class));
    }

    private void setRecyclerView(ArrayList<GetNotificationResponse.NotiItem> notiItems) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NotificationListItemAdapter adapter = new NotificationListItemAdapter(NotificationActivity.this, notiItems);
        binding.recyclerView.setAdapter(adapter);
    }

    private void getRequestList() {
        ProgressDialog.getInstance().showProgressDialog(NotificationActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetNotificationResponse> responseCall = api.getUserNotification(mLoginUserId);
        responseCall.enqueue(new Callback<GetNotificationResponse>() {
            @Override
            public void onResponse(Call<GetNotificationResponse> call, retrofit2.Response<GetNotificationResponse> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetNotificationResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(GetNotificationResponse response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response == null) {
            Utility.showToast(NotificationActivity.this, getString(R.string.wrong));
        } else {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                if (response.getCount() > 0) {
                    binding.textViewCount.setText("" + response.getCount());
                }
                setRecyclerView(response.getNotiItems());
            } else {
                Utility.showToast(NotificationActivity.this, getString(R.string.wrong));
            }
        }
    }
}