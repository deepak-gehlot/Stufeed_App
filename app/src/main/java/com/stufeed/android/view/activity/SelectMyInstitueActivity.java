package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAllCollegeResponse;
import com.stufeed.android.databinding.ActivitySelectMyInstitueBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.CollegeListAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectMyInstitueActivity extends AppCompatActivity {

    private ActivitySelectMyInstitueBinding mBinding;
    private GetAllCollegeResponse.College college;
    //private ArrayList<String> collegesStrList = new ArrayList<>();
    private ArrayList<GetAllCollegeResponse.College> colleges = new ArrayList<>();
    private CollegeListAdapter adapterCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_my_institue);
        mBinding.setActivity(this);

        setTextChangeListener();

        mBinding.searchCollegeEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                college = (GetAllCollegeResponse.College) parent.getItemAtPosition(position);
                mBinding.searchCollegeEdt.setText(college.getCollegeName());
            }
        });
    }

    public void onSkipClick() {
        if (college == null) {
            Utility.showToast(SelectMyInstitueActivity.this, "Select college.");
        } else {
            Intent intent = new Intent();
            intent.putExtra("college", college);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void setTextChangeListener() {
        mBinding.searchCollegeEdt.addTextChangedListener(new TextWatcher() {
            private final long DELAY = 800; // milliseconds
            private Timer timer = new Timer();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.progressBar.setVisibility(View.VISIBLE);
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
                                        searchCollege(mBinding.searchCollegeEdt.getText().toString().trim());
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
                mBinding.progressBar.setVisibility(View.GONE);
                handleSearchResult(response.body());
            }

            @Override
            public void onFailure(Call<GetAllCollegeResponse> call, Throwable t) {
                mBinding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void handleSearchResult(GetAllCollegeResponse getAllCollegeResponse) {
        if (getAllCollegeResponse == null) {
            Utility.showErrorMsg(SelectMyInstitueActivity.this);
        } else if (getAllCollegeResponse.getResponseCode().equals(Api.SUCCESS)) {
            colleges.clear();
            // collegesStrList.clear();
            colleges = getAllCollegeResponse.getCollegeArrayList();
            /*int size = colleges.size();
            collegesStrList.clear();
            for (int i = 0; i < size; i++) {
                collegesStrList.add(colleges.get(i).getCollegeName());
            }*/


            adapterCity = new CollegeListAdapter
                    (this, colleges);
            mBinding.searchCollegeEdt.setAdapter(adapterCity);
            mBinding.searchCollegeEdt.showDropDown();
            //binding.searchCollegeEdt.setShowAlways(true);
        }
    }

}
