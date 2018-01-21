package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        binding.setActivity(this);
        setSpinner();
    }

    private void setSpinner() {
        String colors[] = {"Student", "Faculty", "Academy"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout
                .row_spinner, colors);
        //spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down
        // view
        binding.profileSpinner.setAdapter(spinnerArrayAdapter);
    }

    public void onSubmitClick() {
        startActivity(new Intent(RegistrationActivity.this, SelectCollegeActivity.class));
    }
}
