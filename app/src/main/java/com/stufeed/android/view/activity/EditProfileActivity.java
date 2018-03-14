package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        mBinding.setActivity(this);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onClickBasicInfo() {
        startActivity(new Intent(EditProfileActivity.this, EditBasicInfoActivity.class));
    }

    public void onClickAchievement() {
        startActivity(new Intent(EditProfileActivity.this, AchivementActivity.class));
    }

    public void onClickSkill() {
        startActivity(new Intent(EditProfileActivity.this, SkillsActivity.class));
    }

    public void onClickAbout() {

    }
}
