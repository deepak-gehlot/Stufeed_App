package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.cunoraz.tagview.Tag;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAchievementListResponse;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.GetUserDetailsResponse;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.databinding.ActivityMyProfileMainBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.AchivementFragmentListAdapter;
import com.stufeed.android.view.adapter.ViewPagerAdapter;
import com.stufeed.android.view.fragment.UserFeedFragment;
import com.stufeed.android.view.fragment.connect.academy.AcademyBoardListFragment;
import com.stufeed.android.view.fragment.connect.academy.EduKitFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileMainActivity extends AppCompatActivity {

    private ActivityMyProfileMainBinding binding;
    private String mLoginUserId = "";
    private GetUserDetailsResponse userDetailsResponse = new GetUserDetailsResponse();
    private List<Tag> tagList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile_main);
        setSupportActionBar(binding.tabToolbar);


        mLoginUserId = Utility.getLoginUserId(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        setupViewPager(viewPager);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tabLayout.setupWithViewPager(viewPager);

        binding.tabToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AQuery aQuery = new AQuery(this);
        String profilePic = Utility.getLoginUserDetail(this).getProfilePic();
        aQuery.id(binding.profilePic).image(profilePic, true, true, 100, R.drawable.user_default);

        getBasicDetails();
        setUserType();
        binding.setActivity(this);
    }

    public void setUserType() {
        UserDetail userDetail = Utility.getLoginUserDetail(MyProfileMainActivity.this);
        Utility.setUserTypeTagColor(MyProfileMainActivity.this, userDetail.getUserType(), binding.txtType);
        binding.txtType.setText("Institute");
    }

    public void onPostCountClick() {
        Intent intent = new Intent(MyProfileMainActivity.this, UsersPostActivity.class);
        intent.putExtra("user_id", mLoginUserId);
        startActivity(intent);
    }

    public void onBoardJoinCountClick() {
        Intent intent = new Intent(MyProfileMainActivity.this, UserJoinBoardActivity.class);
        intent.putExtra("user_id", mLoginUserId);
        intent.putExtra("login_user_id", mLoginUserId);
        startActivity(intent);
    }

    public void onFollowerCountClick() {
        Intent intent = new Intent(MyProfileMainActivity.this, UserFollowingActivity.class);
        intent.putExtra("user_id", mLoginUserId);
        startActivity(intent);
    }

    public void onEditButtonClick() {
        startActivity(new Intent(MyProfileMainActivity.this, EditProfileActivity.class));
    }

    public void onViewButtonClick() {
        startActivity(new Intent(MyProfileMainActivity.this, ViewFullProfileActivity.class));
    }

    public void onClickDownArrow() {
        if (binding.detailsLayout.getVisibility() == View.GONE) {
            binding.detailsLayout.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (tagList != null) {
                        binding.tagGroup.addTags(tagList);
                    }
                }
            }, 10);
        } else {
            binding.detailsLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Set view pager adapter
     *
     * @param viewPager @ViewPager
     */
    private void setupViewPager(ViewPager viewPager) {

        String collegeId = "";
        if (Utility.getLoginUserDetail(MyProfileMainActivity.this).getUserType().equals("4")) {
            collegeId = Utility.getLoginUserId(MyProfileMainActivity.this);
        } else {
            collegeId = Utility.getLoginUserDetail(MyProfileMainActivity.this).getUserInstituteId();
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(EduKitFragment.newInstance(), "Edukit");
        adapter.addFragment(UserFeedFragment.newInstance(collegeId), "Post");
        adapter.addFragment(AcademyBoardListFragment.newInstance(), "Board");
        viewPager.setAdapter(adapter);
    }

    /**
     * Get User basic details
     */
    private void getBasicDetails() {
        ProgressDialog.getInstance().showProgressDialog(MyProfileMainActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetUserDetailsResponse> responseCall = api.getUserDetails(mLoginUserId, mLoginUserId);
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
     * User details response
     */
    private void handleUserResponse(GetUserDetailsResponse response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response != null) {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                userDetailsResponse = response;
                binding.setModel(response.getAllDetails());
                String description = userDetailsResponse.getAllDetails().getAbout();
                // binding.textAboutMeData.setText(description);
                binding.txtUserName.setText(userDetailsResponse.getAllDetails().getFullName());
                binding.tabToolbar.setTitle(userDetailsResponse.getAllDetails().getFullName());
                String allSkills = response.getAllDetails().getSkills();
                String skills[] = allSkills.split(",");
                for (int i = 0; i < skills.length; i++) {
                    if (!TextUtils.isEmpty(skills[i])) {
                        Tag tag = new Tag(skills[i]);
                        tagList.add(tag);
                    }
                }
                if (tagList.size() == 0) {
                    //..isHaveSkills = false;
                    binding.textSkill.setVisibility(View.GONE);
                } else {
                    //isHaveSkills = true;
                    binding.textSkill.setVisibility(View.VISIBLE);
                }
                setAchievementRecyclerView(userDetailsResponse.getAllDetails().getAchievementArrayList());
            }
        }
    }

    /**
     * Get Achievement response
     */
    private void setAchievementRecyclerView(ArrayList<GetAchievementListResponse.Achievement> achievementArrayList) {
        if (achievementArrayList != null && achievementArrayList.size() != 0) {
            AchivementFragmentListAdapter adapter = new AchivementFragmentListAdapter(MyProfileMainActivity.this,
                    achievementArrayList);
            binding.recyclerViewAchievement.setLayoutManager(new LinearLayoutManager(MyProfileMainActivity.this));
            binding.recyclerViewAchievement.setAdapter(adapter);
        } else {
            binding.textAchievement.setVisibility(View.GONE);
            binding.recyclerViewAchievement.setVisibility(View.GONE);
        }
        // showHideArrow();
    }

    private void getBoardList() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetBoardListResponse> responseCall = api.getAllBoardList(mLoginUserId, mLoginUserId);
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
        if (response == null) {
            Utility.showErrorMsg(MyProfileMainActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            //this.response = response;
            /*setRecyclerView(response.getBoardArrayList());
            mBinding.container.setCountModel(response.getAllCount());*/
        }
        getAllPost();
    }

    private void getAllPost() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetPostResponse> responseCall = api.getUserAllPost(mLoginUserId, "1");
        responseCall.enqueue(new Callback<GetPostResponse>() {
            @Override
            public void onResponse(Call<GetPostResponse> call, Response<GetPostResponse> response) {
                //         binding.progressBar.setVisibility(View.GONE);
                handleGetAllPostResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetPostResponse> call, Throwable t) {
                //       binding.progressBar.setVisibility(View.GONE);
                Utility.showToast(MyProfileMainActivity.this, getString(R.string.wrong));
            }
        });
    }

    private void handleGetAllPostResponse(GetPostResponse getPostResponse) {
        /*if (getPostResponse != null) {
            if (getPostResponse.getResponseCode().equals(Api.SUCCESS)) {
                this.getPostResponse = getPostResponse;
            }
        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MyProfileMainActivity.this));
        BoardPostCombineAdapter adapter = new BoardPostCombineAdapter(MyProfileMainActivity.this,
                user, response.getAllCount(), userDetailsResponse.getAllDetails(), response.getBoardArrayList(), this.getPostResponse.getPost());
        binding.recyclerView.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressDialog.getInstance().dismissDialog();
            }
        }, 500);*/
    }
}
