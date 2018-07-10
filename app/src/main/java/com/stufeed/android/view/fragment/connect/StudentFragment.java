package com.stufeed.android.view.fragment.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.databinding.FragmentStudentBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.FacultyListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentFragment extends Fragment {

    private FragmentStudentBinding binding;
    private String loginUserId = "", loginUserCollegeId = "";
    private FacultyListAdapter facultyListAdapter;
    ArrayList<GetCollegeUserResponse.User> userArrayList = new ArrayList<>();

    public StudentFragment() {
        // Required empty public constructor
    }

    public static StudentFragment newInstance() {

        Bundle args = new Bundle();

        StudentFragment fragment = new StudentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginUserId = Utility.getLoginUserId(getActivity());
        loginUserCollegeId = Utility.getLoginUserDetail(getActivity()).getCollegeId();
        setRecyclerView();
        getStudents();
        SearchReceiver searchReceiver = new SearchReceiver();
        getActivity().registerReceiver(searchReceiver, new IntentFilter("com.stufeed.android.search"));

    }

    private void getStudents() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetCollegeUserResponse> responseCall = api.getCollegeUsers(loginUserId, loginUserCollegeId, "1");
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
        if (getView() != null) {
            if (response == null) {
              //  Utility.showErrorMsg(getActivity());
            } else if (response.getResponseCode().equals(Api.SUCCESS)) {
                userArrayList = response.getUserArrayList();
                setRecyclerView();
            }
        }
    }

    private void setRecyclerView() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        facultyListAdapter = new FacultyListAdapter(getActivity(), "Student", userArrayList);
        binding.recyclerView.setAdapter(facultyListAdapter);
    }

    class SearchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int type = bundle.getInt("type");
                if (type == 2) {
                    String search = bundle.getString("search");

                    //new array list that will hold the filtered data
                    ArrayList<GetCollegeUserResponse.User> userArrayListNew = new ArrayList<>();

                    //looping through existing elements
                    for (GetCollegeUserResponse.User s : userArrayList) {
                        //if the existing elements contains the search input
                        if (TextUtils.isEmpty(search)) {
                            userArrayListNew.add(s);
                        } else if (s.getFullName().toLowerCase().contains(search.toLowerCase())) {
                            //adding the element to filtered list
                            userArrayListNew.add(s);
                        }
                    }
                    if (facultyListAdapter != null) {
                        facultyListAdapter.filterList(userArrayListNew);
                    }
                }
            }
        }
    }
}