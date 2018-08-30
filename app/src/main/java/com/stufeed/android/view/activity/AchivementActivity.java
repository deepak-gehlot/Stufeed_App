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
import com.stufeed.android.api.response.GetAchievementListResponse;
import com.stufeed.android.databinding.AchievementListBinding;
import com.stufeed.android.databinding.ActivityAchivementBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.AchivementListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchivementActivity extends AppCompatActivity {

    private AchievementListBinding mBinding;
    private String mLoginUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.achievement_list);
        mBinding.setActivity(this);
        mLoginUserId = Utility.getLoginUserId(this);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAchievementList();
    }

    public void onClickAddButton() {
        Intent intent = new Intent(AchivementActivity.this, AddAchievementActivity.class);
        intent.putExtra("from", "0");
        startActivity(intent);
    }

    private void getAchievementList() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetAchievementListResponse> responseCall = api.getAllAchievements(mLoginUserId);
        responseCall.enqueue(new Callback<GetAchievementListResponse>() {
            @Override
            public void onResponse(Call<GetAchievementListResponse> call, Response<GetAchievementListResponse>
                    response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetAchievementListResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(GetAchievementListResponse response) {
        if (response == null) {
            Utility.showErrorMsg(AchivementActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(response.getAchievementArrayList());
        } else {
            Utility.showToast(AchivementActivity.this, response.getResponseMessage());
        }
    }

    private void setRecyclerView(ArrayList<GetAchievementListResponse.Achievement> achievementArrayList) {
        if (achievementArrayList != null) {
            AchivementListAdapter adapter = new AchivementListAdapter(AchivementActivity.this, achievementArrayList);
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(AchivementActivity.this));
            mBinding.recyclerView.setAdapter(adapter);
        } else {
            Utility.showToast(AchivementActivity.this, getString(R.string.wrong));
        }
    }
}