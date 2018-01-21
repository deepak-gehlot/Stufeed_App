package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);

    }

    public void onLoginBtnClick() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }

    public void onRegisterBtnClick() {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }
}
