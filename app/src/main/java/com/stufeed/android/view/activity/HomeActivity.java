package com.stufeed.android.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.GetSettingResponse;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.bean.DrawerItem;
import com.stufeed.android.databinding.ActivityHomeBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.listener.OnItemClickListener;
import com.stufeed.android.util.PreferenceConnector;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.DrawrAdapter;
import com.stufeed.android.view.fragment.BoardFragment;
import com.stufeed.android.view.fragment.ConnectFragment;
import com.stufeed.android.view.fragment.FeedFragment;
import com.stufeed.android.view.fragment.YouFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeBinding binding;
    private int searchType = 0;
    private String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utility.getLoginUserDetail(HomeActivity.this) != null) {
            handleIntent();

            MobileAds.initialize(this, getString(R.string.ad_mob_id));
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

            binding.profileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userType.equals("4")) {
                        startActivity(new Intent(HomeActivity.this, MyProfileMainActivity.class));
                    } else {
                        GetCollegeUserResponse.User user = new GetCollegeUserResponse.User();
                        user.setUserId(Utility.getLoginUserId(HomeActivity.this));
                        user.setFullName(Utility.getLoginUserDetail(HomeActivity.this).getFullName());
                        user.setIsFollow("0");
                        Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                }
            });

            binding.searchImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchType != 0) {
                        binding.toolBar.setVisibility(View.GONE);
                        binding.searchLayout.setVisibility(View.VISIBLE);
                        binding.editText.requestFocus();
                        Utility.openKeyboard(HomeActivity.this);
                    /*Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                    intent.putExtra("type", searchType);
                    startActivity(intent);*/
                    }
                }
            });


            binding.closeSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchType != 0) {
                        binding.toolBar.setVisibility(View.VISIBLE);
                        binding.searchLayout.setVisibility(View.GONE);
                        binding.editText.getText().clear();
                        setResult("");
                        Utility.closeKeyboard(HomeActivity.this, binding.editText);
                    }
                }
            });

            binding.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String searchTxt = binding.editText.getText().toString().trim();
                    setResult(searchTxt);
                }
            });


            String userId = Utility.getLoginUserId(this);

            Utility.addFragment(this, FeedFragment.newInstance(userId), "FeedFragment", binding.frame.getId());

            UserDetail userDetail = Utility.getLoginUserDetail(this);
            userType = userDetail.getUserType();
            setNavigationList();

            getSettings();
        } else {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AQuery aQuery = new AQuery(HomeActivity.this);
        String profilePic = Utility.getLoginUserDetail(HomeActivity.this).getProfilePic();
        aQuery.id(binding.profilePic).image(profilePic, true, true, 100, R.mipmap.ic_launcher_round);

        if (searchType != 0) {
            binding.toolBar.setVisibility(View.VISIBLE);
            binding.searchLayout.setVisibility(View.GONE);
            binding.editText.getText().clear();
            setResult("");
        }
    }

    @Override
    public void onBackPressed() {
        int selectedId = binding.bottomNavigation.getSelectedItemId();
        switch (selectedId) {
            case R.id.navigation_connect:
            /*case R.id.navigation_person:*/
            case R.id.navigation_post:
            case R.id.navigation_board:
                binding.bottomNavigation.setSelectedItemId(R.id.navigation_feed);
                String userId = Utility.getLoginUserId(HomeActivity.this);
                Utility.addFragment(this, FeedFragment.newInstance(userId), "FeedFragment", binding.frame.getId());
                break;
            case R.id.navigation_feed:
                finish();
                break;

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.navigation_person:
                Utility.addFragment(this, YouFragment.newInstance(), "YouFragment", binding.frame.getId());
                break;*/
            case R.id.navigation_connect:
                Utility.addFragment(this, ConnectFragment.newInstance(), "ConnectFragment", binding.frame.getId());
                break;
            case R.id.navigation_post:  // PostActivity
                if (userType.equals("4")) {
                    onClickInstitutePost();
                } else {
                    startActivity(new Intent(HomeActivity.this, PostActivity.class));
                }
                return false;
            case R.id.navigation_board:
                Utility.addFragment(this, BoardFragment.newInstance(), "BoardFragment", binding.frame.getId());
                break;
            case R.id.navigation_feed:
                String userId = Utility.getLoginUserId(HomeActivity.this);
                Utility.addFragment(this, FeedFragment.newInstance(userId), "FeedFragment", binding.frame.getId());
                break;
        }
        return true;
    }

    private void handleIntent() {
        if (Utility.getLoginUserDetail(HomeActivity.this) != null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String value1 = extras.getString(Intent.EXTRA_TEXT);
                if (!TextUtils.isEmpty(value1)) {
                    if (userType.equals("4")) {
                        onClickInstitutePost();
                    } else {
                        Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT, value1);
                        startActivity(intent);
                    }
                }
            }
        } else {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }
    }

    public void onClickInstitutePost() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomeActivity.this);
        bottomSheetDialog.setContentView(R.layout.dialog_type_selection);
        TextView boardTxt = bottomSheetDialog.findViewById(R.id.txtBoard);
        TextView edukitTxt = bottomSheetDialog.findViewById(R.id.txtEdukit);
        boardTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                onClickBoard();
            }
        });
        edukitTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                onClickEdukit();
            }
        });
        bottomSheetDialog.show();
    }

    /**
     * Select board click
     */
    public void onClickBoard() {
        Intent intent = new Intent(HomeActivity.this, InstitutePostActivity.class);
        intent.putExtra("code", InstitutePostActivity.SELECT_BOARD);
        startActivity(intent);
        // mBinding.postSelectionLayout.setVisibility(View.GONE);
    }

    /**
     * Select Edukit click
     */
    public void onClickEdukit() {
        Intent intent = new Intent(HomeActivity.this, InstitutePostActivity.class);
        intent.putExtra("code", InstitutePostActivity.SELECT_EDUKIT);
        startActivity(intent);
        //  mBinding.postSelectionLayout.setVisibility(View.GONE);
    }


    private void setResult(String searchTxt) {
        Intent intent = new Intent();
        intent.setAction("com.stufeed.android.search");
        intent.putExtra("type", searchType);
        intent.putExtra("search", searchTxt);
        sendBroadcast(intent);
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
            binding.toolBar.setVisibility(View.VISIBLE);
            binding.searchLayout.setVisibility(View.GONE);
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
                   /* case 4:
                        break;*/
                    case 4:
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;
                    case 5:
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
      //  drawerItems.add(drawerItem5);
        drawerItems.add(drawerItem6);
        drawerItems.add(drawerItem7);
        return drawerItems;
    }

    private void getSettings() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetSettingResponse> responseCall = api.getSetting(Utility.getLoginUserId(HomeActivity.this));
        responseCall.enqueue(new Callback<GetSettingResponse>() {
            @Override
            public void onResponse(Call<GetSettingResponse> call, Response<GetSettingResponse> response) {
                handleGetSettingResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetSettingResponse> call, Throwable t) {
                handleGetSettingResponse(null);
            }
        });
    }

    private void handleGetSettingResponse(GetSettingResponse response) {
        if (response != null && response.getResponseCode().equals(Api.SUCCESS)) {
            GetSettingResponse.Setting setting = response.getSetting();
            String strSetting = new Gson().toJson(setting);
            PreferenceConnector.writeString(HomeActivity.this, PreferenceConnector.USER_SETTING, strSetting);
        }
    }
}

