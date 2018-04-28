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

public class FacultyFragment extends Fragment {

    private FragmentFacultyBinding binding;
    private String loginUserId = "", loginUserCollegeId = "";
    ArrayList<GetCollegeUserResponse.User> userArrayList = new ArrayList<>();
    private FacultyListAdapter facultyListAdapter;

    public FacultyFragment() {
        // Required empty public constructor
    }

    public static FacultyFragment newInstance() {

        Bundle args = new Bundle();

        FacultyFragment fragment = new FacultyFragment();
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
        SearchReceiver searchReceiver = new SearchReceiver();
        getActivity().registerReceiver(searchReceiver, new IntentFilter("com.stufeed.android.search"));
        getStudents();
    }

    private void getStudents() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetCollegeUserResponse> responseCall = api.getCollegeUsers(loginUserId, loginUserCollegeId, "2");
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
            if (response.getUserArrayList() != null) {
                userArrayList = response.getUserArrayList();
                setRecyclerView();
            }
        } else {
            Utility.showToast(getActivity(), response.getResponseMessage());
        }
    }

    private void setRecyclerView() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        facultyListAdapter = new FacultyListAdapter(getActivity(), "Faculty", userArrayList);
        binding.recyclerView.setAdapter(facultyListAdapter);
    }

    class SearchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int type = bundle.getInt("type");
                if (type == 1) {
                    String search = bundle.getString("search");

                    //new array list that will hold the filtered data
                    ArrayList<GetCollegeUserResponse.User> userArrayListNew = new ArrayList<>();

                    //looping through existing elements
                    for (GetCollegeUserResponse.User s : userArrayList) {
                        //if the existing elements contains the search input
                        if (s.getFullName().toLowerCase().contains(search.toLowerCase())) {
                            //adding the element to filtered list
                            userArrayListNew.add(s);
                        }
                    }
                    facultyListAdapter.filterList(userArrayListNew);
                }
            }
        }
    }
}