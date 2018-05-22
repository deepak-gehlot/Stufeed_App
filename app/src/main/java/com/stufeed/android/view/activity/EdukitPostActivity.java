package com.stufeed.android.view.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetEdukitResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.ActivityEdukitPostBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.EdukitPostAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class EdukitPostActivity extends AppCompatActivity {

    private ActivityEdukitPostBinding mBinding;
    private String mCollegeId = "";
    private String edukitId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edukit_post);
        MobileAds.initialize(this,
                getString(R.string.ad_mob_id));
        init();
        getDataFromBundle();

        getPost();

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private void init() {
        if (Utility.getLoginUserDetail(this).getUserType().equals("4")) {
            mCollegeId = Utility.getLoginUserId(this);
        } else {
            mCollegeId = Utility.getLoginUserDetail(EdukitPostActivity.this).getUserInstituteId();
        }
        //mCollegeId = "45";
        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        edukitId = bundle.getString("edukit_id");
    }

    /**
     * Set recycler view list adapter
     */
    private void setRecyclerView(ArrayList<GetEdukitResponse.EdukitPost> arrayList) {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(EdukitPostActivity.this));
        EdukitPostAdapter edukitPostAdapter = new EdukitPostAdapter(this, arrayList);
        mBinding.recyclerView.setAdapter(edukitPostAdapter);
    }

    /**
     * Api call for getEdukitPost
     */
    public void getPost() {
        ProgressDialog.getInstance().showProgressDialog(EdukitPostActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetEdukitResponse> responseCall = api.getEdukitPost(mCollegeId, edukitId);
        responseCall.enqueue(new Callback<GetEdukitResponse>() {
            @Override
            public void onResponse(Call<GetEdukitResponse> call, retrofit2.Response<GetEdukitResponse> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetEdukitResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    /**
     * Handle response
     */
    private void handleResponse(GetEdukitResponse response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response == null) {
            Utility.showToast(EdukitPostActivity.this, getString(R.string.wrong));
        } else {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                setRecyclerView(response.getPostArrayList());
            } else {
                Utility.showToast(EdukitPostActivity.this, getString(R.string.wrong));
            }
        }
    }
}