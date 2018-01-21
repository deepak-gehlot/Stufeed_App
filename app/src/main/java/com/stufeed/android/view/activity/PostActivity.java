package com.stufeed.android.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityPostBinding;

public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;
    private String settingStr[] = new String[]{"Allow Comment", "Allow Repost"};
    private boolean settingBool[] = {true, true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post);
        binding.setActivity(this);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Show setting dialog
     */
    public void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Settings");
        builder.setMultiChoiceItems(settingStr, settingBool, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                settingBool[which] = isChecked;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                for (int i = 0; i < settingBool.length; i++) {
                    boolean checked = settingBool[i];
                    if (checked) {

                    }
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
