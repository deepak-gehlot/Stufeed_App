package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.google.gson.Gson;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAllSkillsResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.bean.Branch;
import com.stufeed.android.databinding.ActivitySkillsBinding;
import com.stufeed.android.util.AssetsUtil;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SkillsActivity extends AppCompatActivity {

    private ActivitySkillsBinding mBinding;
    private List<String> skillsList = new ArrayList<>();
    private List<String> tagsList = new ArrayList<>();
    private List<Tag> tagsListTags = new ArrayList<>();
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

        mBinding.tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(TagView tagView, Tag tag, int i) {
                mBinding.tagGroup.remove(i);
                tagsList.remove(i);
            }
        });

        getAllSkills();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSkillLocalList();
            }
        }).start();
        ArrayAdapter<String> adapterDesignation = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, skillsList);
        mBinding.editTextSkill.setAdapter(adapterDesignation);
    }


    private void getSkillLocalList() {
        String json = AssetsUtil.ReadFromfile("skills.json", SkillsActivity.this);
        Branch[] branches = new Gson().fromJson(json, Branch[].class);
        for (Branch branch : branches) {
            skillsList.add(branch.getFIELD1());
        }
    }

    public void onClickAdd() {
        String skills = mBinding.editTextSkill.getText().toString().trim();
        if (!TextUtils.isEmpty(skills)) {
            if (tagsList.size() < 15) {
                tagsList.add(skills);
                Tag tag = new Tag(skills);
                tag.isDeletable = true;
                mBinding.tagGroup.addTag(tag);
                tag.layoutColor= ContextCompat.getColor(SkillsActivity.this,R.color.topheader);
                mBinding.editTextSkill.getText().clear();
            }
        }
    }

    /**
     * Save button click listener
     */
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

    /**
     * Get all skills list
     */
    public void getAllSkills() {
        ProgressDialog.getInstance().showProgressDialog(SkillsActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetAllSkillsResponse> responseCall = api.getUserSkills(mLoginUserId);
        responseCall.enqueue(new Callback<GetAllSkillsResponse>() {
            @Override
            public void onResponse(Call<GetAllSkillsResponse> call, retrofit2.Response<GetAllSkillsResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleGetUserSkillResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetAllSkillsResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                handleGetUserSkillResponse(null);
            }
        });
    }

    /**
     * Get all skills response
     */
    private void handleGetUserSkillResponse(GetAllSkillsResponse response) {
        if (response != null) {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                String allSkills = response.getAllSkills();
                String skills[] = allSkills.split(",");
                for (int i = 0; i < skills.length; i++) {
                    tagsList.add(skills[i]);
                    Tag tag = new Tag(skills[i]);
                    tag.isDeletable = true;
                    tag.layoutColor= ContextCompat.getColor(SkillsActivity.this,R.color.topheader);
                   // tag.tagTextColor = getColor(R.color.topheader);
                    tagsListTags.add(tag);
                }
                mBinding.tagGroup.addTags(tagsListTags);
            }
        }
    }
}