package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.LoginResponse;
import com.stufeed.android.api.response.VerifyResponse;
import com.stufeed.android.databinding.ActivityVerifyAccountBinding;
import com.stufeed.android.util.PreferenceConnector;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyAccountActivity extends AppCompatActivity {

    private ActivityVerifyAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_account);
        binding.setActivity(this);
    }

    public void onVerifyClick() {
        String code = binding.edtOtp.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            // Utility.showToast(VerifyAccountActivity.this, "Enter OTP");
            try {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Utility.showToast(VerifyAccountActivity.this, "No app found.");
            }
        } else {
            PreferenceConnector.writeString(VerifyAccountActivity.this, PreferenceConnector.VERIFY, "1");
            onLoginBtnClick();
            // verify(code);
        }
    }

    private void verify(String code) {
        Api api = APIClient.getClient().create(Api.class);
        String userId = Utility.getLoginUserId(VerifyAccountActivity.this);
        Call<VerifyResponse> responseCall = api.verifyEmail(userId, code);
        ProgressDialog.getInstance().showProgressDialog(VerifyAccountActivity.this);
        responseCall.enqueue(new Callback<VerifyResponse>() {
            @Override
            public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleVerifyResponse(response.body());
            }

            @Override
            public void onFailure(Call<VerifyResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(VerifyAccountActivity.this);
            }
        });
    }

    private void handleVerifyResponse(VerifyResponse response) {
        if (response == null) {
            Utility.showErrorMsg(VerifyAccountActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            PreferenceConnector.writeString(VerifyAccountActivity.this, PreferenceConnector.VERIFY, "1");
            startActivity(new Intent(VerifyAccountActivity.this, LoginActivity.class));
            finish();
        } else {
            Utility.showToast(VerifyAccountActivity.this, response.getResponseMessage());
        }
    }

    /**
     * On Login button click method
     */
    public void onLoginBtnClick() {
        Api api = APIClient.getClient().create(Api.class);
        String userId = Utility.getLoginUserId(VerifyAccountActivity.this);
        final Call<LoginResponse> responseCall = api.getLoginUserDetails(userId);
        ProgressDialog.getInstance().showProgressDialog(VerifyAccountActivity.this);
        responseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                responseCall.cancel();
                ProgressDialog.getInstance().dismissDialog();
                handleLoginResponse(response.body());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                responseCall.cancel();
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(VerifyAccountActivity.this);
            }
        });
    }

    /**
     * handle login response
     *
     * @param loginResponse @LoginResponse
     */
    private void handleLoginResponse(LoginResponse loginResponse) {
        if (loginResponse != null) {
            if (loginResponse.getResponseCode().equals(Api.SUCCESS)) {
                startActivity(new Intent(VerifyAccountActivity.this, HomeActivity.class));
                PreferenceConnector.writeString(VerifyAccountActivity.this, PreferenceConnector.USER_DATA, new Gson()
                        .toJson(loginResponse.getUser()));
                PreferenceConnector.writeBoolean(VerifyAccountActivity.this, PreferenceConnector.IS_LOGIN, true);
                finish();
            } else if (loginResponse.getResponseMessage().equals("Account not verified.")) {
                startActivity(new Intent(VerifyAccountActivity.this, HomeActivity.class));
                PreferenceConnector.writeString(VerifyAccountActivity.this, PreferenceConnector.USER_DATA, new Gson()
                        .toJson(loginResponse.getUser()));
                PreferenceConnector.writeBoolean(VerifyAccountActivity.this, PreferenceConnector.IS_LOGIN, true);
                finish();
                /*startActivity(new Intent(LoginActivity.this, VerifyAccountActivity.class));
                finish();*/
            } else {
                Utility.showToast(VerifyAccountActivity.this, loginResponse.getResponseMessage());
            }
        } else {
            Utility.showErrorMsg(VerifyAccountActivity.this);
        }
    }
}
