package com.stufeed.android.view.activity;

import android.content.Intent;
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
import android.widget.RadioGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAllCollegeResponse;
import com.stufeed.android.api.response.GetInstituteRegistrationResponse;
import com.stufeed.android.api.response.UpdateCollegeResponse;
import com.stufeed.android.databinding.RegisterInstituteBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.viewmodel.RegisterInstituteModel;

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
    private RegisterInstituteModel registerInstituteModel = new RegisterInstituteModel();
    private int stepTag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.register_institute);
        mBinding.mainContent.setActivity(this);
        mBinding.mainContent.setModel(registerInstituteModel);
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setTextChangeListener();
        setInstituteTypeRadioGroup();
        setLocationRadio();
        setManageByRadio();

        mBinding.mainContent.searchCollegeEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                college = colleges.get(position);
                registerInstituteModel.setAddress(college.getAddress());
                registerInstituteModel.setInstituteId(college.getAffiliationNo());
                registerInstituteModel.setCollegeId(college.getCollegeId());
                registerInstituteModel.setCollegeName(college.getCollegeName());
                registerInstituteModel.setInstitutionType(college.getCollegeType());
                registerInstituteModel.setCity(college.getDistrict());
                registerInstituteModel.setInstitutionType(college.getInstitutionType());
                registerInstituteModel.setLocation(college.getLocation());
                registerInstituteModel.setManagedBy(college.getManagement());
                registerInstituteModel.setSpecialisedIn(college.getSpecialisedIn());
                registerInstituteModel.setState(college.getState());
                registerInstituteModel.setUniversityName(college.getUniversityName());
                registerInstituteModel.setWebsite(college.getWebsite());
                registerInstituteModel.setYearOfEstablishment(college.getYearOfEstablishment());
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
            registerService(mBinding.mainContent.getModel());
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

    public void setInstituteTypeRadioGroup() {
        mBinding.mainContent.rdx.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.college_radio:
                        mBinding.mainContent.getModel().setInstitutionType("1");
                        mBinding.mainContent.autocompleUniversity.setVisibility(GONE);
                        break;
                    case R.id.school_radio:
                        mBinding.mainContent.getModel().setInstitutionType("2");
                        mBinding.mainContent.autocompleUniversity.setVisibility(GONE);
                        break;
                    case R.id.university_radio:
                        mBinding.mainContent.getModel().setInstitutionType("3");
                        mBinding.mainContent.autocompleUniversity.setVisibility(View.VISIBLE);
                        break;
                    case R.id.coaching_radio:
                        mBinding.mainContent.getModel().setInstitutionType("4");
                        mBinding.mainContent.autocompleUniversity.setVisibility(GONE);
                        break;
                }
            }
        });
    }

    public void setManageByRadio() {
        mBinding.mainContent.managedRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.private_radio:
                        mBinding.mainContent.getModel().setManagedBy("Private");
                        break;
                    case R.id.goverment_radio:
                        mBinding.mainContent.getModel().setManagedBy("Government");
                        break;
                }
            }
        });
    }

    private void setLocationRadio() {
        mBinding.mainContent.locationRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.urban:
                        mBinding.mainContent.getModel().setLocation("Urban");
                        break;
                    case R.id.rural:
                        mBinding.mainContent.getModel().setLocation("Rural");
                        break;
                }
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

    private void registerService(RegisterInstituteModel registerInstituteModel) {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetInstituteRegistrationResponse> responseCall = api.registerInstitute(registerInstituteModel
                        .getCollegeId(), registerInstituteModel.getCollegeName(),
                registerInstituteModel.getEmail(), registerInstituteModel.getPassword(),
                registerInstituteModel.getUserType(), registerInstituteModel.getContactNo(),
                registerInstituteModel.getInstitutionType(), registerInstituteModel.getUniversityName(),
                registerInstituteModel.getAddress(), registerInstituteModel.getInstituteId(), registerInstituteModel
                        .getCity(), registerInstituteModel.getState(), registerInstituteModel.getWebsite(),
                registerInstituteModel.getSpecialisedIn(), registerInstituteModel.getYearOfEstablishment(),
                registerInstituteModel.getManagedBy(), registerInstituteModel.getLocation());
        ProgressDialog.getInstance().showProgressDialog(RegisterInstituteActivity.this);

        responseCall.enqueue(new Callback<GetInstituteRegistrationResponse>() {
            @Override
            public void onResponse(Call<GetInstituteRegistrationResponse> call,
                                   Response<GetInstituteRegistrationResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleRegisterResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetInstituteRegistrationResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                handleRegisterResponse(null);
            }
        });
    }

    private void handleRegisterResponse(GetInstituteRegistrationResponse response) {
        if (response == null) {
            Utility.showErrorMsg(RegisterInstituteActivity.this);
        } else if (response.getResponseCode().equalsIgnoreCase(Api.SUCCESS)) {
            Utility.showToast(RegisterInstituteActivity.this, response.getResponseMessage());
            setCollege(response.getUserid(), registerInstituteModel
                    .getCollegeId());
        } else {
            Utility.showToast(RegisterInstituteActivity.this, response.getResponseMessage());
        }
    }

    private void setCollege(String userId, String collegeId) {
        Api api = APIClient.getClient().create(Api.class);
        Call<UpdateCollegeResponse> responseCall = api.setCollegeId(userId, collegeId);
        ProgressDialog.getInstance().showProgressDialog(RegisterInstituteActivity.this);
        responseCall.enqueue(new Callback<UpdateCollegeResponse>() {
            @Override
            public void onResponse(Call<UpdateCollegeResponse> call, Response<UpdateCollegeResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleSetCollegeResponse(response.body());
            }

            @Override
            public void onFailure(Call<UpdateCollegeResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(RegisterInstituteActivity.this);
            }
        });
    }

    private void handleSetCollegeResponse(UpdateCollegeResponse response) {
        if (response == null) {
            Utility.showErrorMsg(RegisterInstituteActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            startActivity(new Intent(RegisterInstituteActivity.this, VerifyAccountActivity.class));
            finish();
        } else {
            Utility.showToast(RegisterInstituteActivity.this, response.getResponseMessage());
        }
    }
}