package com.stufeed.android.view.fragment.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetUserDetailsResponse;
import com.stufeed.android.databinding.FragmentBasicInfoBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicInfoFragment extends Fragment {

    public BasicInfoFragment() {
        // Required empty public constructor
    }

    public static BasicInfoFragment newInstance() {
        Bundle args = new Bundle();
        BasicInfoFragment fragment = new BasicInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentBasicInfoBinding mBinding;
    private String loginUserId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_basic_info, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginUserId = Utility.getLoginUserId(getActivity());
        getUserAllDetails();
    }

    private void getUserAllDetails() {
        ProgressDialog.getInstance().showProgressDialog(getActivity());
        Api api = APIClient.getClient().create(Api.class);
        Call<GetUserDetailsResponse> responseCall = api.getUserAllInfo(loginUserId);
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

    private void handleUserResponse(GetUserDetailsResponse response) {
        if (response == null) {
            Utility.showErrorMsg(getActivity());
        } else {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                mBinding.setModel(response.getAllDetails());
            } else {
                Utility.showToast(getActivity(), response.getResponseMessage());
            }
        }
    }
}