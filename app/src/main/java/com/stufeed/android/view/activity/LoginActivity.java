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
import com.stufeed.android.databinding.ActivityLoginBinding;
import com.stufeed.android.util.Extension;
import com.stufeed.android.util.PreferenceConnector;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.util.ValidationTemplate;
import com.stufeed.android.view.viewmodel.LoginModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);
        binding.setModel(new LoginModel());

    }

    /**
     * Validate login button
     *
     * @param loginModel @LoginModel
     * @return true if valid else false
     */
    private boolean validate(LoginModel loginModel) {
        Extension extension = Extension.getInstance();
        if (TextUtils.isEmpty(loginModel.getEmail()) || TextUtils.isEmpty(loginModel.getPassword())) {
            Utility.showToast(LoginActivity.this, "Enter your email and password.");
            return false;
        } else if (!extension.executeStrategy(LoginActivity.this, loginModel.getEmail(), ValidationTemplate.EMAIL)) {
            Utility.showToast(LoginActivity.this, "Invalid email.");
            return false;
        } else if (!extension.executeStrategy(LoginActivity.this, "", ValidationTemplate.INTERNET)) {
            Utility.setNoInternetPopup(LoginActivity.this);
            return false;
        } else {
            return true;
        }
    }

    /**
     * On Login button click method
     *
     * @param loginModel @LoginModel
     */
    public void onLoginBtnClick(LoginModel loginModel) {
        if (validate(loginModel)) {
            Api api = APIClient.getClient().create(Api.class);
            final Call<LoginResponse> responseCall = api.login(loginModel.getEmail(), loginModel.getPassword(),
                    loginModel
                            .getDeviceToken());
            ProgressDialog.getInstance().showProgressDialog(LoginActivity.this);
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
                    Utility.showErrorMsg(LoginActivity.this);
                }
            });
        }
    }

    /**
     * handle login response
     *
     * @param loginResponse @LoginResponse
     */
    private void handleLoginResponse(LoginResponse loginResponse) {
        if (loginResponse != null) {
            if (loginResponse.getResponseCode().equals(Api.SUCCESS)) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.USER_DATA, new Gson()
                        .toJson(loginResponse.getUser()));
            } else {

            }
        } else {
            Utility.showErrorMsg(LoginActivity.this);
        }
    }

    /**
     * On Register button click method
     */
    public void onRegisterBtnClick() {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }
}
