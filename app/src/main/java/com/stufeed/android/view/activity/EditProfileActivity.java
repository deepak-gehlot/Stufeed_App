package com.stufeed.android.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityEditProfileBinding;
import com.stufeed.android.util.Utility;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        mBinding.setActivity(this);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onClickBasicInfo() {
        startActivity(new Intent(EditProfileActivity.this, EditBasicInfoActivity.class));
    }

    public void onClickAchievement() {
        startActivity(new Intent(EditProfileActivity.this, AchivementActivity.class));
    }

    public void onClickSkill() {
        startActivity(new Intent(EditProfileActivity.this, SkillsActivity.class));
    }

    public void onClickAbout() {
        final Dialog dialog = new Dialog(EditProfileActivity.this);
        dialog.setContentView(R.layout.dialog_about);
        dialog.setTitle("About");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        final EditText editTextAbout = dialog.findViewById(R.id.editTextAbout);
        Button buttonSave = dialog.findViewById(R.id.buttonSave);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aboutStr = editTextAbout.getText().toString().trim();
                if (!TextUtils.isEmpty(aboutStr)) {
                    dialog.dismiss();
                } else {
                    Utility.showToast(EditProfileActivity.this, "Enter about");
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
