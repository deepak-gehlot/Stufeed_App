package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.LoginResponse;
import com.stufeed.android.databinding.ActivityRegistrationBinding;
import com.stufeed.android.util.Extension;
import com.stufeed.android.util.PreferenceConnector;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.util.ValidationTemplate;
import com.stufeed.android.view.viewmodel.RegistrationModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        binding.setModel(new RegistrationModel());
        binding.setActivity(this);
        binding.setModel(new RegistrationModel());
        setSpinner();
    }

    private void setSpinner() {
        String type[] = {"Student", "Faculty", "Department"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout
                .row_spinner, type);
        binding.profileSpinner.setAdapter(spinnerArrayAdapter);
        binding.profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.getModel().setUserType(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Validate registration form
     *
     * @param registrationModel
     * @return
     */
    private boolean validate(RegistrationModel registrationModel) {
        Extension extension = Extension.getInstance();
        if (TextUtils.isEmpty(registrationModel.getFullName()) || TextUtils.isEmpty(registrationModel.getEmail())
                || TextUtils.isEmpty(registrationModel.getContactNo()) || TextUtils.isEmpty(registrationModel
                .getPassword())) {
            Utility.showToast(RegistrationActivity.this, getString(R.string.all_required));
            return false;
        } else if (!extension.executeStrategy(RegistrationActivity.this, registrationModel.getEmail(),
                ValidationTemplate.EMAIL)) {
            Utility.showToast(RegistrationActivity.this, getString(R.string.invalid_email));
            return false;
        } else if (!extension.executeStrategy(RegistrationActivity.this, registrationModel.getContactNo(),
                ValidationTemplate.isnumber)) {
            Utility.showToast(RegistrationActivity.this, getString(R.string.invalid_number));
            return false;
        } else {
            return true;
        }
    }

    /**
     * On Submit button click
     *
     * @param registrationModel @RegistrationModel
     */
    public void onSubmitClick(RegistrationModel registrationModel) {
        if (validate(registrationModel)) {
            Api api = APIClient.getClient().create(Api.class);
            Call<LoginResponse> responseCall = api.register(registrationModel.getFullName(), registrationModel.getEmail
                    (), registrationModel.getPassword(), registrationModel.getContactNo(), registrationModel
                    .getUserType());
            ProgressDialog.getInstance().showProgressDialog(RegistrationActivity.this);
            responseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    ProgressDialog.getInstance().dismissDialog();
                    handleRegistrationResponse(response.body());
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    ProgressDialog.getInstance().dismissDialog();
                    Utility.showErrorMsg(RegistrationActivity.this);
                }
            });
        }
    }

    private void handleRegistrationResponse(LoginResponse loginResponse) {
        if (loginResponse != null) {
            if (loginResponse.getResponseCode().equals(Api.SUCCESS)) {
                PreferenceConnector.writeString(RegistrationActivity.this, PreferenceConnector.USER_DATA, new Gson()
                        .toJson(loginResponse.getUser()));
                startActivity(new Intent(RegistrationActivity.this, SelectCollegeActivity.class));
                finish();
            } else {
                Utility.showToast(RegistrationActivity.this, loginResponse.getResponseMessage());
            }
        } else {
            Utility.showErrorMsg(RegistrationActivity.this);
        }
    }
}
