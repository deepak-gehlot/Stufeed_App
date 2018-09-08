package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllInstituteFollowersListResponse {
    private String responseMessage = "";
    private String responseCode = "";
    @SerializedName("allfollowers")
    private ArrayList<InstituteFollower> followerArrayList;

    public ArrayList<InstituteFollower> getFollowerArrayList() {
        return followerArrayList;
    }

    public void setFollowerArrayList(ArrayList<InstituteFollower> followerArrayList) {
        this.followerArrayList = followerArrayList;
    }

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


    public class InstituteFollower {

        @SerializedName("userid")
        private String userId = "";


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

        @SerializedName("isFollow")
        private String isFollow = "";


        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getIsFollow() {
            return isFollow;
        }

        public void setIsFollow(String isFollow) {
            this.isFollow = isFollow;
        }
    }
}
