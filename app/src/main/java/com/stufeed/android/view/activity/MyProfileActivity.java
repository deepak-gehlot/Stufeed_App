package com.stufeed.android.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.FollowResponse;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.GetUserDetailsResponse;
import com.stufeed.android.databinding.ActivityMyProfileBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.MyBoardPostCombineAdapter;
import com.stufeed.android.view.adapter.UserBoardListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity {

    public static final String USER = "user";

    private ActivityMyProfileBinding mBinding;
    private GetCollegeUserResponse.User user;
    private String mLoginUserId = "";
    private GetUserDetailsResponse userDetailsResponse = new GetUserDetailsResponse();
    private GetPostResponse getPostResponse = new GetPostResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
        MobileAds.initialize(this,
                getString(R.string.ad_mob_id));
        setSupportActionBar(mBinding.toolbar);
        mBinding.container.setActivity(this);

        mLoginUserId = Utility.getLoginUserId(MyProfileActivity.this);
        setTitleBackClick();
        getDataFromBundle();
        getBasicDetails();
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
            Utility.showToast(MyProfileActivity.this, getString(R.string.wrong));
            finish();
        } else {
            user = bundle.getParcelable(USER);
        }
    }

    public void onPostCountClick() {
        Intent intent = new Intent(MyProfileActivity.this, UsersPostActivity.class);
        intent.putExtra("user_id", user.getUserId());
        startActivity(intent);
    }

    public void onBoardJoinCountClick() {
        Intent intent = new Intent(MyProfileActivity.this, UserJoinBoardActivity.class);
        intent.putExtra("user_id", user.getUserId());
        intent.putExtra("login_user_id", mLoginUserId);
        startActivity(intent);
    }

    public void onFollowerCountClick() {
        Intent intent = new Intent(MyProfileActivity.this, UserFollowingActivity.class);
        intent.putExtra("user_id", user.getUserId());
        startActivity(intent);
    }

    private void getBoardList() {
        String userId = Utility.getLoginUserId(MyProfileActivity.this);
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

    private GetBoardListResponse response;

    private void handleResponse(GetBoardListResponse response) {
        if (response == null) {
            Utility.showErrorMsg(MyProfileActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            this.response = response;
            /*setRecyclerView(response.getBoardArrayList());
            mBinding.container.setCountModel(response.getAllCount());*/
        }
        getAllPost();
    }

    private void setRecyclerView(ArrayList<GetBoardListResponse.Board> boardArrayList) {
        if (boardArrayList != null) {
            mBinding.container.recyclerView.setHasFixedSize(true);
            mBinding.container.recyclerView.setLayoutManager(new GridLayoutManager(MyProfileActivity.this, 2));
            UserBoardListAdapter adapter = new UserBoardListAdapter(MyProfileActivity.this, boardArrayList);
            mBinding.container.recyclerView.setAdapter(adapter);
        }
    }

    /*public void onClickDownArrow() {
        if (mBinding.container.detailsLayout.getVisibility() == View.GONE) {
            mBinding.container.detailsLayout.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (tagList != null) {
                        mBinding.container.tagGroup.addTags(tagList);
                    }
                }
            }, 10);
        } else {
            mBinding.container.detailsLayout.setVisibility(View.GONE);
        }
    }*/

    /*public void setUserType(String type) {
        switch (type) {
            case "1":
                mBinding.container.txtType.setText("Student");
                break;
            case "2":
                mBinding.container.txtType.setText("Department");
                break;
            case "3":
                mBinding.container.txtType.setText("Faculty");
                break;
            case "4":
                mBinding.container.txtType.setText("Institute");
                break;
        }
    }*/

    /**
     * Get User basic details
     */
    private void getBasicDetails() {
        ProgressDialog.getInstance().showProgressDialog(MyProfileActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetUserDetailsResponse> responseCall = api.getUserDetails(mLoginUserId, user.getUserId());
        responseCall.enqueue(new Callback<GetUserDetailsResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailsResponse> call, Response<GetUserDetailsResponse> response) {
                handleUserResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetUserDetailsResponse> call, Throwable t) {
                handleUserResponse(null);
            }
        });
    }

    /**
     * Get User Achievement list
     */
    /*private void getAchievement() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetAchievementListResponse> responseCall = api.getAllAchievements(user.getUserId());
        responseCall.enqueue(new Callback<GetAchievementListResponse>() {
            @Override
            public void onResponse(Call<GetAchievementListResponse> call, Response<GetAchievementListResponse>
                    response) {
                handleAchievementResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetAchievementListResponse> call, Throwable t) {
                handleAchievementResponse(null);
            }
        });
    }*/

    /**
     * User details response
     */
    private void handleUserResponse(GetUserDetailsResponse response) {
        //mBinding.container.topPanel.setVisibility(View.VISIBLE);
        if (response != null) {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                userDetailsResponse = response;
                mBinding.container.setModel(response.getAllDetails());
                mBinding.toolbar.setTitle(userDetailsResponse.getAllDetails().getFullName());
            }
        }
        getBoardList();
        //getAchievement();
    }

    /**
     * Get achievement response
     */
   /* private void handleAchievementResponse(GetAchievementListResponse response) {
        if (response != null) {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                setAchievementRecyclerView(response.getAchievementArrayList());
            } else {
                isHaveAchivment = false;
                mBinding.container.textAchievement.setVisibility(View.GONE);
                mBinding.container.recyclerViewAchievement.setVisibility(View.GONE);
            }
            showHideArrow();
        }
    }*/

    /**
     * Get Achievement response
     */
   /* private void setAchievementRecyclerView(ArrayList<GetAchievementListResponse.Achievement> achievementArrayList) {
        if (achievementArrayList != null && achievementArrayList.size() != 0) {
            AchivementFragmentListAdapter adapter = new AchivementFragmentListAdapter(UserProfileActivity.this,
                    achievementArrayList);
            mBinding.container.recyclerViewAchievement.setLayoutManager(new LinearLayoutManager(UserProfileActivity
                    .this));
            mBinding.container.recyclerViewAchievement.setAdapter(adapter);
            isHaveAchivment = true;
        } else {
            isHaveAchivment = false;
            mBinding.container.textAchievement.setVisibility(View.GONE);
            mBinding.container.recyclerViewAchievement.setVisibility(View.GONE);
            Utility.showToast(UserProfileActivity.this, getString(R.string.wrong));
        }
        showHideArrow();
    }*/

   /* private void showHideArrow() {
        if (isHaveAchivment || isHaveAbout) {
            mBinding.container.imageDownIcon.setVisibility(View.VISIBLE);
        } else {
            mBinding.container.imageDownIcon.setVisibility(View.GONE);
        }
    }*/

    /**
     * unblock user
     */
    private void unBlock() {
        ProgressDialog.getInstance().showProgressDialog(MyProfileActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<com.stufeed.android.api.response.Response> responseCall = api.unblockUser(mLoginUserId, user.getUserId());
        responseCall.enqueue(new Callback<com.stufeed.android.api.response.Response>() {
            @Override
            public void onResponse(Call<com.stufeed.android.api.response.Response> call, Response<com.stufeed.android
                    .api.response.Response> response) {
                handleResponseBlockUnblock(response.body());
            }

            @Override
            public void onFailure(Call<com.stufeed.android.api.response.Response> call, Throwable t) {
                handleResponseBlockUnblock(null);
            }
        });
    }

    /**
     * block user
     */
    private void block() {
        ProgressDialog.getInstance().showProgressDialog(MyProfileActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<com.stufeed.android.api.response.Response> responseCall = api.blockUser(mLoginUserId, user.getUserId());
        responseCall.enqueue(new Callback<com.stufeed.android.api.response.Response>() {
            @Override
            public void onResponse(Call<com.stufeed.android.api.response.Response> call, Response<com.stufeed.android
                    .api.response.Response> response) {
                handleResponseBlockUnblock(response.body());
            }

            @Override
            public void onFailure(Call<com.stufeed.android.api.response.Response> call, Throwable t) {
                handleResponseBlockUnblock(null);
            }
        });
    }

    /**
     * Handle block unblock response
     */
    private void handleResponseBlockUnblock(com.stufeed.android.api.response.Response response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response == null) {
            Utility.showErrorMsg(MyProfileActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            Utility.showToast(MyProfileActivity.this, response.getResponseMessage());
            if (mBinding.container.getModel().getIsBlock().equals("1")) {
                mBinding.container.getModel().setIsBlock("0");
            } else {
                mBinding.container.getModel().setIsBlock("1");
            }
        } else {
            Utility.showToast(MyProfileActivity.this, response.getResponseMessage());
        }
    }

    public void followUnFollowClick() {
        if (mBinding.container.getModel().getIsFollow().equals("1")) {
            setUnFollowConfirmation(mBinding.container.getModel());
        } else {
            setFollowConfirmation(mBinding.container.getModel());
        }
    }

    private void follow() {
        if (mBinding.container.getModel().getIsFollow().equals("1")) {
            mBinding.container.getModel().setIsFollow("0");
        } else {
            mBinding.container.getModel().setIsFollow("1");
        }
        //refreshFollowUnfollow();
        Api api = APIClient.getClient().create(Api.class);
        Call<FollowResponse> responseCall = api.follow(mLoginUserId, user.getUserId());
        responseCall.enqueue(new Callback<FollowResponse>() {
            @Override
            public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {

            }

            @Override
            public void onFailure(Call<FollowResponse> call, Throwable t) {

            }
        });
    }

    private void setUnFollowConfirmation(final GetUserDetailsResponse.Details user) {
        Utility.setDialog(MyProfileActivity.this, "Message", "Do you want to un-follow this.", "No", "Yes",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        follow();
                    }
                });
    }

    private void setFollowConfirmation(final GetUserDetailsResponse.Details user) {
        Utility.setDialog(MyProfileActivity.this, "Message", "Do you want to follow this.", "No", "Yes",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        follow();
                    }
                });
    }

    private void getAllPost() {
        //  binding.progressBar.setVisibility(View.VISIBLE);
        Api api = APIClient.getClient().create(Api.class);
        String isLoginUser = "0";
        if (user.getUserId().equals(Utility.getLoginUserId(MyProfileActivity.this))) {
            isLoginUser = "1";
        }
        Call<GetPostResponse> responseCall = api.getUserAllPost(user.getUserId(), isLoginUser);
        responseCall.enqueue(new Callback<GetPostResponse>() {
            @Override
            public void onResponse(Call<GetPostResponse> call, Response<GetPostResponse> response) {
                //         binding.progressBar.setVisibility(View.GONE);
                handleGetAllPostResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetPostResponse> call, Throwable t) {
                //       binding.progressBar.setVisibility(View.GONE);
                Utility.showToast(MyProfileActivity.this, getString(R.string.wrong));
            }
        });
    }

    private void handleGetAllPostResponse(GetPostResponse getPostResponse) {
        if (getPostResponse != null) {
            if (getPostResponse.getResponseCode().equals(Api.SUCCESS)) {
                this.getPostResponse = getPostResponse;
            }
        }
        mBinding.container.recyclerView.setLayoutManager(new LinearLayoutManager(MyProfileActivity.this));
        MyBoardPostCombineAdapter adapter = new MyBoardPostCombineAdapter(MyProfileActivity.this, user,
                response.getAllCount(), userDetailsResponse.getAllDetails(), response.getBoardArrayList(), this.getPostResponse.getPost());
        mBinding.container.recyclerView.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressDialog.getInstance().dismissDialog();
            }
        }, 500);
    }
}