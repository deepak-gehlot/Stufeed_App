package com.stufeed.android.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetUserDescriptionResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.databinding.ActivityEditProfileBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding mBinding;
    private String mLoginUserId = "";
    private String description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        mBinding.setActivity(this);
        mLoginUserId = Utility.getLoginUserId(this);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getDescription();
    }

    public void onClickBasicInfo() {
        UserDetail userDetail = Utility.getLoginUserDetail(EditProfileActivity.this);
        if (userDetail.getUserType().equals("4")) {
            startActivity(new Intent(EditProfileActivity.this, EditInstituteActivity.class));
        } else {
            startActivity(new Intent(EditProfileActivity.this, EditBasicInfoActivity.class));
        }
    }

    public void onClickAchievement() {
        startActivity(new Intent(EditProfileActivity.this, AchivementActivity.class));
    }

    public void onClickSkill() {
        startActivity(new Intent(EditProfileActivity.this, SkillsActivity.class));
    }

    public void onClickAbout() {
        final Dialog dialog = new Dialog(EditProfileActivity.this);
        dialog.setContentView(R.layout.dialog_about);
        dialog.setTitle("About");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        final EditText editTextAbout = dialog.findViewById(R.id.editTextAbout);
        Button buttonSave = dialog.findViewById(R.id.buttonSave);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        editTextAbout.setText(description);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aboutStr = editTextAbout.getText().toString().trim();
                if (!TextUtils.isEmpty(aboutStr)) {
                    saveDescription(aboutStr);
                    dialog.dismiss();
                } else {
                    Utility.showToast(EditProfileActivity.this, "Enter about");
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * save user description
     */
    private void saveDescription(String description) {
        ProgressDialog.getInstance().showProgressDialog(EditProfileActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<Response> responseCall = api.saveUserDescription(mLoginUserId, description);
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                ProgressDialog.getInstance().dismissDialog();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
            }
        });

    }

    /**
     * Get user description
     */
    private void getDescription() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetUserDescriptionResponse> responseCall = api.getUserDescription(mLoginUserId);
        responseCall.enqueue(new Callback<GetUserDescriptionResponse>() {
            @Override
            public void onResponse(Call<GetUserDescriptionResponse> call,
                                   retrofit2.Response<GetUserDescriptionResponse> response) {
                GetUserDescriptionResponse response1 = response.body();
                if (response1 != null && response1.getResponseCode().equals(Api.SUCCESS)) {
                    description = response1.getDescription();
                }
            }

            @Override
            public void onFailure(Call<GetUserDescriptionResponse> call, Throwable t) {

            }
        });
    }
}