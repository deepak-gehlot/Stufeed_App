package com.stufeed.android.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.AQuery;
import com.stufeed.android.R;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.bean.DrawerItem;
import com.stufeed.android.databinding.ActivityHomeBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.listener.OnItemClickListener;
import com.stufeed.android.util.PreferenceConnector;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.DrawrAdapter;
import com.stufeed.android.view.fragment.BoardFragment;
import com.stufeed.android.view.fragment.ConnectFragment;
import com.stufeed.android.view.fragment.FeedFragment;
import com.stufeed.android.view.fragment.YouFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeBinding binding;
    private int searchType = 0;
    private String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        disableShiftMode(binding.bottomNavigation);
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

        binding.searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchType != 0) {
                    Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                    intent.putExtra("type", searchType);
                    startActivity(intent);
                }
            }
        });

        Utility.addFragment(this, FeedFragment.newInstance(), "FeedFragment", binding.frame.getId());

        UserDetail userDetail = Utility.getLoginUserDetail(this);
        userType = userDetail.getUserType();
        setNavigationList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AQuery aQuery = new AQuery(HomeActivity.this);
        String profilePic = Utility.getLoginUserDetail(HomeActivity.this).getProfilePic();
        aQuery.id(binding.profilePic).image(profilePic, true, true, 100, R.drawable.user_default);
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
            case R.id.navigation_post:  // PostActivity
                if (userType.equals("4")) {
                    startActivity(new Intent(HomeActivity.this, InstitutePostActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, PostActivity.class));
                }
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

    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }

    /**
     * show hide search icon function
     */
    public void showHideSearchIcon(int position, boolean show) {
        searchType = position;
        if (show) {
            binding.searchImg.setVisibility(View.VISIBLE);
        } else {
            binding.searchImg.setVisibility(View.GONE);
        }
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
                        Intent intent = new Intent(HomeActivity.this, UserFollowingActivity.class);
                        intent.putExtra("user_id", Utility.getLoginUserId(HomeActivity.this));
                        startActivity(intent);
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this, MyBookmarkListActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this, ArchiveBoardListActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(HomeActivity.this, BlockedUserListActivity.class));
                        break;
                    case 4:
                        break;
                    case 5:
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;
                    case 6:
                        showLogoutConfirmation();
                        break;
                }
            }
        });
    }

    /**
     * Show logout confirmation dialog
     */
    public void showLogoutConfirmation() {
        Utility.setDialog(HomeActivity.this, "Alert!", "Do you want to logout?",
                "No", "Yes", new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        PreferenceConnector.clear(HomeActivity.this);
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        finish();
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

