package com.stufeed.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Deepak Gehlot on 2/8/2018.
 */

public class GetCollegeUserResponse {

    private String responseCode = "";
    private String responseMessage = "";
    @SerializedName("allusers")
    private ArrayList<User> userArrayList;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }

    public void setUserArrayList(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    public static class User implements Parcelable {
        @SerializedName("userid")
        private String userId = "";
        @SerializedName("collegeid")
        private String collegeId = "";
        @SerializedName("collegename")
        private String collegeName;
        @SerializedName("fullname")
        private String fullName = "";
        @SerializedName("profilepic")
        private String profilePic = "";
        @SerializedName("is_follow")
        private String isFollow = "";
        private String verifyStatus = "";

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCollegeId() {
            return collegeId;
        }

        public void setCollegeId(String collegeId) {
            this.collegeId = collegeId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getIsFollow() {
            return isFollow;
        }

        public void setIsFollow(String isFollow) {
            this.isFollow = isFollow;
        }

        public String getCollegeName() {
            return collegeName;
        }

        public void setCollegeName(String collegeName) {
            this.collegeName = collegeName;
        }

        public String getVerifyStatus() {
            return verifyStatus;
        }

        public void setVerifyStatus(String verifyStatus) {
            this.verifyStatus = verifyStatus;
        }

        public User() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.userId);
            dest.writeString(this.collegeId);
            dest.writeString(this.collegeName);
            dest.writeString(this.fullName);
            dest.writeString(this.profilePic);
            dest.writeString(this.isFollow);
            dest.writeString(this.verifyStatus);
        }

        protected User(Parcel in) {
            this.userId = in.readString();
            this.collegeId = in.readString();
            this.collegeName = in.readString();
            this.fullName = in.readString();
            this.profilePic = in.readString();
            this.isFollow = in.readString();
            this.verifyStatus = in.readString();
        }

        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel source) {
                return new User(source);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };
    }
}