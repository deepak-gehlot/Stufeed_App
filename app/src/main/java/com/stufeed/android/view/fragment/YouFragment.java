package com.stufeed.android.view.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.cunoraz.tagview.Tag;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAchievementListResponse;
import com.stufeed.android.api.response.GetAllSkillsResponse;
import com.stufeed.android.api.response.GetUserDescriptionResponse;
import com.stufeed.android.api.response.GetUserDetailsResponse;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.databinding.FragmentYouBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.EditProfileActivity;
import com.stufeed.android.view.activity.FolloweListActivity;
import com.stufeed.android.view.activity.UserJoinBoardActivity;
import com.stufeed.android.view.activity.UsersPostActivity;
import com.stufeed.android.view.activity.ViewFullProfileActivity;
import com.stufeed.android.view.adapter.AchivementFragmentListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YouFragment extends Fragment {

    public YouFragment() {
        // Required empty public constructor
    }

    private FragmentYouBinding binding;
    private List<Tag> tagList = new ArrayList<>();
    private String mLoginUserId = "";
    private boolean isHaveSkills = false;
    private boolean isHaveAchivment = false;
    private boolean isHaveAbout = false;

    public static YouFragment newInstance() {
        Bundle args = new Bundle();
        YouFragment fragment = new YouFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_you, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        mLoginUserId = Utility.getLoginUserId(getActivity());
        setRecyclerView();
        getBasicDetails();
        setUserType();
    }

    @Override
    public void onResume() {
        super.onResume();
        AQuery aQuery = new AQuery(getActivity());
        String profilePic = Utility.getLoginUserDetail(getActivity()).getProfilePic();
        aQuery.id(binding.profilePic).image(profilePic, true, true, 100, R.drawable.user_default);
    }

    public void onPostCountClick() {
        Intent intent = new Intent(getActivity(), UsersPostActivity.class);
        intent.putExtra("user_id", mLoginUserId);
        startActivity(intent);
    }

    public void onBoardJoinCountClick() {
        Intent intent = new Intent(getActivity(), UserJoinBoardActivity.class);
        intent.putExtra("user_id", mLoginUserId);
        startActivity(intent);
    }

    public void onFollowerCountClick() {
        Intent intent = new Intent(getActivity(), FolloweListActivity.class);
        intent.putExtra("user_id", mLoginUserId);
        startActivity(intent);
    }

    public void onEditButtonClick() {
        startActivity(new Intent(getActivity(), EditProfileActivity.class));
    }

    public void onViewButtonClick() {
        startActivity(new Intent(getActivity(), ViewFullProfileActivity.class));
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

    public void setUserType() {
        UserDetail userDetail = Utility.getLoginUserDetail(getActivity());
        switch (userDetail.getUserType()) {
            case "1":
                binding.txtType.setText("Student");
                break;
            case "2":
                binding.txtType.setText("Department");
                break;
            case "3":
                binding.txtType.setText("Faculty");
                break;
            case "4":
                binding.txtType.setText("Institute");
                break;
        }
    }

    private void setRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*FeedListAdapter adapter = new FeedListAdapter(getActivity());
        binding.recyclerView.setAdapter(adapter);*/
    }

    /**
     * Get User basic details
     */
    private void getBasicDetails() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetUserDetailsResponse> responseCall = api.getUserAllInfo(mLoginUserId);
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
        Call<GetAllSkillsResponse> responseCall = api.getUserSkills(mLoginUserId);
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
        Call<GetAchievementListResponse> responseCall = api.getAllAchievements(mLoginUserId);
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
                    binding.textAboutMe.setText(description);
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
        binding.progressBar.setVisibility(View.GONE);
        binding.topPanel.setVisibility(View.VISIBLE);
        if (response != null) {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                binding.setModel(response.getAllDetails());
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
        tagList.clear();
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
            binding.textSkill.setVisibility(View.GONE);
        } else {
            isHaveSkills = true;
            binding.textSkill.setVisibility(View.VISIBLE);
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
                binding.textAchievement.setVisibility(View.GONE);
                binding.recyclerViewAchievement.setVisibility(View.GONE);
            }
        }
        showHideArrow();
    }

    /**
     * Get Achievement response
     */
    private void setAchievementRecyclerView(ArrayList<GetAchievementListResponse.Achievement> achievementArrayList) {
        if (achievementArrayList != null) {
            AchivementFragmentListAdapter adapter = new AchivementFragmentListAdapter(getActivity(),
                    achievementArrayList);
            binding.recyclerViewAchievement.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclerViewAchievement.setAdapter(adapter);
            isHaveAchivment = true;
        } else {
            isHaveAchivment = false;
            binding.textAchievement.setVisibility(View.GONE);
            binding.recyclerViewAchievement.setVisibility(View.GONE);
            Utility.showToast(getActivity(), getString(R.string.wrong));
        }
        showHideArrow();
    }

    private void showHideArrow() {
        if (isHaveAchivment || isHaveAchivment || isHaveAbout) {
            binding.imageDownIcon.setVisibility(View.VISIBLE);
        } else {
            binding.imageDownIcon.setVisibility(View.GONE);
        }
    }
}