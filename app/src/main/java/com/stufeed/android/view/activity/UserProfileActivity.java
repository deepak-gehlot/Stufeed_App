package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cunoraz.tagview.Tag;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAchievementListResponse;
import com.stufeed.android.api.response.GetAllSkillsResponse;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.GetUserDescriptionResponse;
import com.stufeed.android.api.response.GetUserDetailsResponse;
import com.stufeed.android.databinding.ActivityUserProfileBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.AchivementFragmentListAdapter;
import com.stufeed.android.view.adapter.UserBoardListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    public static final String USER = "user";
    public static final String TYPE = "type";

    private ActivityUserProfileBinding mBinding;
    private GetCollegeUserResponse.User user;
    private List<Tag> tagList = new ArrayList<>();
    private boolean isHaveSkills = false;
    private boolean isHaveAchivment = false;
    private boolean isHaveAbout = false;
    private String mLoginUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        setSupportActionBar(mBinding.toolbar);
        mBinding.container.setActivity(this);

        mLoginUserId = Utility.getLoginUserId(UserProfileActivity.this);
        setTitleBackClick();
        getDataFromBundle();
        getBoardList();

        if (user.getIsFollow().equals("1")) {
            mBinding.container.btnFollowStatus.setText("Followed");
        } else {
            mBinding.container.btnFollowStatus.setVisibility(View.GONE);
        }

        mBinding.container.txtUserName.setText(user.getFullName());

        getBasicDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuBlocked:
                block();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void onClickDownArrow() {
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
    }

    public void setUserType(String type) {
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
    }

    /**
     * Get User basic details
     */
    private void getBasicDetails() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetUserDetailsResponse> responseCall = api.getUserAllInfo(user.getUserId());
        responseCall.enqueue(new Callback<GetUserDetailsResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailsResponse> call, Response<GetUserDetailsResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleUserResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetUserDetailsResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                handleUserResponse(null);
            }
        });
    }

    /**
     * Get User Skills
     */
    private void getSkills() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetAllSkillsResponse> responseCall = api.getUserSkills(user.getUserId());
        responseCall.enqueue(new Callback<GetAllSkillsResponse>() {
            @Override
            public void onResponse(Call<GetAllSkillsResponse> call, retrofit2.Response<GetAllSkillsResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleGetUserSkillResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetAllSkillsResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                handleGetUserSkillResponse(null);
            }
        });
    }

    /**
     * Get User Achievement list
     */
    private void getAchievement() {
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
    }

    /**
     * Get About
     */
    private void getAbout() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetUserDescriptionResponse> responseCall = api.getUserDescription(mLoginUserId);
        responseCall.enqueue(new Callback<GetUserDescriptionResponse>() {
            @Override
            public void onResponse(Call<GetUserDescriptionResponse> call,
                                   retrofit2.Response<GetUserDescriptionResponse> response) {
                GetUserDescriptionResponse response1 = response.body();
                if (response1 != null && response1.getResponseCode().equals(Api.SUCCESS)) {
                    String description = response1.getDescription();
                    mBinding.container.textAboutMeData.setText(description);
                }
            }

            @Override
            public void onFailure(Call<GetUserDescriptionResponse> call, Throwable t) {

            }
        });


    }

    /**
     * User details response
     */
    private void handleUserResponse(GetUserDetailsResponse response) {
        mBinding.container.topPanel.setVisibility(View.VISIBLE);
        if (response != null) {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                mBinding.container.setModel(response.getAllDetails());
                setUserType(response.getAllDetails().getUserType());
            }
        }
        getSkills();
        getAbout();
        getAchievement();
    }

    /**
     * Get all skills response
     */
    private void handleGetUserSkillResponse(GetAllSkillsResponse response) {
        if (response != null) {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                String allSkills = response.getAllSkills();
                String skills[] = allSkills.split(",");
                for (int i = 0; i < skills.length; i++) {
                    if (!TextUtils.isEmpty(skills[i])) {
                        Tag tag = new Tag(skills[i]);
                        tagList.add(tag);
                    }
                }
            }
        }
        if (tagList.size() == 0) {
            isHaveSkills = false;
            mBinding.container.textSkill.setVisibility(View.GONE);
        } else {
            isHaveSkills = true;
            mBinding.container.textSkill.setVisibility(View.VISIBLE);
        }
        showHideArrow();
    }

    /**
     * Get achievement response
     */
    private void handleAchievementResponse(GetAchievementListResponse response) {
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
    }

    /**
     * Get Achievement response
     */
    private void setAchievementRecyclerView(ArrayList<GetAchievementListResponse.Achievement> achievementArrayList) {
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
    }

    private void showHideArrow() {
        if (isHaveAchivment || isHaveAchivment || isHaveAbout) {
            mBinding.container.imageDownIcon.setVisibility(View.VISIBLE);
        } else {
            mBinding.container.imageDownIcon.setVisibility(View.GONE);
        }
    }

    /**
     * unblock user
     */
    private void unBlock() {
        ProgressDialog.getInstance().showProgressDialog(UserProfileActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<com.stufeed.android.api.response.Response> responseCall = api.unblockUser(mLoginUserId, user.getUserId());
        responseCall.enqueue(new Callback<com.stufeed.android.api.response.Response>() {
            @Override
            public void onResponse(Call<com.stufeed.android.api.response.Response> call, Response<com.stufeed.android
                    .api.response.Response> response) {

            }

            @Override
            public void onFailure(Call<com.stufeed.android.api.response.Response> call, Throwable t) {

            }
        });
    }

    /**
     * block user
     */
    private void block() {
        ProgressDialog.getInstance().showProgressDialog(UserProfileActivity.this);
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
            Utility.showErrorMsg(UserProfileActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            Utility.showToast(UserProfileActivity.this, response.getResponseMessage());
        } else {
            Utility.showToast(UserProfileActivity.this, response.getResponseMessage());
        }
    }
}