package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.ActivityForgotPasswordBinding;
import com.stufeed.android.util.Extension;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.util.ValidationTemplate;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        mBinding.setActivity(this);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onSendButtonClick() {
        String email = mBinding.editTextEmail.getText().toString().trim();
        Extension extension = Extension.getInstance();
        if (!TextUtils.isEmpty(email)) {
            if (extension.executeStrategy(ForgotPasswordActivity.this, email, ValidationTemplate.EMAIL)) {
                ProgressDialog.getInstance().showProgressDialog(ForgotPasswordActivity.this);
                Api api = APIClient.getClient().create(Api.class);
                Call<Response> responseCall = api.forgotPassword(email);
                responseCall.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        ProgressDialog.getInstance().dismissDialog();
                        handleResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        ProgressDialog.getInstance().dismissDialog();
                        handleResponse(null);
                    }
                });
            } else {
                Utility.showToast(ForgotPasswordActivity.this, "Enter valid email.");
            }
        }
    }

    private void handleResponse(Response response) {
        if (response == null) {
            Utility.showErrorMsg(ForgotPasswordActivity.this);
        } else if (response.getResponseCode().equalsIgnoreCase(Api.SUCCESS)) {
            Utility.showToast(ForgotPasswordActivity.this, response.getResponseMessage());
            finish();
        } else {
            Utility.showToast(ForgotPasswordActivity.this, response.getResponseMessage());
        }
    }
}