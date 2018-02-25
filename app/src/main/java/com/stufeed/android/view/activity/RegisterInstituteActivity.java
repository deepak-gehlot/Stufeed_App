package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAllCollegeResponse;
import com.stufeed.android.databinding.RegisterInstituteBinding;
import com.stufeed.android.util.Utility;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class RegisterInstituteActivity extends AppCompatActivity {

    private RegisterInstituteBinding mBinding;

    private GetAllCollegeResponse.College college;
    private ArrayList<String> collegesStrList = new ArrayList<>();
    private ArrayList<GetAllCollegeResponse.College> colleges = new ArrayList<>();

    private int stepTag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.register_institute);
        mBinding.mainContent.setActivity(this);
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setTextChangeListener();

        mBinding.mainContent.searchCollegeEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                college = colleges.get(position);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (stepTag == 2) {
            manageViewState();
        } else {
            finish();
        }
    }

    public void onRegisterButtonClick() {
        if (stepTag == 2) {

        } else {
            manageViewState();
        }
    }

    public void onNextButtonClick() {
        manageViewState();

    }

    private void manageViewState() {
        if (stepTag == 1) {
            stepTag = 2;
            mBinding.mainContent.registerPart1.setVisibility(GONE);
            mBinding.mainContent.registerPart2.setVisibility(View.VISIBLE);
            mBinding.mainContent.part2RegisterBtn.setText(getString(R.string.register));
        } else {
            stepTag = 1;
            mBinding.mainContent.registerPart1.setVisibility(View.VISIBLE);
            mBinding.mainContent.registerPart2.setVisibility(View.GONE);
            mBinding.mainContent.part2RegisterBtn.setText("Next");
        }
    }

    private void setTextChangeListener() {
        mBinding.mainContent.searchCollegeEdt.addTextChangedListener(new TextWatcher() {
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
                                        searchCollege(mBinding.mainContent.searchCollegeEdt.getText().toString().trim
                                                ());
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
                handleSearchResult(response.body());
            }

            @Override
            public void onFailure(Call<GetAllCollegeResponse> call, Throwable t) {

            }
        });
    }

    private void handleSearchResult(GetAllCollegeResponse getAllCollegeResponse) {
        if (getAllCollegeResponse == null) {
            Utility.showErrorMsg(RegisterInstituteActivity.this);
        } else if (getAllCollegeResponse.getResponseCode().equals(Api.SUCCESS)) {
            colleges = getAllCollegeResponse.getCollegeArrayList();
            int size = colleges.size();
            collegesStrList.clear();
            for (int i = 0; i < size; i++) {
                collegesStrList.add(colleges.get(i).getCollegeName());
            }

            ArrayAdapter<String> adapterCity = new ArrayAdapter<String>
                    (this, android.R.layout.select_dialog_item, collegesStrList);
            mBinding.mainContent.searchCollegeEdt.setAdapter(adapterCity);
        }
    }

}
