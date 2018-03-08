package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.bean.DrawerItem;
import com.stufeed.android.databinding.ActivityHomeBinding;
import com.stufeed.android.listener.OnItemClickListener;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.DrawrAdapter;
import com.stufeed.android.view.fragment.BoardFragment;
import com.stufeed.android.view.fragment.ConnectFragment;
import com.stufeed.android.view.fragment.FeedFragment;
import com.stufeed.android.view.fragment.PostFragment;
import com.stufeed.android.view.fragment.YouFragment;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
        setSupportActionBar(binding.toolBar);
        binding.toolBar.setNavigationIcon(R.drawable.menu_icon);
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(Gravity.START);
            }
        });

        binding.notificationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
            }
        });
        Utility.addFragment(this, YouFragment.newInstance(), "YouFragment", binding.frame.getId());

        setNavigationList();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_person:
                Utility.addFragment(this, YouFragment.newInstance(), "YouFragment", binding.frame.getId());
                break;
            case R.id.navigation_connect:
                Utility.addFragment(this, ConnectFragment.newInstance(), "ConnectFragment", binding.frame.getId());
                break;
            case R.id.navigation_post:
                startActivity(new Intent(HomeActivity.this, PostActivity.class));
                return false;
            case R.id.navigation_board:
                Utility.addFragment(this, BoardFragment.newInstance(), "BoardFragment", binding.frame.getId());
                break;
            case R.id.navigation_feed:
                Utility.addFragment(this, FeedFragment.newInstance(), "FeedFragment", binding.frame.getId());
                break;
        }
        return true;
    }

    /**
     * Set navigation list
     */
    private void setNavigationList() {
        ArrayList<DrawerItem> drawerItems = createDrawerList();
        binding.recyclerViewNavigation.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        DrawrAdapter drawrAdapter = new DrawrAdapter(HomeActivity.this, drawerItems);
        binding.recyclerViewNavigation.setAdapter(drawrAdapter);
        drawrAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position, Object obj) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this, MyBookmarkListActivity.class));
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;
                    case 6:
                        break;
                }
            }
        });
    }

    /**
     * Create navigation list
     *
     * @return ArrayList<DrawerItem>
     */
    private ArrayList<DrawerItem> createDrawerList() {
        ArrayList<DrawerItem> drawerItems = new ArrayList<>();
        DrawerItem drawerItem1 = new DrawerItem(getString(R.string.people_you_follow), R.drawable.people_follow_icon);
        DrawerItem drawerItem2 = new DrawerItem(getString(R.string.saved_post), R.drawable.bookmark_icon);
        DrawerItem drawerItem3 = new DrawerItem(getString(R.string.archive_board), R.drawable.archive_icon);
        DrawerItem drawerItem4 = new DrawerItem(getString(R.string.blocked_user), R.drawable.block_icon);
        DrawerItem drawerItem5 = new DrawerItem(getString(R.string.library_board), R.drawable.block_icon);
        DrawerItem drawerItem6 = new DrawerItem(getString(R.string.settings), R.drawable.settings_icon);
        DrawerItem drawerItem7 = new DrawerItem(getString(R.string.logout), R.drawable.logout_icon);
        drawerItems.add(drawerItem1);
        drawerItems.add(drawerItem2);
        drawerItems.add(drawerItem3);
        drawerItems.add(drawerItem4);
        drawerItems.add(drawerItem5);
        drawerItems.add(drawerItem6);
        drawerItems.add(drawerItem7);
        return drawerItems;
    }
}

