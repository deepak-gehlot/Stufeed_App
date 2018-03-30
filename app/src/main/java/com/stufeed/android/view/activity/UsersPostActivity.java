package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityUsersPostBinding;

public class UsersPostActivity extends AppCompatActivity {

    private ActivityUsersPostBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_users_post);

    }

    private void getPostList(){

    }
}
