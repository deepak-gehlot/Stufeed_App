package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetArchiveBoardListResponse;
import com.stufeed.android.databinding.ActivityArchiveBoardListBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.ArchiveBoardListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArchiveBoardListActivity extends AppCompatActivity {

    private ActivityArchiveBoardListBinding mBinding;
    private String loginUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_archive_board_list);
        loginUserId = Utility.getLoginUserId(ArchiveBoardListActivity.this);

        getBoardList();
    }

    private void getBoardList() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetArchiveBoardListResponse> responseCall = api.getArchiveBoardList(loginUserId);
        responseCall.enqueue(new Callback<GetArchiveBoardListResponse>() {
            @Override
            public void onResponse(Call<GetArchiveBoardListResponse> call, Response<GetArchiveBoardListResponse>
                    response) {
                handleBoardListResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetArchiveBoardListResponse> call, Throwable t) {
                handleBoardListResponse(null);
            }
        });
    }

    private void handleBoardListResponse(GetArchiveBoardListResponse response) {
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
        if (response == null) {
            Utility.showErrorMsg(ArchiveBoardListActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(response.getBoardArrayList());
        }
    }

    private void setRecyclerView(ArrayList<GetArchiveBoardListResponse.Board> boardArrayList) {
        if (boardArrayList != null) {
            mBinding.recyclerView.setLayoutManager(new GridLayoutManager(ArchiveBoardListActivity.this, 2));
            ArchiveBoardListAdapter adapter = new ArchiveBoardListAdapter(ArchiveBoardListActivity.this,
                    boardArrayList);
            mBinding.recyclerView.setAdapter(adapter);
        }
    }
}
