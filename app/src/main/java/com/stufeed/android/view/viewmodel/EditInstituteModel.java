package com.stufeed.android.view.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.stufeed.android.BR;

/**
 * Created by HP on 3/21/2018.
 */
public class EditInstituteModel extends BaseObservable {

    private String emailId = "";
    private String userId = "";
    @SerializedName("collageId")
    private String collegeId = "0";
    @SerializedName("college_name")
    private String collegeName = "";
    @SerializedName("Contactno")
    private String contactNo = "";
    @SerializedName("institutiontype")
    private String institutionType = "";
    @SerializedName("university_name")
    private String universityName = "";
    @SerializedName("address")
    private String address = "";
    @SerializedName("instituteid")
    private String instituteId = "";
    @SerializedName("city")
    private String city = "";
    @SerializedName("state")
    private String state = "";
    @SerializedName("website")
    private String website = "";
    @SerializedName("specialised_in")
    private String specialisedIn = "";
    @SerializedName("year_of_establishment")
    private String yearOfEstablishment = "";
    @SerializedName("managedby")
    private String managedBy = "";
    @SerializedName("location")
    private String location = "";
    private String affiliation_no = "";

    @Bindable
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
        notifyPropertyChanged(BR.emailId);
    }

    @Bindable
    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
        notifyPropertyChanged(BR.collegeId);
    }

    @Bindable
    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
        notifyPropertyChanged(BR.collegeName);
    }


    @Bindable
    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
        notifyPropertyChanged(BR.contactNo);
    }

    @Bindable
    public String getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
        notifyPropertyChanged(BR.institutionType);
    }

    @Bindable
    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
        notifyPropertyChanged(BR.universityName);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
        notifyPropertyChanged(BR.instituteId);
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        notifyPropertyChanged(BR.state);
    }

    @Bindable
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
        notifyPropertyChanged(BR.website);
    }

    @Bindable
    public String getSpecialisedIn() {
        return specialisedIn;
    }

    public void setSpecialisedIn(String specialisedIn) {
        this.specialisedIn = specialisedIn;
        notifyPropertyChanged(BR.specialisedIn);
    }

    @Bindable
    public String getYearOfEstablishment() {
        return yearOfEstablishment;
    }

    public void setYearOfEstablishment(String yearOfEstablishment) {
        this.yearOfEstablishment = yearOfEstablishment;
        notifyPropertyChanged(BR.yearOfEstablishment);
    }

    @Bindable
    public String getManagedBy() {
        return managedBy;
    }

    public void setManagedBy(String managedBy) {
        this.managedBy = managedBy;
        notifyPropertyChanged(BR.managedBy);
    }

    @Bindable
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        notifyPropertyChanged(BR.location);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAffiliation_no() {
        return affiliation_no;
    }

    public void setAffiliation_no(String affiliation_no) {
        this.affiliation_no = affiliation_no;
    }
}