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
import com.stufeed.android.databinding.FragmentPostListBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostListFragment extends Fragment {


    public PostListFragment() {
        // Required empty public constructor
    }

    public static PostListFragment newInstance() {
        Bundle args = new Bundle();
        PostListFragment fragment = new PostListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentPostListBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}