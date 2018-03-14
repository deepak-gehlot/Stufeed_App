package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivitySkillsBinding;

import java.util.ArrayList;

import me.gujun.android.taggroup.TagGroup;

public class SkillsActivity extends AppCompatActivity {

    private ActivitySkillsBinding mBinding;
    private ArrayList<String> tagsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_skills);
        mBinding.setActivity(this);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onClickAdd() {
        String skills = mBinding.editTextSkill.getText().toString().trim();
        if (!TextUtils.isEmpty(skills)) {
            if (tagsList.size() < 15) {
                tagsList.add(skills);
                TagGroup mTagGroup = (TagGroup) findViewById(R.id.tag_group);
                mTagGroup.setTags();
                mTagGroup.setTags(tagsList);
                mBinding.editTextSkill.getText().clear();
            }
        }
    }
}