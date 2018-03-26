package com.stufeed.android.view.fragment;

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
import com.stufeed.android.databinding.FragmentYouBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.EditBasicInfoActivity;
import com.stufeed.android.view.activity.EditProfileActivity;
import com.stufeed.android.view.activity.ViewFullProfileActivity;
import com.stufeed.android.view.adapter.FeedListAdapter;

public class YouFragment extends Fragment {

    public YouFragment() {
        // Required empty public constructor
    }

    private FragmentYouBinding binding;

    public static YouFragment newInstance() {
        Bundle args = new Bundle();
        YouFragment fragment = new YouFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_you, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        setRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void onEditButtonClick() {
        startActivity(new Intent(getActivity(), EditProfileActivity.class));
    }

    public void onViewButtonClick() {
        startActivity(new Intent(getActivity(), ViewFullProfileActivity.class));
    }

    private void setRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*FeedListAdapter adapter = new FeedListAdapter(getActivity());
        binding.recyclerView.setAdapter(adapter);*/
    }

}
