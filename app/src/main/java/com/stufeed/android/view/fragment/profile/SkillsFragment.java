package com.stufeed.android.view.fragment.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cunoraz.tagview.Tag;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAllSkillsResponse;
import com.stufeed.android.databinding.FragmentSkillsBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.SkillsActivity;

import retrofit2.Call;
import retrofit2.Callback;

public class SkillsFragment extends Fragment {

    public SkillsFragment() {
        // Required empty public constructor
    }

    public static SkillsFragment newInstance() {
        Bundle args = new Bundle();
        SkillsFragment fragment = new SkillsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentSkillsBinding mBinding;
    private String mLoginUserId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_skills, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginUserId = Utility.getLoginUserId(getActivity());
        getAllSkills();
    }


    /**
     * Get all skills list
     */
    public void getAllSkills() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetAllSkillsResponse> responseCall = api.getUserSkills(mLoginUserId);
        responseCall.enqueue(new Callback<GetAllSkillsResponse>() {
            @Override
            public void onResponse(Call<GetAllSkillsResponse> call, retrofit2.Response<GetAllSkillsResponse> response) {
                handleGetUserSkillResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetAllSkillsResponse> call, Throwable t) {
                handleGetUserSkillResponse(null);
            }
        });
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
                    Tag tag = new Tag(skills[i]);
                    tag.isDeletable = false;
                    mBinding.tagGroup.addTag(tag);
                }
            }
        }
    }
}