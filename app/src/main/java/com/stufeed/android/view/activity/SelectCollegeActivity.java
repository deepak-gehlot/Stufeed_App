package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivitySelectCollegeBinding;

public class SelectCollegeActivity extends AppCompatActivity {

    private ActivitySelectCollegeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_college);
        binding.setActivity(this);
    }

    public void onSkipClick() {
        startActivity(new Intent(SelectCollegeActivity.this, VerifyAccountActivity.class));
    }
}
