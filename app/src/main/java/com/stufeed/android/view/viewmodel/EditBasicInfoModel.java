package com.stufeed.android.view.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.stufeed.android.BR;

/**
 * Created by HP on 3/11/2018.
 */
public class EditBasicInfoModel extends BaseObservable {

    @SerializedName("userid")
    private String userId = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("joiningyear")
    private String joiningYear = "";
    @SerializedName("aboutus")
    private String about = "";
    @SerializedName("gender")
    private String gender = "";
    @SerializedName("contactno")
    private String contact = "";
    @SerializedName("birthdate")
    private String birthDate = "";
    @SerializedName("degree")
    private String degree = "";
    @SerializedName("branch")
    private String branch = "";
    @SerializedName("designation")
    private String designation = "";
    @SerializedName("department")
    private String department = "";

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        notifyPropertyChanged(BR.userId);
    }

    @Bindable
    public String getJoiningYear() {
        return joiningYear;
    }

    public void setJoiningYear(String joiningYear) {
        this.joiningYear = joiningYear;
        notifyPropertyChanged(BR.joiningYear);
    }

    @Bindable
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
        notifyPropertyChanged(BR.about);
    }

    @Bindable
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }

    @Bindable
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
        notifyPropertyChanged(BR.contact);
    }

    @Bindable
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        notifyPropertyChanged(BR.birthDate);
    }

    @Bindable
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
        notifyPropertyChanged(BR.degree);
    }

    @Bindable
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
        notifyPropertyChanged(BR.branch);
    }

    @Bindable
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
        notifyPropertyChanged(BR.designation);
    }

    @Bindable
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
        notifyPropertyChanged(BR.department);
    }
}