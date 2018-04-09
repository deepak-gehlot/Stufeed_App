package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityBlockedDetailsMainBinding;
import com.stufeed.android.view.adapter.ViewPagerAdapter;
import com.stufeed.android.view.fragment.boarddetail.MemberListFragment;
import com.stufeed.android.view.fragment.boarddetail.PostListFragment;

public class BoardDetailsMainActivity extends AppCompatActivity {

    private ActivityBlockedDetailsMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_blocked_details_main);
        setupViewPager(mBinding.viewPager);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    /**
     * Set view pager adapter
     *
     * @param viewPager @ViewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PostListFragment.newInstance(), getString(R.string.board_post));
        adapter.addFragment(MemberListFragment.newInstance(), getString(R.string.board_member));
        viewPager.setAdapter(adapter);
    }

}
