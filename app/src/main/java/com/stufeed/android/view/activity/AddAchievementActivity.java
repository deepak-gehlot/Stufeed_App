package com.stufeed.android.view.activity;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAchievementListResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.ActivityAchivementBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.viewmodel.AddAchievementModel;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;

public class AddAchievementActivity extends AppCompatActivity {

    private ActivityAchivementBinding binding;
    private String mLoginUserId = "";
    private String tagFrom = "0"; // 0 = add, 1 = update;
    private GetAchievementListResponse.Achievement achievement;
    private AddAchievementModel addAchievementModel;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_achivement);
        getDataFromBundle();
        binding.setActivity(this);
        binding.setModel(addAchievementModel);
        mLoginUserId = Utility.getLoginUserId(this);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void selectIssuedDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddAchievementActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "-" + month + "-" + year;
                        addAchievementModel.setIssueDate(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getDataFromBundle() {
        addAchievementModel = new AddAchievementModel();
        Bundle bundle = getIntent().getExtras();
        tagFrom = bundle.getString("from");
        if (tagFrom.equals("1")) {
            achievement = bundle.getParcelable("item");
            id = achievement.getAchievmentId();
            addAchievementModel.setTitle(achievement.getTitle());
            addAchievementModel.setAchievmentType(achievement.getAchievmentType());
            addAchievementModel.setDescription(achievement.getDescription());
            addAchievementModel.setIssueBy(achievement.getIssuedBy());
            addAchievementModel.setIssueDate(achievement.getIssuedDate());
        }
    }

    public void saveAchievement(AddAchievementModel model) {
        if (tagFrom.equals("1")) {
            update(model);
            return;
        }
        ProgressDialog.getInstance().showProgressDialog(AddAchievementActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<Response> responseCall = api.saveAchievement(mLoginUserId, model.getTitle(), model.getIssueBy(), model
                .getIssueDate(), model
                .getAchievmentType(), model.getDescription());

        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleAddResponse(response.body());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                handleAddResponse(null);
            }
        });
    }

    private void update(AddAchievementModel model) {
        ProgressDialog.getInstance().showProgressDialog(AddAchievementActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<Response> responseCall = api.updateAchievement(mLoginUserId, id, model.getTitle(), model.getIssueBy(),
                model.getIssueDate(), model.getAchievmentType(), model.getDescription());

        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleAddResponse(response.body());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                handleAddResponse(null);
            }
        });
    }

    private void handleAddResponse(Response response) {
        if (response == null) {
            Utility.showErrorMsg(AddAchievementActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            Utility.showToast(AddAchievementActivity.this, response.getResponseMessage());
            finish();
        } else {
            Utility.showToast(AddAchievementActivity.this, response.getResponseMessage());
        }
    }
}
