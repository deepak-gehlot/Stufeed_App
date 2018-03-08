package com.stufeed.android.view.fragment.connect;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.databinding.FragmentFacultyBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.FacultyListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DepartmentFragment extends Fragment {


    public DepartmentFragment() {
        // Required empty public constructor
    }

    private FragmentFacultyBinding binding;
    private String loginUserId = "", loginUserCollegeId = "";

    public static DepartmentFragment newInstance() {

        Bundle args = new Bundle();

        DepartmentFragment fragment = new DepartmentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_faculty, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginUserId = Utility.getLoginUserId(getActivity());
        loginUserCollegeId = "1";

   //     getStudents();
    }

    private void getStudents() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetCollegeUserResponse> responseCall = api.getCollegeUsers(loginUserId, loginUserCollegeId, "3");
        responseCall.enqueue(new Callback<GetCollegeUserResponse>() {
            @Override
            public void onResponse(Call<GetCollegeUserResponse> call, Response<GetCollegeUserResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                handleStudentResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetCollegeUserResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                handleStudentResponse(null);
            }
        });
    }

    private void handleStudentResponse(GetCollegeUserResponse response) {
        if (response == null) {
            Utility.showErrorMsg(getActivity());
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(response.getUserArrayList());
        } else {
            Utility.showToast(getActivity(), response.getResponseMessage());
        }
    }

    private void setRecyclerView(ArrayList<GetCollegeUserResponse.User> userArrayList) {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recyclerView.setAdapter(new FacultyListAdapter(getActivity(), "Department", userArrayList));
    }

}
