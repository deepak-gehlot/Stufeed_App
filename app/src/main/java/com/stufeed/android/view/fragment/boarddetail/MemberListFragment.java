package com.stufeed.android.view.fragment.boarddetail;

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
import com.stufeed.android.api.response.GetBoardMemberListResponse;
import com.stufeed.android.databinding.FragmentMemberListBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.BoardMemberListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class MemberListFragment extends Fragment {

    public MemberListFragment() {
    }

    private String boardId = "";
    private String mLoginUserId = "";
    private boolean isAdmin = false;
    private FragmentMemberListBinding mBinding;

    public static MemberListFragment newInstance(String boardId, boolean isAdmin) {
        Bundle args = new Bundle();
        MemberListFragment fragment = new MemberListFragment();
        args.putString("board_id", boardId);
        args.putBoolean("is_admin", isAdmin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        boardId = bundle.getString("board_id");
        isAdmin = bundle.getBoolean("is_admin");
        mLoginUserId = Utility.getLoginUserId(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMemberList();
    }

    private void setRecyclerView(ArrayList<GetBoardMemberListResponse.User> userArrayList) {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BoardMemberListAdapter adapter = new BoardMemberListAdapter(getActivity(), userArrayList);
        mBinding.recyclerView.setAdapter(adapter);
    }

    private void getMemberList() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetBoardMemberListResponse> responseCall = api.getBoardMemberList(mLoginUserId, boardId);
        responseCall.enqueue(new Callback<GetBoardMemberListResponse>() {
            @Override
            public void onResponse(Call<GetBoardMemberListResponse> call,
                                   retrofit2.Response<GetBoardMemberListResponse> response) {
                GetBoardMemberListResponse boardMemberListResponse = response.body();
                if (boardMemberListResponse == null) {
                    Utility.showErrorMsg(getActivity());
                } else if (boardMemberListResponse.getResponseCode().equals(Api.SUCCESS)) {
                    setRecyclerView(boardMemberListResponse.getUserArrayList());
                }
            }

            @Override
            public void onFailure(Call<GetBoardMemberListResponse> call, Throwable t) {

            }
        });
    }
}
