package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
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
            Utility.showToast(VerifyAccountActivity.this, "Enter OTP");
            try {
                Intent mailClient = new Intent(Intent.ACTION_VIEW);
                mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
                startActivity(mailClient);
            } catch (Exception e) {
                e.printStackTrace();
                Utility.showToast(VerifyAccountActivity.this, "No app found.");
            }
        } else {
            verify(code);
        }
    }

    private void verify(String code) {
        Api api = APIClient.getClient().create(Api.class);
        String userId = PreferenceConnector.readString(VerifyAccountActivity.this, PreferenceConnector.USER_ID, "");
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
}
