package com.stufeed.android.view.fragment.board;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
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
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.GetJoinBoardListResponse;
import com.stufeed.android.databinding.FragmentJoinBoardBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.BoardListAdapter;
import com.stufeed.android.view.adapter.JoinBoardListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinBoardFragment extends Fragment {

    public JoinBoardFragment() {
        // Required empty public constructor
    }

    private FragmentJoinBoardBinding mBinding;
    private String mLoginUserId = "";

    public static JoinBoardFragment newInstance() {
        JoinBoardFragment fragment = new JoinBoardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_join_board, container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBoardList();
    }

    /**
     * Get login user board list
     */
    private void getBoardList() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetJoinBoardListResponse> responseCall = api.getJoinBoardList(mLoginUserId);
        responseCall.enqueue(new Callback<GetJoinBoardListResponse>() {
            @Override
            public void onResponse(Call<GetJoinBoardListResponse> call, Response<GetJoinBoardListResponse> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetJoinBoardListResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(GetJoinBoardListResponse response) {
        if (response == null) {
            Utility.showToast(getActivity(), getString(R.string.wrong));
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(response.getBoardArrayList());
        } else {
            Utility.showToast(getActivity(), getString(R.string.wrong));
        }
    }

    private void setRecyclerView(ArrayList<GetJoinBoardListResponse.Board> boardArrayList) {
        if (boardArrayList != null) {
            mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            JoinBoardListAdapter adapter = new JoinBoardListAdapter(getActivity(), boardArrayList);
            mBinding.recyclerView.setAdapter(adapter);
        }
    }
}