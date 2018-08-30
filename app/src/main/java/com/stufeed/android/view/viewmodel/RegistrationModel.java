package com.stufeed.android.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RegistrationModel implements Parcelable {

    @SerializedName("firstname")
    private String fullName;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("contactno")
    private String contactNo;
    @SerializedName("usertype")
    private int userType=0;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.contactNo);
        dest.writeInt(this.userType);
    }

    public RegistrationModel() {
    }

    protected RegistrationModel(Parcel in) {
        this.fullName = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.contactNo = in.readString();
        this.userType = in.readInt();
    }

    public static final Parcelable.Creator<RegistrationModel> CREATOR = new Parcelable.Creator<RegistrationModel>() {
        @Override
        public RegistrationModel createFromParcel(Parcel source) {
            return new RegistrationModel(source);
        }

        @Override
        public RegistrationModel[] newArray(int size) {
            return new RegistrationModel[size];
        }
    };
}
