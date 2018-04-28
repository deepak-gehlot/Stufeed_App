package com.stufeed.android.view.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityEdukitPostBinding;
import com.stufeed.android.view.adapter.EdukitPostAdapter;

public class EdukitPostActivity extends AppCompatActivity {

    private ActivityEdukitPostBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edukit_post);

        EdukitPostAdapter edukitPostAdapter = new EdukitPostAdapter(this);
        mBinding.recyclerView.setAdapter(edukitPostAdapter);
    }
}
