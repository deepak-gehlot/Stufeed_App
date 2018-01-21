package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityVerifyAccountBinding;

public class VerifyAccountActivity extends AppCompatActivity {

    private ActivityVerifyAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_account);
        binding.setActivity(this);
    }

    public void onVerifyClick() {
        startActivity(new Intent(VerifyAccountActivity.this, HomeActivity.class));
    }
}
