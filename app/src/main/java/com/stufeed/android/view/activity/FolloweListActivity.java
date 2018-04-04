package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetFollowerListResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.ActivityFolloweListBinding;

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

            }

            @Override
            public void onFailure(Call<GetFollowerListResponse> call, Throwable t) {

            }
        });

    }
}
