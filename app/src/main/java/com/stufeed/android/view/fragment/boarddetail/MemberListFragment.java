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
import com.stufeed.android.databinding.FragmentMemberListBinding;
import com.stufeed.android.view.adapter.BoardMemberListAdapter;


public class MemberListFragment extends Fragment {

    public MemberListFragment() {
    }

    public static MemberListFragment newInstance() {
        Bundle args = new Bundle();
        MemberListFragment fragment = new MemberListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentMemberListBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();
    }

    private void setRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BoardMemberListAdapter adapter = new BoardMemberListAdapter(getActivity());
        mBinding.recyclerView.setAdapter(adapter);
    }
}
