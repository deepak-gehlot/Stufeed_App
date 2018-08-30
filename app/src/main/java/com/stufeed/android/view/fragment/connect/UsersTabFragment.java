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
import com.stufeed.android.databinding.FragmentUsersTabBinding;
import com.stufeed.android.view.activity.HomeActivity;
import com.stufeed.android.view.adapter.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTabFragment extends Fragment {


    public UsersTabFragment() {
        // Required empty public constructor
    }

    public static UsersTabFragment newInstance() {
        Bundle args = new Bundle();
        UsersTabFragment fragment = new UsersTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentUsersTabBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users_tab, container, false);
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
        ((HomeActivity) getActivity()).showHideSearchIcon(1, true);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
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
                ((HomeActivity) getActivity()).showHideSearchIcon(position + 1, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
