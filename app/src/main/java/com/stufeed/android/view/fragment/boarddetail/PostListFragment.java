package com.stufeed.android.view.fragment.boarddetail;


import android.app.Activity;
import android.content.Intent;
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
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.FragmentPostListBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.CommentPostActivity;
import com.stufeed.android.view.adapter.FeedListAdapter;
import com.stufeed.android.view.fragment.FeedFragment;

import java.util.ArrayList;

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
    private boolean isAdmin = false;
    private FragmentPostListBinding mBinding;

    public static PostListFragment newInstance(String boardId, boolean isAdmin) {
        Bundle args = new Bundle();
        PostListFragment fragment = new PostListFragment();
        args.putString("board_id", boardId);
        args.putBoolean("isAdmin", isAdmin);
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPostByBoard();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            FeedListAdapter adapter = (FeedListAdapter) mBinding.recyclerView.getAdapter();
            if (requestCode == 114) { // comment
                GetPostResponse.Post post = data.getParcelableExtra(CommentPostActivity.TAG_POST);
                int position = data.getIntExtra(CommentPostActivity.TAG_POSITION, -1);
                adapter.refreshItem(position, post);
            } else if (requestCode == 192) {
                getPostByBoard();
            } else {    // re post
                String boardId = data.getStringExtra("board_id");
                String postId = data.getStringExtra("post_id");
                adapter.rePost(postId, boardId);
            }
        }
    }

    private void getPostByBoard() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetPostResponse> responseCall = api.getPostByBoardId(mLoginUserId, boardId);
        responseCall.enqueue(new Callback<GetPostResponse>() {
            @Override
            public void onResponse(Call<GetPostResponse> call, retrofit2.Response<GetPostResponse> response) {
                handleGetAllPostResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetPostResponse> call, Throwable t) {
                handleGetAllPostResponse(null);
            }
        });
    }

    private void handleGetAllPostResponse(GetPostResponse getPostResponse) {
        if (getView() != null && getActivity() != null) {
            if (getPostResponse != null) {
                if (getPostResponse.getResponseCode().equals(Api.SUCCESS)) {
                    setRecyclerView(getPostResponse.getPost());
                } else {
                    mBinding.msgTxt.setVisibility(View.VISIBLE);
                    mBinding.msgTxt.setText("No post found.");
                    Utility.showToast(getActivity(), "No post found.");
                }
            } else {
                mBinding.msgTxt.setVisibility(View.VISIBLE);
                mBinding.msgTxt.setText(getString(R.string.wrong));
                Utility.showErrorMsg(getActivity());
            }
        }
    }

    private void setRecyclerView(ArrayList<GetPostResponse.Post> postArrayList) {
        for (int i = 0; i < postArrayList.size(); i++) {
            if (i != 0 && i % 4 == 0) {
                postArrayList.add(i, null);
            }
        }

        if (getView() != null && getActivity() != null) {
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.msgTxt.setVisibility(View.GONE);
            mBinding.recyclerView.setVisibility(View.VISIBLE);
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            FeedListAdapter adapter = new FeedListAdapter(PostListFragment.this, postArrayList,false);
            mBinding.recyclerView.setAdapter(adapter);
        }
    }
}