package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.ActivitySkillsBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;

import java.util.ArrayList;

import me.gujun.android.taggroup.TagGroup;
import retrofit2.Call;
import retrofit2.Callback;

public class SkillsActivity extends AppCompatActivity {

    private ActivitySkillsBinding mBinding;
    private ArrayList<String> tagsList = new ArrayList<>();
    private String mLoginUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_skills);
        mLoginUserId = Utility.getLoginUserId(this);
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

    public void onSaveButtonClick() {
        if (tagsList.size() == 0) {
            return;
        }
        ProgressDialog.getInstance().showProgressDialog(SkillsActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        String skills = TextUtils.join(",", tagsList);
        Call<Response> responseCall = api.saveSkills(mLoginUserId, skills);
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleSaveSkillsResponse(response.body());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                handleSaveSkillsResponse(null);
            }
        });
    }

    private void handleSaveSkillsResponse(Response response) {
        if (response == null) {
            Utility.showErrorMsg(SkillsActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            Utility.showToast(SkillsActivity.this, response.getResponseMessage());
            finish();
        } else {
            Utility.showToast(SkillsActivity.this, response.getResponseMessage());
        }
    }
}