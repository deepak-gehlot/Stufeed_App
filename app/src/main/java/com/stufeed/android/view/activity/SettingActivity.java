package com.stufeed.android.view.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetSettingResponse;
import com.stufeed.android.api.response.SaveSettingResponse;
import com.stufeed.android.databinding.ActivitySettingBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    private String mSearch = "1", mSound = "1", mNotification = "1";
    private String mLoginUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setActivity(this);
        binding.editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mLoginUserId = Utility.getLoginUserId(SettingActivity.this);
        getSettings();
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
        Api api = APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(SettingActivity.this);
        Call<SaveSettingResponse> responseCall = api.saveSetting(mLoginUserId, mSearch, mSound, mNotification);
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
        }
    }
}