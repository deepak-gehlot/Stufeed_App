package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAllCollegeResponse;
import com.stufeed.android.api.response.UpdateCollegeResponse;
import com.stufeed.android.databinding.ActivitySelectCollegeBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCollegeActivity extends AppCompatActivity {

    GetAllCollegeResponse.College college;
    private ActivitySelectCollegeBinding binding;
    private ArrayList<String> collegesStrList = new ArrayList<>();
    private ArrayList<GetAllCollegeResponse.College> colleges = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_college);
        binding.setActivity(this);
        setTextChangeListener();

        binding.searchCollegeEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                college = colleges.get(position);
            }
        });
    }

    public void onSkipClick() {
        if (college == null) {
            Utility.showToast(SelectCollegeActivity.this, "Select college.");
        } else {
            setCollege(college.getCollegeId());
        }
    }

    private void setTextChangeListener() {
        binding.searchCollegeEdt.addTextChangedListener(new TextWatcher() {
            private final long DELAY = 500; // milliseconds
            private Timer timer = new Timer();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.progressBar.setVisibility(View.VISIBLE);
                //   closeImg.setVisibility(View.GONE);
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        searchCollege(binding.searchCollegeEdt.getText().toString().trim());
                                    }
                                });
                            }
                        }, DELAY);

            }
        });
    }

    private void searchCollege(String searchTxt) {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetAllCollegeResponse> responseCall = api.getColleges(searchTxt);
        responseCall.enqueue(new Callback<GetAllCollegeResponse>() {
            @Override
            public void onResponse(Call<GetAllCollegeResponse> call, Response<GetAllCollegeResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                handleSearchResult(response.body());
            }

            @Override
            public void onFailure(Call<GetAllCollegeResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void handleSearchResult(GetAllCollegeResponse getAllCollegeResponse) {
        if (getAllCollegeResponse == null) {
            Utility.showErrorMsg(SelectCollegeActivity.this);
        } else if (getAllCollegeResponse.getResponseCode().equals(Api.SUCCESS)) {
            colleges = getAllCollegeResponse.getCollegeArrayList();
            int size = colleges.size();
            for (int i = 0; i < size; i++) {
                collegesStrList.add(colleges.get(i).getCollegeName());
            }

            ArrayAdapter<String> adapterCity = new ArrayAdapter<String>
                    (this, android.R.layout.select_dialog_item, collegesStrList);
            binding.searchCollegeEdt.setAdapter(adapterCity);
        }
    }

    private void setCollege(String collegeId) {
        Api api = APIClient.getClient().create(Api.class);
        Call<UpdateCollegeResponse> responseCall = api.setCollegeId(collegeId);
        ProgressDialog.getInstance().showProgressDialog(SelectCollegeActivity.this);
        responseCall.enqueue(new Callback<UpdateCollegeResponse>() {
            @Override
            public void onResponse(Call<UpdateCollegeResponse> call, Response<UpdateCollegeResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleSetCollegeResponse(response.body());
            }

            @Override
            public void onFailure(Call<UpdateCollegeResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(SelectCollegeActivity.this);
            }
        });
    }

    private void handleSetCollegeResponse(UpdateCollegeResponse response) {
        if (response == null) {
            Utility.showErrorMsg(SelectCollegeActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            startActivity(new Intent(SelectCollegeActivity.this, VerifyAccountActivity.class));
            finish();
        } else {
            Utility.showToast(SelectCollegeActivity.this, response.getResponseMessage());
        }
    }
}
