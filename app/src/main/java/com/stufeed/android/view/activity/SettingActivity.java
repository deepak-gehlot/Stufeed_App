package com.stufeed.android.view.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetSettingResponse;
import com.stufeed.android.api.response.SaveSettingResponse;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.databinding.ActivitySettingBinding;
import com.stufeed.android.databinding.DialogInstituteCodeBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.PreferenceConnector;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    private String mSearch = "1", mSound = "1", mNotification = "1";
    private String mLoginUserId = "", mCollegeId = "";
    private String code = "";
    private boolean isStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_setting);
        binding.setActivity(this);
        binding.editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mLoginUserId = Utility.getLoginUserId(SettingActivity.this);
        getSettings();

        UserDetail userDetail = Utility.getLoginUserDetail(this);

        if (userDetail.getUserType().equals("4")) {
            binding.instituteCodeLayout.setVisibility(View.VISIBLE);
            mCollegeId = Utility.getLoginUserDetail(SettingActivity.this).getCollegeId();
        } else {
            binding.instituteCodeLayout.setVisibility(View.GONE);
            mCollegeId = Utility.getLoginUserDetail(SettingActivity.this).getCollegeId();
        }

        binding.switchInstituteCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !isStart) {
                    showCodeDialog();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isStart = false;
            }
        }, 50);
    }

    private void showCodeDialog() {
        final DialogInstituteCodeBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(SettingActivity.this),
                R.layout.dialog_institute_code, null, false);
        final Dialog dialog = new Dialog(SettingActivity.this);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(false);
        dialog.setTitle("Enter PIN Code");
        dialogBinding.editText.setText(code);
        dialog.show();


        dialogBinding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeStr = dialogBinding.editText.getText().toString().trim();
                if (!TextUtils.isEmpty(codeStr)) {
                    code = codeStr;
                    dialog.dismiss();

                }
            }
        });
        dialogBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                binding.switchInstituteCode.setChecked(false);
            }
        });

    }

    /**
     * Delete button click method
     */
    public void onDeleteButtonClick() {
        Utility.setDialog(SettingActivity.this, "Alert!", "Are you sure do your want to delete your account\n" +
                "this can not be recover.", "No", "Yes", new DialogListener() {
            @Override
            public void onNegative(DialogInterface dialog) {
                dialog.dismiss();
            }

            @Override
            public void onPositive(DialogInterface dialog) {
                dialog.dismiss();
                deleteApi();
            }
        });
    }

    /**
     * Save button click
     */
    public void onSaveButtonClick() {
        if (binding.switchSearch.isChecked()) {
            mSearch = "1";
        } else {
            mSearch = "0";
        }
        if (binding.switchSound.isChecked()) {
            mSound = "1";
        } else {
            mSound = "0";
        }
        if (binding.switchNotification.isChecked()) {
            mNotification = "1";
        } else {
            mNotification = "0";
        }
        if (!binding.switchInstituteCode.isChecked()) {
            code = "";
        }
        Api api = APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(SettingActivity.this);
        Call<SaveSettingResponse> responseCall = api.saveSetting(
                mLoginUserId, mCollegeId, mSearch, mSound, code, mNotification);
        responseCall.enqueue(new Callback<SaveSettingResponse>() {
            @Override
            public void onResponse(Call<SaveSettingResponse> call, Response<SaveSettingResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<SaveSettingResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                handleResponse(null);
            }
        });
    }

    private void handleResponse(SaveSettingResponse response) {
        if (response == null) {
            Utility.showErrorMsg(SettingActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            Utility.showToast(SettingActivity.this, "Setting updated.");
        }
    }

    private void getSettings() {
        ProgressDialog.getInstance().showProgressDialog(SettingActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetSettingResponse> responseCall = api.getSetting(mLoginUserId);
        responseCall.enqueue(new Callback<GetSettingResponse>() {
            @Override
            public void onResponse(Call<GetSettingResponse> call, Response<GetSettingResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleGetSettingResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetSettingResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                handleGetSettingResponse(null);
            }
        });
    }

    private void handleGetSettingResponse(GetSettingResponse response) {
        if (response == null) {
            Utility.showErrorMsg(SettingActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            GetSettingResponse.Setting setting = response.getSetting();
            mSearch = setting.getIsSearch();
            mSound = setting.getIsSound();
            mNotification = setting.getIsNotification();
            code = setting.getInstituteCode();
            setUi();
        }
    }

    private void setUi() {
        if (mSearch.equals("1")) {
            binding.switchSearch.setChecked(true);
        } else {
            binding.switchSearch.setChecked(false);
        }
        if (mSound.equals("1")) {
            binding.switchSound.setChecked(true);
        } else {
            binding.switchSound.setChecked(false);
        }
        if (mNotification.equals("1")) {
            binding.switchNotification.setChecked(true);
        } else {
            binding.switchNotification.setChecked(false);
        }
        if (Utility.getLoginUserDetail(SettingActivity.this).getUserType().equals("4")) {
            if (!TextUtils.isEmpty(code)) {
                binding.switchInstituteCode.setChecked(true);
            } else {
                binding.switchInstituteCode.setChecked(false);
            }
        }
    }

    private void deleteApi() {
        ProgressDialog.getInstance().showProgressDialog(SettingActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<com.stufeed.android.api.response.Response> responseCall = api.deleteuser(Utility.getLoginUserId(SettingActivity.this));
        responseCall.enqueue(new Callback<com.stufeed.android.api.response.Response>() {
            @Override
            public void onResponse(Call<com.stufeed.android.api.response.Response> call, Response<com.stufeed.android.api.response.Response> response) {
                handleDeleteResponse(response.body());
            }

            @Override
            public void onFailure(Call<com.stufeed.android.api.response.Response> call, Throwable t) {
                handleDeleteResponse(null);
            }
        });
    }

    private void handleDeleteResponse(com.stufeed.android.api.response.Response response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response != null) {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                Utility.showToast(SettingActivity.this, "Your account has been deleted.");
                PreferenceConnector.clear(SettingActivity.this);
                Intent intent = new Intent(SettingActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // call this to finish the current activity
            } else {
                Utility.showErrorMsg(SettingActivity.this);
            }
        } else {
            Utility.showErrorMsg(SettingActivity.this);
        }
    }
}