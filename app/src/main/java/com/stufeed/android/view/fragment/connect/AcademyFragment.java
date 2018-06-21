package com.stufeed.android.view.fragment.connect;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cunoraz.tagview.Tag;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAchievementListResponse;
import com.stufeed.android.api.response.GetAllSkillsResponse;
import com.stufeed.android.api.response.GetUserDescriptionResponse;
import com.stufeed.android.api.response.GetUserDetailsResponse;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.databinding.FragmentAcademyBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.FolloweListActivity;
import com.stufeed.android.view.activity.HomeActivity;
import com.stufeed.android.view.activity.UserJoinBoardActivity;
import com.stufeed.android.view.activity.UsersPostActivity;
import com.stufeed.android.view.adapter.AchivementFragmentListAdapter;
import com.stufeed.android.view.adapter.ViewPagerAdapter;
import com.stufeed.android.view.fragment.FeedFragment;
import com.stufeed.android.view.fragment.UserFeedFragment;
import com.stufeed.android.view.fragment.connect.academy.AcademyBoardListFragment;
import com.stufeed.android.view.fragment.connect.academy.EduKitFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcademyFragment extends Fragment {

    public AcademyFragment() {
        // Required empty public constructor
    }

    public static AcademyFragment newInstance() {
        Bundle args = new Bundle();
        AcademyFragment fragment = new AcademyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentAcademyBinding binding;
    private boolean isVisible = false;
    private List<Tag> tagList = new ArrayList<>();
    private String mLoginUserId = "";
    private boolean isHaveSkills = false;
    private boolean isHaveAchivment = false;
    private boolean isHaveAbout = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_academy, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Utility.getLoginUserDetail(getActivity()).getUserType().equals("4")) {
            mLoginUserId = Utility.getLoginUserId(getActivity());
        } else {
            mLoginUserId = Utility.getLoginUserDetail(getActivity()).getUserInstituteId();
        }
        setupViewPager(binding.viewpager);
        binding.setFragment(this);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
        binding.viewpager.setOffscreenPageLimit(3);


        getBasicDetails();
        setUserType();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
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

    public void setUserType() {
        Utility.setUserTypeTagColor(getActivity(), "4", binding.txtType);
        binding.txtType.setText("Institute");
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
        if (Utility.getLoginUserDetail(getActivity()).getUserType().equals("4")) {
            collegeId = Utility.getLoginUserId(getActivity());
        } else {
            collegeId = Utility.getLoginUserDetail(getActivity()).getUserInstituteId();
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(EduKitFragment.newInstance(), "EDUKIT");
        adapter.addFragment(UserFeedFragment.newInstance(collegeId), "POST");
        adapter.addFragment(AcademyBoardListFragment.newInstance(), "BOARD");
        viewPager.setAdapter(adapter);
    }

    /**
     * Get User basic details
     */
    private void getBasicDetails() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetUserDetailsResponse> responseCall = api.getUserDetails(
                mLoginUserId, mLoginUserId);
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
     * User details response
     */
    private void handleUserResponse(GetUserDetailsResponse response) {
        binding.progressBar.setVisibility(View.GONE);
        binding.topPanel.setVisibility(View.VISIBLE);
        if (response != null) {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                binding.setModel(response.getAllDetails());

                String description = response.getAllDetails().getAbout();
                binding.textAboutMe.setText(description);
                binding.txtUserName.setText(binding.getModel().getFullName());
                String allSkills = response.getAllDetails().getSkills();
                String skills[] = allSkills.split(",");
                for (int i = 0; i < skills.length; i++) {
                    if (!TextUtils.isEmpty(skills[i])) {
                        Tag tag = new Tag(skills[i]);
                        tagList.add(tag);
                    }
                }
                if (tagList.size() == 0) {
                    isHaveSkills = false;
                    binding.textSkill.setVisibility(View.GONE);
                } else {
                    isHaveSkills = true;
                    binding.textSkill.setVisibility(View.VISIBLE);
                }

            }
        }
        getAchievement();
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