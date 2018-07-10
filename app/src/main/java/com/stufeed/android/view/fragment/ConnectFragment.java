package com.stufeed.android.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.databinding.FragmentConnectBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.HomeActivity;
import com.stufeed.android.view.adapter.ViewPagerAdapter;
import com.stufeed.android.view.fragment.connect.AcademyFragment;
import com.stufeed.android.view.fragment.connect.DepartmentFragment;
import com.stufeed.android.view.fragment.connect.FacultyFragment;
import com.stufeed.android.view.fragment.connect.StudentFragment;
import com.stufeed.android.view.fragment.connect.UsersTabFragment;

public class ConnectFragment extends Fragment {

    public ConnectFragment() {
        // Required empty public constructor
    }

    private FragmentConnectBinding binding;

    public static ConnectFragment newInstance() {

        Bundle args = new Bundle();

        ConnectFragment fragment = new ConnectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_connect, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*setupViewPager(binding.viewpager);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
        binding.viewpager.setOffscreenPageLimit(4);*/

        setSelected(0);
        binding.textViewInstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(0);
            }
        });

        binding.textViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(1);
            }
        });
    }

    private void setSelected(int i) {
        if (i == 0) {
            binding.textViewInstitute.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_selected));
            binding.textViewInstitute.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            binding.textViewUser.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color));
            binding.textViewUser.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
            Utility.addChildFragment(ConnectFragment.this, AcademyFragment.newInstance(), "AcademyFragment", R.id.frame);
        } else {
            binding.textViewUser.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_selected));
            binding.textViewInstitute.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
            binding.textViewInstitute.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color));
            binding.textViewUser.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            Utility.addChildFragment(ConnectFragment.this, UsersTabFragment.newInstance(), "UsersTabFragment", R.id.frame);
        }
    }

    /* *//**
     * Set view pager adapter
     *
     * @param viewPager @ViewPager
     *//*
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(AcademyFragment.newInstance(), getString(R.string.academy));
        adapter.addFragment(FacultyFragment.newInstance(), getString(R.string.faculty));
        adapter.addFragment(StudentFragment.newInstance(), getString(R.string.student));
        adapter.addFragment(DepartmentFragment.newInstance(), getString(R.string.department));
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    ((HomeActivity) getActivity()).showHideSearchIcon(position, true);
                } else {
                    ((HomeActivity) getActivity()).showHideSearchIcon(position, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }*/
}