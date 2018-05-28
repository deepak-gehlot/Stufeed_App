package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetJoinBoardListResponse;
import com.stufeed.android.databinding.ActivityUserJoinBoardBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.JoinBoardListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserJoinBoardActivity extends AppCompatActivity {

    private ActivityUserJoinBoardBinding mBinding;
    private String userId = "";
    private String loginUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_join_board);
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("user_id");
        loginUserId = bundle.getString("login_user_id");
        getBoardList();

        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    /**
     * Get login user board list
     */
    private void getBoardList() {
        mBinding.recyclerView.setVisibility(View.GONE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetJoinBoardListResponse> responseCall = api.getJoinBoardList(userId, loginUserId);
        responseCall.enqueue(new Callback<GetJoinBoardListResponse>() {
            @Override
            public void onResponse(Call<GetJoinBoardListResponse> call, Response<GetJoinBoardListResponse> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetJoinBoardListResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(GetJoinBoardListResponse response) {
        mBinding.recyclerView.setVisibility(View.VISIBLE);
        mBinding.progressBar.setVisibility(View.GONE);
        if (response == null) {
            Utility.showToast(UserJoinBoardActivity.this, getString(R.string.wrong));
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(response.getBoardArrayList());
        } else {
            Utility.showToast(UserJoinBoardActivity.this, getString(R.string.wrong));
        }
    }

    private void setRecyclerView(ArrayList<GetJoinBoardListResponse.Board> boardArrayList) {
        if (boardArrayList != null) {
            boolean isAdmin = false;
            if (userId.equals(loginUserId)) {
                isAdmin = true;
            }
            mBinding.recyclerView.setLayoutManager(new GridLayoutManager(UserJoinBoardActivity.this, 2));
            JoinBoardListAdapter adapter = new JoinBoardListAdapter(UserJoinBoardActivity.this, boardArrayList, isAdmin);
            mBinding.recyclerView.setAdapter(adapter);
        }
    }
}
