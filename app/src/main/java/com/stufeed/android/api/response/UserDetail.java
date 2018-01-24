package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Deepak Gehlot on 1/24/2018.
 */
public class UserDetail {

    @SerializedName("Userid")
    private String userId;
    @SerializedName("Fullname")
    private String fullName;
    @SerializedName("Email")
    private String email;
    @SerializedName("Usertype")
    private String userType;
    @SerializedName("Contactno")
    private String contactNo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
