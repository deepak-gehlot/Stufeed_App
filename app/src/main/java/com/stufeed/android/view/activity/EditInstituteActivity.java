package com.stufeed.android.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import com.androidquery.AQuery;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.CollegeDetails;
import com.stufeed.android.api.response.UpdateProfileResponse;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.databinding.ActivityEditInstituteBinding;
import com.stufeed.android.util.PreferenceConnector;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.viewmodel.EditInstituteModel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditInstituteActivity extends AppCompatActivity {

    private ActivityEditInstituteBinding mBinding;
    private Uri outputUri;
    private AQuery aQuery;
    private String loginUserId = "", collageId = "";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_institute);
        mBinding.setActivity(this);
        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        init();

        UserDetail details = Utility.getLoginUserDetail(EditInstituteActivity
                .this);
        String profilePic = details.getProfilePic();
        if (!TextUtils.isEmpty(profilePic)) {
            aQuery.id(mBinding.studentImg).image(profilePic, true, true, 150, R.mipmap.ic_launcher);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            aQuery.id(mBinding.studentImg).image(new File(outputUri.getPath()), 150);
            updateProfile(new File(outputUri.getPath()));
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                    File file = imageFiles.get(0);
                    File newFile = Utility.generateFile(EditInstituteActivity.this);
                    outputUri = Uri.fromFile(newFile);
                    Crop.of(Uri.fromFile(file), outputUri).asSquare().start(EditInstituteActivity.this);
                }
            });
        }
    }

    private void init() {
        context = this;
        aQuery = new AQuery(this);
        loginUserId = Utility.getLoginUserId(this);
        collageId = Utility.getLoginUserDetail(this).getCollegeId();


        getCollegeDetails();
    }

    public void setInstituteTypeRadioGroup() {
        switch (mBinding.getModel().getInstitutionType()) {
            case "1":
                mBinding.collegeRadio.setChecked(true);
                break;
            case "2":
                mBinding.schoolRadio.setChecked(true);
                break;
            case "3":
                mBinding.universityRadio.setChecked(true);
                break;
            case "4":
                mBinding.collegeRadio.setChecked(true);
                break;
        }

        mBinding.rdx.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.college_radio:
                        mBinding.getModel().setInstitutionType("1");
                        mBinding.autocompleUniversity.setVisibility(View.VISIBLE);
                        break;
                    case R.id.school_radio:
                        mBinding.getModel().setInstitutionType("2");
                        mBinding.autocompleUniversity.setVisibility(View.VISIBLE);
                        break;
                    case R.id.university_radio:
                        mBinding.getModel().setInstitutionType("3");
                        mBinding.autocompleUniversity.setVisibility(View.GONE);
                        break;
                    case R.id.coaching_radio:
                        mBinding.getModel().setInstitutionType("4");
                        mBinding.autocompleUniversity.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    public void setManageByRadio() {

        switch (mBinding.getModel().getManagedBy()) {
            case "Private":
                mBinding.privateRadio.setChecked(true);
                break;
            case "Government":
                mBinding.govermentRadio.setChecked(true);
                break;
        }

        mBinding.managedRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.private_radio:
                        mBinding.getModel().setManagedBy("Private");
                        break;
                    case R.id.goverment_radio:
                        mBinding.getModel().setManagedBy("Government");
                        break;
                }
            }
        });
    }

    private void setLocationRadio() {


        switch (mBinding.getModel().getLocation()) {
            case "Urban":
                mBinding.urban.setChecked(true);
                break;
            case "Rural":
                mBinding.rural.setChecked(true);
                break;
        }

        mBinding.locationRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.urban:
                        mBinding.getModel().setLocation("Urban");
                        break;
                    case R.id.rural:
                        mBinding.getModel().setLocation("Rural");
                        break;
                }
            }
        });
    }

    public void selectEstablishYear() {
        final NumberPicker numberPicker;
        final Dialog graduation_dialog = new Dialog(EditInstituteActivity.this);
        graduation_dialog.setContentView(R.layout.graduation_year_layout);
        graduation_dialog.setTitle("Select Year");
        numberPicker = (NumberPicker) graduation_dialog.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1960);
        numberPicker.setMaxValue(2027);

        final Button done = (Button) graduation_dialog.findViewById(R.id.done);
        final Button cancel = (Button) graduation_dialog.findViewById(R.id.graduation_cancel);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.getModel().setYearOfEstablishment(Integer.toString(numberPicker.getValue()));
                graduation_dialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graduation_dialog.dismiss();
            }
        });
        graduation_dialog.show();
    }

    public void openImagePicker() {
        final String[] choice = {"Choose from Gallery", "Capture a photo"};
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(choice, 0, null)
                .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        if (selectedPosition == 0) {
                            openGallery();
                        } else {
                            openCamera();
                        }
                    }
                })
                .show();
    }

    /**
     * Request camera permission, if permission granted then open camera
     */
    private void openCamera() {
        new Permissive.Request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        if (permissions.length == 2) {
                            EasyImage.openCamera(EditInstituteActivity.this, 1);
                        }
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(EditInstituteActivity.this, "Need permission to open camera.");
            }
        }).execute(EditInstituteActivity.this);
    }

    /**
     * Request gallery(READ_EXTERNAL_STORAGE) permission, if permission granted then open gallery
     */
    private void openGallery() {
        new Permissive.Request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        EasyImage.openGallery(EditInstituteActivity.this, 1);
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(EditInstituteActivity.this, "Need permission to open gallery.");
            }
        }).execute(EditInstituteActivity.this);
    }

    private void getCollegeDetails() {
        ProgressDialog.getInstance().showProgressDialog(context);
        Api api = APIClient.getClient().create(Api.class);
        Call<CollegeDetails> call = api.getCollegeDetails(collageId);
        call.enqueue(new Callback<CollegeDetails>() {
            @Override
            public void onResponse(Call<CollegeDetails> call, Response<CollegeDetails> response) {
                handleCollegeDetailsResponse(response.body());
            }

            @Override
            public void onFailure(Call<CollegeDetails> call, Throwable t) {
                handleCollegeDetailsResponse(null);
            }
        });
    }


    private void handleCollegeDetailsResponse(CollegeDetails details) {
        ProgressDialog.getInstance().dismissDialog();
        if (details == null) {
            Utility.showErrorMsg(EditInstituteActivity.this);
        } else if (details.getResponseCode().equals(Api.SUCCESS)) {
            EditInstituteModel model = new EditInstituteModel();
            CollegeDetails.Data data = details.getData();
            model.setAddress(data.getAddress());
            model.setAffiliation_no(data.getAffiliationNo());
            model.setCity(data.getDistrict());
            model.setCollegeId(data.getCollegeId());
            model.setCollegeName(data.getCollegeName());
            model.setContactNo(Utility.getLoginUserDetail(context).getContactNo());
            model.setEmailId(Utility.getLoginUserDetail(context).getEmail());
            model.setInstituteId(data.getAffiliationNo());
            model.setInstitutionType(data.getInstitutionType());
            model.setLocation(data.getLocation());
            model.setManagedBy(data.getManagement());
            model.setSpecialisedIn(data.getSpecialisedIn());
            model.setState(data.getState());
            model.setUniversityName(data.getUniversityName());
            model.setUserId(loginUserId);
            model.setWebsite(data.getWebsite());
            model.setYearOfEstablishment(data.getYearOfEstablishment());
            mBinding.setModel(model);

            setInstituteTypeRadioGroup();
            setManageByRadio();
            setLocationRadio();
        } else {
            Utility.showToast(EditInstituteActivity.this, details.getResponseMessage());
        }
    }

    private void updateProfile(File file) {
        ProgressDialog.getInstance().showProgressDialog(EditInstituteActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        MultipartBody.Part part = prepareFilePart("profilepic", file);
        Call<UpdateProfileResponse> responseCall = api.uploadProfile(getPostBody(loginUserId), part);
        responseCall.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                UpdateProfileResponse updateProfileResponse = response.body();
                if (updateProfileResponse != null) {
                    if (updateProfileResponse.getResponseCode().equals(Api.SUCCESS)) {
                        UserDetail details = Utility.getLoginUserDetail(EditInstituteActivity
                                .this);
                        details.setProfilePic(updateProfileResponse.getProfilePic());
                        String json = new Gson().toJson(details);
                        PreferenceConnector.writeString(EditInstituteActivity.this, PreferenceConnector.USER_DATA,
                                json);
                        Utility.showToast(EditInstituteActivity.this, updateProfileResponse.getResponseMessage());
                    } else {
                        Utility.showToast(EditInstituteActivity.this, updateProfileResponse.getResponseMessage());
                    }
                } else {
                    Utility.showErrorMsg(EditInstituteActivity.this);
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(EditInstituteActivity.this);
            }
        });
    }

    public void onUpdateProfileClick() {
        ProgressDialog.getInstance().showProgressDialog(context);
        EditInstituteModel model = mBinding.getModel();
        Api api = APIClient.getClient().create(Api.class);
        Call<com.stufeed.android.api.response.Response> call = api.editInstituteProfile(loginUserId, collageId,
                model.getInstitutionType(), model.getUniversityName(),
                model.getAddress(), model.getAffiliation_no(), model.getCollegeName(),
                model.getCity(), model.getState(), model.getWebsite(), model.getSpecialisedIn(),
                model.getYearOfEstablishment(), model.getManagedBy(), model.getLocation(), model.getContactNo());
        call.enqueue(new Callback<com.stufeed.android.api.response.Response>() {
            @Override
            public void onResponse(Call<com.stufeed.android.api.response.Response> call, Response<com.stufeed.android.api.response.Response> response) {
                handleUpdateResponse(response.body());
            }

            @Override
            public void onFailure(Call<com.stufeed.android.api.response.Response> call, Throwable t) {
                handleUpdateResponse(null);
            }
        });
    }

    private void handleUpdateResponse(com.stufeed.android.api.response.Response response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response == null) {
            Utility.showErrorMsg(EditInstituteActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            UserDetail details = Utility.getLoginUserDetail(EditInstituteActivity
                    .this);
            details.setContactNo(mBinding.getModel().getContactNo());
            String json = new Gson().toJson(details);
            PreferenceConnector.writeString(EditInstituteActivity.this, PreferenceConnector.USER_DATA,
                    json);
            Utility.showToast(EditInstituteActivity.this, "Update Successfully.");
            finish();
        } else {
            Utility.showMsg(EditInstituteActivity.this, "Error", response.getResponseMessage());
        }
    }

    public Map<String, RequestBody> getPostBody(String userId) {
        MediaType mediaTypeText = MediaType.parse("text/plain");
        MediaType mediaTypeImage = MediaType.parse("image/*");

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("userid", RequestBody.create(mediaTypeText, userId));
        return requestBodyMap;
    }

    @NonNull
    public MultipartBody.Part prepareFilePart(String partName, File file) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils
        // /FileUtils.java
        // use the FileUtils to get the actual file by uri
        // create RequestBody instance from file
        MediaType mediaTypeImage = MediaType.parse("image/*");
        RequestBody requestFile =
                RequestBody.create(
                        mediaTypeImage,
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}