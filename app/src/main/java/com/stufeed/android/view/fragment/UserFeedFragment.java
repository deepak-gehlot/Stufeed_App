package com.stufeed.android.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.databinding.FragmentFeedBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.CommentPostActivity;
import com.stufeed.android.view.activity.HomeActivity;
import com.stufeed.android.view.adapter.FeedListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentFeedBinding binding;

    public UserFeedFragment() {
        // Required empty public constructor
    }

    public static UserFeedFragment newInstance() {
        Bundle args = new Bundle();
        UserFeedFragment fragment = new UserFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllPost();
        binding.pullToRefresh.setOnRefreshListener(this);

        ((HomeActivity) getActivity()).showHideSearchIcon(0, false);
    }

    @Override
    public void onRefresh() {
        getAllPost();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            FeedListAdapter adapter = (FeedListAdapter) binding.recyclerView.getAdapter();
            if (requestCode == 114) { // comment
                GetPostResponse.Post post = data.getParcelableExtra(CommentPostActivity.TAG_POST);
                int position = data.getIntExtra(CommentPostActivity.TAG_POSITION, -1);
                adapter.refreshItem(position, post);
            } else if (requestCode == 192) {
                getAllPost();
            } else {    // re post
                String boardId = data.getStringExtra("board_id");
                String postId = data.getStringExtra("post_id");
                adapter.rePost(postId, boardId);
            }
        }
    }

    private void getAllPost() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.msgTxt.setVisibility(View.GONE);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetPostResponse> responseCall = api.getUserAllPost(Utility.getLoginUserId(getActivity()
        ));
        responseCall.enqueue(new Callback<GetPostResponse>() {
            @Override
            public void onResponse(Call<GetPostResponse> call, Response<GetPostResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.pullToRefresh.setRefreshing(false);
                handleGetAllPostResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetPostResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.msgTxt.setVisibility(View.VISIBLE);
                binding.msgTxt.setText(getString(R.string.wrong));
                binding.pullToRefresh.setRefreshing(false);
                Utility.showToast(getActivity(), getString(R.string.wrong));
            }
        });
    }

    private void handleGetAllPostResponse(GetPostResponse getPostResponse) {
        if (getPostResponse != null) {
            if (getPostResponse.getResponseCode().equals(Api.SUCCESS)) {
                setRecyclerView(getPostResponse.getPost());
            } else {
                binding.msgTxt.setVisibility(View.VISIBLE);
                binding.msgTxt.setText("No post found.");
                Utility.showToast(getActivity(), "No post found.");
            }
        } else {
            binding.msgTxt.setVisibility(View.VISIBLE);
            binding.msgTxt.setText(getString(R.string.wrong));
            Utility.showErrorMsg(getActivity());
        }
    }

    private void setRecyclerView(ArrayList<GetPostResponse.Post> postArrayList) {
        binding.progressBar.setVisibility(View.GONE);
        binding.msgTxt.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FeedListAdapter adapter = new FeedListAdapter(UserFeedFragment.this, postArrayList);
        binding.recyclerView.setAdapter(adapter);
    }
}