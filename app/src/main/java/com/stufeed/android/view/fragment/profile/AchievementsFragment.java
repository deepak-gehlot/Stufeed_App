package com.stufeed.android.view.fragment.profile;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAchievementListResponse;
import com.stufeed.android.databinding.FragmentAchievementsBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.AchivementActivity;
import com.stufeed.android.view.adapter.AchivementFragmentListAdapter;
import com.stufeed.android.view.adapter.AchivementListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementsFragment extends Fragment {


    public AchievementsFragment() {
        // Required empty public constructor
    }

    public static AchievementsFragment newInstance() {
        Bundle args = new Bundle();
        AchievementsFragment fragment = new AchievementsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentAchievementsBinding mBinding;
    private String mLoginUserId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_achievements, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginUserId = Utility.getLoginUserId(getActivity());
        getAchievementList();
    }

    private void getAchievementList() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetAchievementListResponse> responseCall = api.getAllAchievements(mLoginUserId);
        responseCall.enqueue(new Callback<GetAchievementListResponse>() {
            @Override
            public void onResponse(Call<GetAchievementListResponse> call, Response<GetAchievementListResponse>
                    response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetAchievementListResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(GetAchievementListResponse response) {
        if (response == null) {
            Utility.showErrorMsg(getActivity());
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(response.getAchievementArrayList());
        } else {
            Utility.showToast(getActivity(), response.getResponseMessage());
        }
    }

    private void setRecyclerView(ArrayList<GetAchievementListResponse.Achievement> achievementArrayList) {
        if (achievementArrayList != null) {
            AchivementFragmentListAdapter adapter = new AchivementFragmentListAdapter(getActivity(),
                    achievementArrayList);
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBinding.recyclerView.setAdapter(adapter);
        } else {
            Utility.showToast(getActivity(), getString(R.string.wrong));
        }
    }
}
