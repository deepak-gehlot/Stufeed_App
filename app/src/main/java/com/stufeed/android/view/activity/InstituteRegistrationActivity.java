/*package com.stufeed.android.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.pojo.Institute;

import java.util.ArrayList;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

public class InstituteRegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailEdt, passwordEdt, mobileEdt, websiteEdt, stablishYearEdt;
    private EditText institute_name, institute_address, institute_id_no;
    private AutoCompleteTextView universityAutoCompleteTextView, cityAutoCompleteTextView, stateAutoCompleteTextView, specalizedAutoCompleteTextView;
    private RadioGroup rdx,managanedRG, locationRG;
    private Toolbar toolbar;
    private ArrayList<String> commonArrayList;
    private ArrayAdapter<String> commonArrayAdapter;
    private ArrayList<String> citylist;
    private String radioStr, instNameStr, instAddrStr, instIdNoStr, universityStr,
            cityStr, stateStr,email, pass, mobile, website,totalCount,
            specalist, stablish,institute_type_radio_string,institute_managed_radio_string, institute_location_radio_string;
    private CardView register_part1, register_part2;
    private MenuItem nextBtn;
    private Button register;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int i =0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_institute);
        mFireBaseAuth = FirebaseAuth.getInstance();
        setViewWidget();
        progressDialog = loadProgressDialog(InstituteRegistration.this, "Loading data", BOTTOM_DISPLAY);
        progressDialog.show();
        mFireBaseAuth.signInWithEmailAndPassword(REGISTER_EMAIL, CST_PROGARD)
                .addOnCompleteListener(InstituteRegistration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mFireBaseUser = mFireBaseAuth.getCurrentUser();
                            setList(commonArrayList = loadLists(CST_UNIVERSITY, CST_UNIVERSITY_CHILD,progressDialog),commonArrayAdapter, universityAutoCompleteTextView,InstituteRegistration.this);
                            setList(commonArrayList = loadLists(CST_DISTRICTS, CST_DISTRICT,progressDialog),commonArrayAdapter, cityAutoCompleteTextView,InstituteRegistration.this);
                            setList(commonArrayList = loadLists(CST_STATES, CST_STATE,progressDialog),commonArrayAdapter, stateAutoCompleteTextView,InstituteRegistration.this);
                            setList(commonArrayList = loadLists(CST_SPECALIZED, CST_SPECALIZED_IN,progressDialog),commonArrayAdapter, specalizedAutoCompleteTextView,InstituteRegistration.this);
                            DATABASE_REFERENCE = setFireBaseKey(CST_INSTITUTES);
                            DATABASE_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getChildrenCount()!=0){
                                        totalCount = String.valueOf(dataSnapshot.getChildrenCount());
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
        institute_type_radio_string = "college_radio";
    }

    private void setViewWidget() {
        *//*TODO: ToolBar *//*
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.register_institute);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        institute_name = (EditText) findViewById(R.id.institute_name);
        register = (Button) findViewById(R.id.part2_register_btn);
        register.setOnClickListener(this);
        institute_address = (EditText) findViewById(R.id.institute_address);
        institute_id_no = (EditText) findViewById(R.id.institute_id_no);
        register_part1 = (CardView) findViewById(R.id.register_part1);
        register_part2 = (CardView) findViewById(R.id.register_part2);

        cityAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_city);
        stateAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_state);

        rdx = (RadioGroup) findViewById(R.id.rdx);
        rdx.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.university_radio:
                        setHintLayout();
                        hideKeyBoard(rdx, InstituteRegistration.this);
                        universityAutoCompleteTextView.setVisibility(View.GONE);
                        institute_type_radio_string = "university_radio";
                        break;
                    case R.id.school_radio:
                        setHintLayout();
                        hideKeyBoard(rdx, InstituteRegistration.this);
                        universityAutoCompleteTextView.setVisibility(View.GONE);
                        institute_type_radio_string = "school_radio";
                        break;
                    case R.id.college_radio:
                        universityAutoCompleteTextView.requestFocus();
                        universityAutoCompleteTextView.setVisibility(View.VISIBLE);
                        universityAutoCompleteTextView.setEnabled(true);
                        universityAutoCompleteTextView.setText("");
                        universityAutoCompleteTextView.setHint(SELECT_UNIVERSITY);
                        institute_type_radio_string = "college_radio";
                        break;
                    case R.id.coaching_radio:
                        setHintLayout();
                        hideKeyBoard(rdx, InstituteRegistration.this);
                        universityAutoCompleteTextView.setVisibility(View.GONE);
                        institute_type_radio_string = "coaching_radio";
                        break;
                }
            }
        });
        universityAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomple_university);
        universityAutoCompleteTextView.setOnClickListener(this);
        register_part1.setVisibility(View.VISIBLE);

        *//**TODO: Part 2*//*
        emailEdt = (EditText) findViewById(R.id.email_address);
        passwordEdt = (EditText) findViewById(R.id.password);
        mobileEdt = (EditText) findViewById(R.id.mobile_number);
        websiteEdt = (EditText) findViewById(R.id.website);
        specalizedAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.specialist);
        stablishYearEdt = (EditText) findViewById(R.id.year_os_established);
        stablishYearEdt.setOnClickListener(this);
        register = (Button) findViewById(R.id.part2_register_btn);
        register.setOnClickListener(this);

        managanedRG = (RadioGroup) findViewById(R.id.managed_rg);
        managanedRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.private_radio:
                        institute_managed_radio_string = "private_radio";
                        break;

                    case R.id.goverment_radio:
                        institute_managed_radio_string = "goverment_radio";
                        break;
                }
            }
        });
        locationRG = (RadioGroup) findViewById(R.id.location_rg);
        locationRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.urban:
                        institute_location_radio_string = "urban_radio";
                        break;

                    case R.id.rural:
                        institute_location_radio_string = "rural_radio";
                        break;
                }
            }
        });

        sharedPreferences = getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    private void setHintLayout() {
        universityAutoCompleteTextView.setEnabled(false);
        universityAutoCompleteTextView.setHint("Select University");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_next, menu);
        nextBtn =(MenuItem) menu.findItem(R.id.next_button);
        nextBtn.setVisible(true);
        setNextBtn(nextBtn);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.next_button:
                setNextBtn(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNextBtn(MenuItem next) {
        instNameStr = institute_name.getText().toString();
        universityStr = universityAutoCompleteTextView.getText().toString();
        instAddrStr = institute_address.getText().toString();
        cityStr = cityAutoCompleteTextView.getText().toString();
        stateStr = stateAutoCompleteTextView.getText().toString();
        instIdNoStr = institute_id_no.getText().toString();
        boolean q1 = checkEditText(institute_name,getResources().getString(R.string.error_institute_name));
        boolean q2 = checkEditText(universityAutoCompleteTextView,getResources().getString(R.string.error_university));
        boolean q3 = checkEditText(institute_address, getResources().getString(R.string.error_institue_address));
        boolean q4 = checkEditText(cityAutoCompleteTextView,getResources().getString(R.string.error_city));
        boolean q5 = checkEditText(stateAutoCompleteTextView,getResources().getString(R.string.error_state));
        boolean q6 = checkEditText(institute_id_no,getResources().getString(R.string.error_institute_id_no));
        if(institute_type_radio_string.equals("college_radio")){
            if(q1 && q2 && q3 && q4 && q5 && q6){
                if(rdx.getCheckedRadioButtonId() != -1) {
                    register_part1.setVisibility(View.GONE);
                    register_part2.setVisibility(View.VISIBLE);
                    i = i + 1;
                    hideKeyBoard(institute_address, InstituteRegistration.this);
                    mFireBaseAuth.signOut();
                    if (register_part1.getVisibility() == View.VISIBLE) {
                        next.setVisible(true);
                    } else {
                        next.setVisible(false);
                    }
                }else{
                    showToast(this, "Please Select Institute Type");
                }
            }
        }else{
            if(q1 && q3 && q4 && q5 && q6){
                if(rdx.getCheckedRadioButtonId() != -1) {
                    register_part1.setVisibility(View.GONE);
                    register_part2.setVisibility(View.VISIBLE);
                    i = i + 1;
                    hideKeyBoard(institute_address, InstituteRegistration.this);
                    mFireBaseAuth.signOut();
                    if (register_part1.getVisibility() == View.VISIBLE) {
                        next.setVisible(true);
                    } else {
                        next.setVisible(false);
                    }
                }else{
                    showToast(this, "Please Select Institute Type");
                }
            }
        }

    }


    @Override
    public void onBackPressed() {
        i = i-2;
        switch (i) {
            case -1:
                register_part2.setVisibility(View.GONE);
                register_part1.setVisibility(View.VISIBLE);
                if(register_part1.getVisibility() == View.GONE){
                    nextBtn.setVisible(false);
                }else{
                    nextBtn.setVisible(true);
                }
                break;
            default:
                showToast(InstituteRegistration.this, "default");
                mFireBaseAuth.signOut();
                goToNext(InstituteRegistration.this, SPLASH_SCREEN, true);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.part2_register_btn:
                email = emailEdt.getText().toString();
                pass = passwordEdt.getText().toString();
                mobile = mobileEdt.getText().toString();
                website = websiteEdt.getText().toString();
                specalist = specalizedAutoCompleteTextView.getText().toString();
                stablish = stablishYearEdt.getText().toString();

                boolean q11 = checkEditText(emailEdt,getResources().getString(R.string.error_email));
                boolean q12 = checkEditText(passwordEdt,getResources().getString(R.string.error_pass));
                boolean q13 = checkEditText(mobileEdt,getResources().getString(R.string.error_mobile));

                if(q11 && q12 && q13){
                    progressDialog = loadProgressDialog(InstituteRegistration.this, LOADING,CENTER_DISPLAY);
                    progressDialog.show();
                    if(managanedRG.getCheckedRadioButtonId() !=-1 && locationRG.getCheckedRadioButtonId()!=-1) {

                        Log.i(LOGCAT,instNameStr + "\n" + universityStr + "\n" +
                                instAddrStr + "\n" + cityStr + "\n" + stateStr + "\n" + instIdNoStr + "\n" + email + "\n" + pass + "\n" +
                                mobile + "\n" + website + "\n" + specalist + "\n" + stablish);
                        final Institute institute = new Institute(" address",
                                "affiliation_no",
                                "college_name",
                                "college_type",
                                "district",
                                "location",
                                "management",
                                "specialised_in",
                                "state ",
                                "university_name",
                                "university_type ",
                                "upload_year",
                                "website ",
                                "year_of_establishment");

                        mFireBaseAuth = FirebaseAuth.getInstance();
                        mFireBaseAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(InstituteRegistration.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            showSnackBar(register,"Email already in use..."+task.toString(),"");
                                            progressDialog.dismiss();
                                        } else {
                                            DATABASE_REFERENCE.child(totalCount).setValue(institute);
                                            progressDialog.dismiss();
                                            boolean b = setToLocalDataBase(editor, sharedPreferences, COMMON_FLAG, CST_INSTITUTE);
                                            if(b){
                                                goToNext(InstituteRegistration.this, EDIT_PROFILE, true);
                                            }
                                        }
                                    }
                                });

                    }
                    else{
                        //showToast(InstituteRegistration.this, "Select Both Managed And Location");
                    }
                }


                break;
          *//*  case R.id.rdx:
                switch (i){
                    case R.id.university_radio:
                        showToast(InstituteRegistration.this, "sss");
                        setHintLayout();
                        universityAutoCompleteTextView.setVisibility(View.GONE);
                        institute_type_radio_string = "university_radio";
                        break;
                    case R.id.school_radio:
                        setHintLayout();
                        universityAutoCompleteTextView.setVisibility(View.GONE);
                        institute_type_radio_string = "school_radio";
                        break;
                    case R.id.college_radio:
                        universityAutoCompleteTextView.setVisibility(View.VISIBLE);
                        universityAutoCompleteTextView.setEnabled(true);
                        universityAutoCompleteTextView.setText("");
                        universityAutoCompleteTextView.setHint(SELECT_UNIVERSITY);
                        institute_type_radio_string = "college_radio";
                        break;
                    case R.id.coaching_radio:
                        setHintLayout();
                        universityAutoCompleteTextView.setVisibility(View.GONE);
                        institute_type_radio_string = "coaching_radio";
                        break;
                }
                break;
*//*

           *//* case R.id.managed_rg:
                switch (i){
                    case R.id.private_radio:
                        institute_managed_radio_string = "private_radio";
                        break;

                    case R.id.goverment_radio:
                        institute_managed_radio_string = "goverment_radio";
                        break;
                }
                break;*//*
            *//*case R.id.location_rg:
                switch (i){
                    case R.id.urban:
                        institute_location_radio_string = "urban_radio";
                        break;

                    case R.id.rural:
                        institute_location_radio_string = "rural_radio";
                        break;
                }
                break;*//*
            case R.id.year_os_established:
                final NumberPicker numberPicker;
                final Dialog graduation_dialog = new Dialog(InstituteRegistration.this);
                graduation_dialog.setContentView(R.layout.graduation_year_layout);
                graduation_dialog.setTitle("Select Year");
                //graduation_dialog.setCancelable(false);
                numberPicker = (NumberPicker) graduation_dialog.findViewById(R.id.numberPicker);
                numberPicker.setMinValue(1960);
                numberPicker.setMaxValue(2027);

                final Button done = (Button) graduation_dialog.findViewById(R.id.done);
                *//*final Button cancel = (Button) graduation_dialog.findViewById(R.id.dialog_cancel_btn);
                cancel.setVisibility(View.GONE);*//*

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stablishYearEdt.setText(Integer.toString(numberPicker.getValue()));
                        graduation_dialog.dismiss();

                    }
                });
                graduation_dialog.show();
                break;


            default:
                break;
        }
    }
}*/
