package com.stufeed.android.view.fragment.boarddetail;


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
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.FragmentPostListBinding;
import com.stufeed.android.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostListFragment extends Fragment {


    public PostListFragment() {
        // Required empty public constructor
    }

    private String boardId = "";
    private String mLoginUserId = "";
    private FragmentPostListBinding mBinding;

    public static PostListFragment newInstance(String boardId) {
        Bundle args = new Bundle();
        PostListFragment fragment = new PostListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        boardId = bundle.getString("board_id");
        mLoginUserId = Utility.getLoginUserId(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPostByBoard();
    }


    private void getPostByBoard() {
        Api api = APIClient.getClient().create(Api.class);
        Call<Response> responseCall = api.getPostByBoardId(mLoginUserId, boardId);
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

}