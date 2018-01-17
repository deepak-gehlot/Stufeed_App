package com.stufeed.android.view.activity;

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
import com.stufeed.android.view.adapter.DrawrAdapter;

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

        setNavigationList();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_person:
                break;
            case R.id.navigation_connect:
                break;
            case R.id.navigation_post:
                break;
            case R.id.navigation_board:
                break;
            case R.id.navigation_feed:
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
        binding.recyclerViewNavigation.setAdapter(new DrawrAdapter(HomeActivity.this, drawerItems));
    }

    /**
     * Create navigation list
     *
     * @return ArrayList<DrawerItem>
     */
    private ArrayList<DrawerItem> createDrawerList() {
        ArrayList<DrawerItem> drawerItems = new ArrayList<>();
        DrawerItem drawerItem1 = new DrawerItem(getString(R.string.people_you_follow), 0);
        DrawerItem drawerItem2 = new DrawerItem(getString(R.string.saved_post), 0);
        DrawerItem drawerItem3 = new DrawerItem(getString(R.string.archive_board), 0);
        DrawerItem drawerItem4 = new DrawerItem(getString(R.string.blocked_user), 0);
        DrawerItem drawerItem5 = new DrawerItem(getString(R.string.library_board), 0);
        DrawerItem drawerItem6 = new DrawerItem(getString(R.string.settings), 0);
        DrawerItem drawerItem7 = new DrawerItem(getString(R.string.logout), 0);
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

