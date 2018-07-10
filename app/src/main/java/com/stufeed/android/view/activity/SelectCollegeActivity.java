package com.stufeed.android.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAllCollegeResponse;
import com.stufeed.android.api.response.UpdateCollegeResponse;
import com.stufeed.android.databinding.ActivitySelectCollegeBinding;
import com.stufeed.android.databinding.DialogInstituteCodeBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.CollegeListAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCollegeActivity extends AppCompatActivity {

    private ActivitySelectCollegeBinding binding;
    private GetAllCollegeResponse.College college;
    //private ArrayList<String> collegesStrList = new ArrayList<>();
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
                college = (GetAllCollegeResponse.College) parent.getItemAtPosition(position);
                binding.searchCollegeEdt.setText(college.getCollegeName());
            }
        });
    }

    public void onSkipClick() {
        if (college == null) {
            Utility.showToast(SelectCollegeActivity.this, "Select college.");
        } else {
            if (TextUtils.isEmpty(college.getInstituteCode())) {
                setCollege(college.getCollegeId());
            } else {
                showCodeDialog();
            }
        }
    }

    private void showCodeDialog() {
        final DialogInstituteCodeBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(SelectCollegeActivity.this),
                R.layout.dialog_institute_code, null, false);
        final Dialog dialog = new Dialog(SelectCollegeActivity.this);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(false);
        dialog.setTitle("Enter PIN Code");
        dialog.show();

        dialogBinding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeStr = dialogBinding.editText.getText().toString().trim();
                if (!TextUtils.isEmpty(codeStr)) {
                    if (college.getInstituteCode().equals(codeStr)) {
                        dialog.dismiss();
                        setCollege(college.getCollegeId());
                    } else {
                        Utility.showToast(SelectCollegeActivity.this, "Wrong code.");
                    }
                }
            }
        });
        dialogBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
            colleges.clear();
            // collegesStrList.clear();
            colleges = getAllCollegeResponse.getCollegeArrayList();
            /*int size = colleges.size();
            collegesStrList.clear();
            for (int i = 0; i < size; i++) {
                collegesStrList.add(colleges.get(i).getCollegeName());
            }*/

            CollegeListAdapter adapterCity = new CollegeListAdapter
                    (this, colleges);
            binding.searchCollegeEdt.setAdapter(adapterCity);
        }
    }

    private void setCollege(String collegeId) {
        Api api = APIClient.getClient().create(Api.class);
        String userId = Utility.getLoginUserId(SelectCollegeActivity.this);
        Call<UpdateCollegeResponse> responseCall = api.setCollegeId(userId, collegeId);
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
