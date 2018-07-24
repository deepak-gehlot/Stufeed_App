package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UnVerifyUserListResponse {

    private String responseMessage = "";
    private String responseCode = "";
    private ArrayList<Data> data = new ArrayList<>();

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("userid")
        private String userId = "";
        @SerializedName("collegeid")
        private String collegeId = "";
        @SerializedName("user_instituteId")
        private String userInstituteId = "";
        @SerializedName("usertype")
        private String userType = "";
        @SerializedName("fullname")
        private String fullName = "";
        @SerializedName("email")
        private String email = "";
        @SerializedName("profilepic")
        private String profilePic = "";
        @SerializedName("contactno")
        private String contactNo = "";

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

        public String getUserInstituteId() {
            return userInstituteId;
        }

        public void setUserInstituteId(String userInstituteId) {
            this.userInstituteId = userInstituteId;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
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

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }
    }
}
