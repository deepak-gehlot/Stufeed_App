package com.stufeed.android.view.fragment;


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
import com.stufeed.android.databinding.FragmentConnectBinding;
import com.stufeed.android.view.adapter.ViewPagerAdapter;
import com.stufeed.android.view.fragment.connect.AcademyFragment;
import com.stufeed.android.view.fragment.connect.DepartmentFragment;
import com.stufeed.android.view.fragment.connect.FacultyFragment;
import com.stufeed.android.view.fragment.connect.StudentFragment;

public class ConnectFragment extends Fragment {

    public ConnectFragment() {
        // Required empty public constructor
    }

    private FragmentConnectBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_connect, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager(binding.viewpager);
        binding.tabLayout.setupWithViewPager(binding.viewpager);

    }

    /**
     * Set view pager adapter
     *
     * @param viewPager @ViewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(AcademyFragment.newInstance(), getString(R.string.academy));
        adapter.addFragment(FacultyFragment.newInstance(), getString(R.string.faculty));
        adapter.addFragment(StudentFragment.newInstance(), getString(R.string.student));
        adapter.addFragment(DepartmentFragment.newInstance(), getString(R.string.department));
        viewPager.setAdapter(adapter);
    }
}