package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.LoginResponse;
import com.stufeed.android.databinding.ActivityUserTypeBinding;
import com.stufeed.android.util.PreferenceConnector;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.viewmodel.RegistrationModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTypeActivity extends AppCompatActivity {
    ActivityUserTypeBinding binding;
    private int userType;

    RegistrationModel registrationModel = new RegistrationModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_type);
        binding.setActivity(this);
        binding.editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getDataFromBundle();
        binding.setModel(registrationModel);
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        registrationModel = bundle.getParcelable("Model");


    }


    public void onCardStu() {

        binding.cardStudent.setCardBackgroundColor(getResources().getColor(R.color.checkbox_color));
        binding.cardTeacher.setCardBackgroundColor(getResources().getColor(R.color.white));
        binding.cardDepartment.setCardBackgroundColor(getResources().getColor(R.color.white));
        userType = 1;
        binding.getModel().setUserType(userType);

    }

    public void onCardTearcher() {
        binding.cardStudent.setCardBackgroundColor(getResources().getColor(R.color.white));
        binding.cardTeacher.setCardBackgroundColor(getResources().getColor(R.color.checkbox_color));
        binding.cardDepartment.setCardBackgroundColor(getResources().getColor(R.color.white));
        userType = 2;
        binding.getModel().setUserType(userType);

    }

    public void onCardDepart() {
        binding.cardStudent.setCardBackgroundColor(getResources().getColor(R.color.white));
        binding.cardTeacher.setCardBackgroundColor(getResources().getColor(R.color.white));
        binding.cardDepartment.setCardBackgroundColor(getResources().getColor(R.color.checkbox_color));
        userType = 3;
        binding.getModel().setUserType(userType);
    }


    public void onSaveButtonClick(RegistrationModel model) {
        if (model.getUserType() != 0) {


            Api api = APIClient.getClient().create(Api.class);
            Call<LoginResponse> responseCall = api.register(model.getFullName(), model.getEmail
                    (), model.getPassword(), model.getContactNo(), model
                    .getUserType());
            ProgressDialog.getInstance().showProgressDialog(UserTypeActivity.this);
            responseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    ProgressDialog.getInstance().dismissDialog();
                    handleRegistrationResponse(response.body());
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    ProgressDialog.getInstance().dismissDialog();
                    Utility.showErrorMsg(UserTypeActivity.this);
                }
            });
        }
    }


    private void handleRegistrationResponse(LoginResponse loginResponse) {
        if (loginResponse != null) {
            if (loginResponse.getResponseCode().equals(Api.SUCCESS)) {
                PreferenceConnector.writeString(UserTypeActivity.this, PreferenceConnector.USER_DATA, new Gson()
                        .toJson(loginResponse.getUser()));
                PreferenceConnector.writeString(UserTypeActivity.this, PreferenceConnector.VERIFY, "0");
                startActivity(new Intent(UserTypeActivity.this, SelectCollegeActivity.class));
                finish();
            } else {
                Utility.showToast(UserTypeActivity.this, loginResponse.getResponseMessage());
            }
        } else {
            Utility.showErrorMsg(UserTypeActivity.this);
        }
    }


}


