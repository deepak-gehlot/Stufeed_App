package com.stufeed.android.view.fragment.connect.academy;

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
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.databinding.FragmentCreateBoardBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.BoardListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcademyBoardListFragment extends Fragment {

    public AcademyBoardListFragment() {
        // Required empty public constructor
    }

    private FragmentCreateBoardBinding binding;
    private String loginUserId = "";

    public static AcademyBoardListFragment newInstance() {
        Bundle args = new Bundle();
        AcademyBoardListFragment fragment = new AcademyBoardListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_board, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginUserId = Utility.getLoginUserDetail(getActivity()).getCollegeId();

        if (TextUtils.isEmpty(loginUserId) || loginUserId.equals("0")) {
            loginUserId = Utility.getLoginUserId(getActivity());
        }

        getBoardList();
    }

    private void getBoardList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetBoardListResponse> responseCall = api.getBoardList(loginUserId);
        responseCall.enqueue(new Callback<GetBoardListResponse>() {
            @Override
            public void onResponse(Call<GetBoardListResponse> call, Response<GetBoardListResponse> response) {
                handleBoardListResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetBoardListResponse> call, Throwable t) {
                handleBoardListResponse(null);
            }
        });
    }

    private void handleBoardListResponse(GetBoardListResponse response) {
        binding.progressBar.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.VISIBLE);
        if (response == null) {
            Utility.showErrorMsg(getActivity());
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(response.getBoardArrayList());
        }
    }

    private void setRecyclerView(ArrayList<GetBoardListResponse.Board> boardArrayList) {
        if (boardArrayList != null) {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            BoardListAdapter adapter = new BoardListAdapter(getActivity(), boardArrayList);
            binding.recyclerView.setAdapter(adapter);
        }
    }
}