package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityBlockedDetailsMainBinding;
import com.stufeed.android.view.adapter.ViewPagerAdapter;
import com.stufeed.android.view.fragment.boarddetail.MemberListFragment;
import com.stufeed.android.view.fragment.boarddetail.PostListFragment;

public class BoardDetailsMainActivity extends AppCompatActivity {

    private ActivityBlockedDetailsMainBinding mBinding;
    private String boardId = "";
    private boolean isAdmin = false;
    private String boardName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_blocked_details_main);
        MobileAds.initialize(this,
                getString(R.string.ad_mob_id));
        getDataFromBundle();
        setupViewPager(mBinding.viewPager);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        mBinding.toolbar.setTitle(boardName);


        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
        } else {
            boardId = bundle.getString("board_id");
            isAdmin = bundle.getBoolean("is_admin");
            boardName=bundle.getString("BoardName");
        }
    }

    /**
     * Set view pager adapter
     *
     * @param viewPager @ViewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PostListFragment.newInstance(boardId, isAdmin), getString(R.string.board_post));
        adapter.addFragment(MemberListFragment.newInstance(boardId, isAdmin), getString(R.string.board_member));
        viewPager.setAdapter(adapter);
    }

}
