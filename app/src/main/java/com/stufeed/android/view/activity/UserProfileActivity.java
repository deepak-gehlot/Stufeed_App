package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.databinding.ActivityUserProfileBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.BoardListAdapter;
import com.stufeed.android.view.adapter.UserBoardListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    public static final String USER = "user";
    public static final String TYPE = "type";

    private ActivityUserProfileBinding mBinding;
    private GetCollegeUserResponse.User user;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        setSupportActionBar(mBinding.toolbar);
        setTitleBackClick();
        getDataFromBundle();
        getBoardList();
        mBinding.container.setUser(user);

        if (user.getIsFollow().equals("1")) {
            mBinding.container.btnFollowStatus.setText("Followed");
        } else {
            mBinding.container.btnFollowStatus.setVisibility(View.GONE);
        }

        mBinding.container.txtUserName.setText(user.getFullName());
        mBinding.container.txtType.setText(type);
    }

    private void setTitleBackClick() {
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Utility.showToast(UserProfileActivity.this, getString(R.string.wrong));
            finish();
        } else {
            user = bundle.getParcelable(USER);
            type = bundle.getString(TYPE);
        }
    }

    private void getBoardList() {
        ProgressDialog.getInstance().showProgressDialog(UserProfileActivity.this);
        String userId = Utility.getLoginUserId(UserProfileActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetBoardListResponse> responseCall = api.getAllBoardList(user.getUserId(), userId);
        responseCall.enqueue(new Callback<GetBoardListResponse>() {
            @Override
            public void onResponse(Call<GetBoardListResponse> call, Response<GetBoardListResponse> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetBoardListResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(GetBoardListResponse response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response == null) {
            Utility.showErrorMsg(UserProfileActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(response.getBoardArrayList());
            mBinding.container.setCountModel(response.getAllCount());
        }
    }

    private void setRecyclerView(ArrayList<GetBoardListResponse.Board> boardArrayList) {
        if (boardArrayList != null) {
            mBinding.container.recyclerView.setLayoutManager(new GridLayoutManager(UserProfileActivity.this, 2));
            UserBoardListAdapter adapter = new UserBoardListAdapter(UserProfileActivity.this, boardArrayList);
            mBinding.container.recyclerView.setAdapter(adapter);
        }
    }
}