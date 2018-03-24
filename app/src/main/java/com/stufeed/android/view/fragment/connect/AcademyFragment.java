package com.stufeed.android.view.fragment.connect;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.databinding.FragmentAcademyBinding;
import com.stufeed.android.view.adapter.ViewPagerAdapter;
import com.stufeed.android.view.fragment.BoardFragment;
import com.stufeed.android.view.fragment.FeedFragment;
import com.stufeed.android.view.fragment.PostFragment;
import com.stufeed.android.view.fragment.connect.academy.AcademyBoardListFragment;
import com.stufeed.android.view.fragment.connect.academy.EduKitFragment;

public class AcademyFragment extends Fragment {

    public AcademyFragment() {
        // Required empty public constructor
    }

    public static AcademyFragment newInstance() {
        Bundle args = new Bundle();
        AcademyFragment fragment = new AcademyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentAcademyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_academy, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager(binding.viewpager);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
        binding.viewpager.setOffscreenPageLimit(3);
    }

    /**
     * Set view pager adapter
     *
     * @param viewPager @ViewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(EduKitFragment.newInstance(), "EDUKIT");
        adapter.addFragment(FeedFragment.newInstance(), "POST");
        adapter.addFragment(AcademyBoardListFragment.newInstance(), "BOARD");
        viewPager.setAdapter(adapter);
    }
}
