package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityViewFullProfileBinding;
import com.stufeed.android.view.adapter.ViewPagerAdapter;
import com.stufeed.android.view.fragment.connect.AcademyFragment;
import com.stufeed.android.view.fragment.connect.DepartmentFragment;
import com.stufeed.android.view.fragment.connect.FacultyFragment;
import com.stufeed.android.view.fragment.connect.StudentFragment;
import com.stufeed.android.view.fragment.profile.AchievementsFragment;
import com.stufeed.android.view.fragment.profile.BasicInfoFragment;
import com.stufeed.android.view.fragment.profile.SkillsFragment;

public class ViewFullProfileActivity extends AppCompatActivity {

    private ActivityViewFullProfileBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_full_profile);
        setupViewPager(mBinding.viewpager);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewpager);
        mBinding.viewpager.setOffscreenPageLimit(4);

        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Set view pager adapter
     *
     * @param viewPager @ViewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BasicInfoFragment.newInstance(), getString(R.string.general));
        adapter.addFragment(AchievementsFragment.newInstance(), getString(R.string.achievement));
        adapter.addFragment(SkillsFragment.newInstance(), getString(R.string.skill));
        viewPager.setAdapter(adapter);
    }
}
