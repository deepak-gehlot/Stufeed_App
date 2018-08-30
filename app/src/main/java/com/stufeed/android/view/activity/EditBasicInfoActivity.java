package com.stufeed.android.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.androidquery.AQuery;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetUserDetailsResponse;
import com.stufeed.android.api.response.UpdateProfileResponse;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.bean.Branch;
import com.stufeed.android.bean.Degree;
import com.stufeed.android.bean.Department;
import com.stufeed.android.bean.Designation;
import com.stufeed.android.databinding.ActivityEditBasicInfoBinding;
import com.stufeed.android.util.AssetsUtil;
import com.stufeed.android.util.PreferenceConnector;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.fragment.DatePickerFragment;

import java.io.File;
import java.util.ArrayList;
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

public class EditBasicInfoActivity extends AppCompatActivity {

    private ArrayList<String> branchList = new ArrayList<>();
    private ArrayList<String> degreeList = new ArrayList<>();
    private ArrayList<String> departmentList = new ArrayList<>();
    private ArrayList<String> designationList = new ArrayList<>();
    private ActivityEditBasicInfoBinding mBinding;
    private String loginUserId = "";
    private Uri outputUri;
    private AQuery aQuery;
    private String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_basic_info);
        mBinding.setActivity(this);
        loginUserId = Utility.getLoginUserId(this);
        aQuery = new AQuery(this);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getUserAllDetails();
        getDegreeList();
        getBranchList();
        getDepartmentsList();
        getDesignationList();
        setData();
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
                    File newFile = Utility.generateFile(EditBasicInfoActivity.this);
                    outputUri = Uri.fromFile(newFile);
                    Crop.of(Uri.fromFile(file), outputUri).asSquare().start(EditBasicInfoActivity.this);
                }
            });
        }
    }

    private void setData() {
        UserDetail userDetail = Utility.getLoginUserDetail(EditBasicInfoActivity.this);
        mBinding.userNameEdt.setText(userDetail.getFullName());
        mBinding.emailEdt.setText(userDetail.getEmail());
        mBinding.mobileEdt.setText(userDetail.getContactNo());
        userType = userDetail.getUserType();
    }

    public void onClickSaveButton(GetUserDetailsResponse.Details model) {
        Api api = APIClient.getClient().create(Api.class);
        Call<UpdateProfileResponse> responseCall = api.updateAllProfileData(loginUserId, model.getFullName(),
                model.getJoiningYear(), model.getAboutUs(), model.getGender(), model.getContactNo(), model
                        .getBirthDate(),
                model.getDegree(), model.getBranch(), model.getDesignation(), model.getDepartment());
        ProgressDialog.getInstance().showProgressDialog(EditBasicInfoActivity.this);
        responseCall.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                UpdateProfileResponse updateProfileResponse = response.body();
                if (updateProfileResponse != null) {
                    if (updateProfileResponse.getResponseCode().equals(Api.SUCCESS)) {
                        Utility.showToast(EditBasicInfoActivity.this, updateProfileResponse.getResponseMessage());
                    } else {
                        Utility.showToast(EditBasicInfoActivity.this, updateProfileResponse.getResponseMessage());
                    }
                } else {
                    Utility.showErrorMsg(EditBasicInfoActivity.this);
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(EditBasicInfoActivity.this);
            }
        });
    }

    public void selectGender() {
        final CharSequence[] gender = {"Male", "Female"};
        final android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder
                (EditBasicInfoActivity.this);
        //alert.setCancelable(false);
        alert.setTitle("Select Gender");
        alert.setSingleChoiceItems(gender, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (gender[which] == "Male") {
                    mBinding.getModel().setGender("Male");
                    dialog.dismiss();
                } else if (gender[which] == "Female") {
                    mBinding.getModel().setGender("Female");
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }

    public void openDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment(mBinding.birthdayTxt);
        DialogFragment newFragment = datePickerFragment;
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void selectGraduationYear() {
        final NumberPicker numberPicker;
        final Dialog graduation_dialog = new Dialog(EditBasicInfoActivity.this);
        graduation_dialog.setContentView(R.layout.graduation_year_layout);
        graduation_dialog.setTitle("Select Year");
        graduation_dialog.setCancelable(false);
        numberPicker = (NumberPicker) graduation_dialog.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1960);
        numberPicker.setMaxValue(2027);

        final Button done = (Button) graduation_dialog.findViewById(R.id.done);
        final Button cancel = (Button) graduation_dialog.findViewById(R.id.graduation_cancel);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.getModel().setJoiningYear(Integer.toString(numberPicker.getValue()));
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

    /**
     * Show change password dialog
     */
    public void showChangePasswordDialog() {
        final Dialog ialog = new Dialog(EditBasicInfoActivity.this);
        ialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ialog.setContentView(R.layout.change_password_layout);
        ialog.setCancelable(false);
        final EditText old_password = (EditText) ialog.findViewById(R.id.old_password);
        final EditText new_password = (EditText) ialog.findViewById(R.id.new_password);
        final EditText confirm_password = (EditText) ialog.findViewById(R.id.confirm_password);

        final Button dialogok = (Button) ialog.findViewById(R.id.ok);
        final Button dialogCancel = (Button) ialog.findViewById(R.id.dialog_cancel_btn);

        dialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpassword = old_password.getText().toString().trim();
                if (TextUtils.isEmpty(oldpassword)) {
                    Utility.showToast(EditBasicInfoActivity.this, "Enter old password.");
                    return;
                }
                if (new_password.getText().toString().trim().equals(confirm_password.getText().toString().trim())) {
                    if (!new_password.getText().toString().trim().equals("")) {
                        if (new_password.getText().toString().trim().length() < 6) {
                            new_password.setError("Atleast 6 characters");
                        } else {
                            ialog.dismiss();
                            changePassword(oldpassword, new_password.getText().toString().trim());
                        }
                    } else if (new_password.getText().toString().trim().equals("")) {
                        new_password.setError("Enter password");
                    }
                } else {
                    Utility.showToast(EditBasicInfoActivity.this, "Password Mismatch, Please enter same password");
                }
            }
        });
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ialog.dismiss();
            }
        });
        ialog.show();
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
                            EasyImage.openCamera(EditBasicInfoActivity.this, 1);
                        }
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(EditBasicInfoActivity.this, "Need permission to open camera.");
            }
        }).execute(EditBasicInfoActivity.this);
    }

    /**
     * Request gallery(READ_EXTERNAL_STORAGE) permission, if permission granted then open gallery
     */
    private void openGallery() {
        new Permissive.Request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        EasyImage.openGallery(EditBasicInfoActivity.this, 1);
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(EditBasicInfoActivity.this, "Need permission to open gallery.");
            }
        }).execute(EditBasicInfoActivity.this);
    }

    private void showHideAsPerRole(String type) {
        GetUserDetailsResponse.Details details = mBinding.getModel();
        switch (type) {
            case "1": // Student
                mBinding.autoComplete1.setHint("program");
                mBinding.autoComplete2.setHint("branch");
                if (details != null) {
                    mBinding.autoComplete1.setText(details.getDegree());
                    mBinding.autoComplete2.setText(details.getBranch());
                }
                //  mBinding.autoComplete1.setText(getFromLocalDataBase(sharedPref, CST_PROGRAM));
                //  mBinding.autoComplete2.setText(getFromLocalDataBase(sharedPref, CST_BRANCH));
                mBinding.autoComplete1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.program_edit_icon, 0, 0, 0);
                mBinding.autoComplete2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.branch_edit_icon, 0, 0, 0);

                ArrayAdapter<String> adapterDegree = new ArrayAdapter<String>
                        (this, android.R.layout.select_dialog_item, degreeList);
                mBinding.autoComplete1.setAdapter(adapterDegree);

                mBinding.autoComplete1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mBinding.getModel().setDegree(mBinding.autoComplete1.getText().toString().trim());
                    }
                });

                ArrayAdapter<String> adapterBranch = new ArrayAdapter<String>
                        (this, android.R.layout.select_dialog_item, branchList);
                mBinding.autoComplete2.setAdapter(adapterBranch);
                mBinding.autoComplete2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mBinding.getModel().setBranch(mBinding.autoComplete2.getText().toString().trim());
                    }
                });
                break;
            case "3":   // Department
                mBinding.autoComplete1.setHint("department");
                if (details != null) {
                    mBinding.autoComplete1.setText(details.getDepartment());
                }
                //     mBinding.autoComplete1.setText(getFromLocalDataBase(sharedPref, CST_DEPARTMENT));
                mBinding.autoComplete1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.branch_edit_icon, 0, 0, 0);

                mBinding.autoComplete2.setVisibility(View.GONE);

                ArrayAdapter<String> adapterDepartment = new ArrayAdapter<String>
                        (this, android.R.layout.select_dialog_item, departmentList);
                mBinding.autoComplete1.setAdapter(adapterDepartment);

                mBinding.autoComplete1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mBinding.getModel().setDepartment(mBinding.autoComplete1.getText().toString().trim());
                    }
                });
                break;
            case "2":   // Faculty
                mBinding.autoComplete1.setHint("designation");
                mBinding.autoComplete2.setHint("department");

                if (details != null) {
                    mBinding.autoComplete1.setText(details.getDesignation());
                    mBinding.autoComplete2.setText(details.getDepartment());
                }

                //    mBinding.autoComplete1.setText(getFromLocalDataBase(sharedPref, CST_DESIGNATION));
                //     mBinding.autoComplete2.setText(getFromLocalDataBase(sharedPref, CST_DEPARTMENT));
                mBinding.autoComplete1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.program_edit_icon, 0, 0, 0);
                mBinding.autoComplete2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.branch_edit_icon, 0, 0, 0);

                ArrayAdapter<String> adapterDepartment2 = new ArrayAdapter<String>
                        (this, android.R.layout.select_dialog_item, designationList);
                mBinding.autoComplete1.setAdapter(adapterDepartment2);

                ArrayAdapter<String> adapterDesignation = new ArrayAdapter<String>
                        (this, android.R.layout.select_dialog_item, departmentList);
                mBinding.autoComplete2.setAdapter(adapterDesignation);

                mBinding.autoComplete1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mBinding.getModel().setDesignation(mBinding.autoComplete1.getText().toString().trim());
                    }
                });

                mBinding.autoComplete2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mBinding.getModel().setDepartment(mBinding.autoComplete2.getText().toString().trim());
                    }
                });

                break;
            case "4": // institute

                break;
            default:
                break;
        }
    }

    private void getDegreeList() {
        String json = AssetsUtil.ReadFromfile("stufeed-dev-degrees-export.json", EditBasicInfoActivity.this);
        Degree[] degrees = new Gson().fromJson(json, Degree[].class);
        for (Degree degree : degrees) {
            degreeList.add(degree.getDegree());
        }
        Log.e("tes", "");
    }

    private void getDepartmentsList() {
        String json = AssetsUtil.ReadFromfile("stufeed-dev-departments-export.json", EditBasicInfoActivity.this);
        Department[] departments = new Gson().fromJson(json, Department[].class);
        for (Department department : departments) {
            departmentList.add(department.getDepartment());
        }
    }

    private void getDesignationList() {
        String json = AssetsUtil.ReadFromfile("stufeed-dev-designations-export.json", EditBasicInfoActivity.this);
        Designation[] designations = new Gson().fromJson(json, Designation[].class);

        for (Designation designation : designations) {
            designationList.add(designation.getDesignation());
        }
    }


    private void getBranchList() {
        String json = AssetsUtil.ReadFromfile("branch.json", EditBasicInfoActivity.this);
        Branch[] branches = new Gson().fromJson(json, Branch[].class);
        for (Branch branch : branches) {
            branchList.add(branch.getFIELD1());
        }
    }

    private void getUserAllDetails() {
        ProgressDialog.getInstance().showProgressDialog(EditBasicInfoActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetUserDetailsResponse> responseCall = api.getUserAllInfo(loginUserId);
        responseCall.enqueue(new Callback<GetUserDetailsResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailsResponse> call, Response<GetUserDetailsResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleUserResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetUserDetailsResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                handleUserResponse(null);
            }
        });
    }

    private void handleUserResponse(GetUserDetailsResponse response) {
        if (response == null) {
            Utility.showErrorMsg(EditBasicInfoActivity.this);
        } else {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                mBinding.setModel(response.getAllDetails());
                if (response.getAllDetails() != null) {
                    mBinding.collegeText.setText(response.getAllDetails().getCollegeName());
                }
            } else {
                Utility.showToast(EditBasicInfoActivity.this, response.getResponseMessage());
            }
        }
        showHideAsPerRole(userType);
    }

    /**
     * Change password
     *
     * @param password
     */
    private void changePassword(String oldPass, String password) {
        ProgressDialog.getInstance().showProgressDialog(EditBasicInfoActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        api.changePassword(loginUserId, password, oldPass).enqueue(new Callback<com.stufeed.android.api.response
                .Response>() {
            @Override
            public void onResponse(Call<com.stufeed.android.api.response.Response> call, Response<com.stufeed.android
                    .api.response.Response> response) {
                ProgressDialog.getInstance().dismissDialog();
                com.stufeed.android.api.response.Response response1 = response.body();
                if (response1 != null) {
                    if (response1.getResponseCode().equals(Api.SUCCESS)) {
                        Utility.showToast(EditBasicInfoActivity.this, response1.getResponseMessage());
                    } else {
                        Utility.showToast(EditBasicInfoActivity.this, response1.getResponseMessage());
                    }
                } else {
                    Utility.showErrorMsg(EditBasicInfoActivity.this);
                }
            }

            @Override
            public void onFailure(Call<com.stufeed.android.api.response.Response> call, Throwable t) {

            }
        });
    }

    private void updateProfile(File file) {
        ProgressDialog.getInstance().showProgressDialog(EditBasicInfoActivity.this);
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
                        UserDetail details = Utility.getLoginUserDetail(EditBasicInfoActivity
                                .this);
                        details.setProfilePic(updateProfileResponse.getProfilePic());
                        String json = new Gson().toJson(details);
                        PreferenceConnector.writeString(EditBasicInfoActivity.this, PreferenceConnector.USER_DATA,
                                json);
                        Utility.showToast(EditBasicInfoActivity.this, updateProfileResponse.getResponseMessage());
                    } else {
                        Utility.showToast(EditBasicInfoActivity.this, updateProfileResponse.getResponseMessage());
                    }
                } else {
                    Utility.showErrorMsg(EditBasicInfoActivity.this);
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(EditBasicInfoActivity.this);
            }
        });
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
