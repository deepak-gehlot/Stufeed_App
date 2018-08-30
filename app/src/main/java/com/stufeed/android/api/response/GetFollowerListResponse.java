package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by HP on 4/4/2018.
 */

public class GetFollowerListResponse {

    private String responseMessage = "";
    private String responseCode = "";
    @SerializedName("allfollowers")
    private ArrayList<User> userArrayList;

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

    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }

    public void setUserArrayList(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    public class User {
        @SerializedName("followuserid")
        private String followUserid = "";
        @SerializedName("fullname")
        private String fullName = "";
        @SerializedName("contactno")
        private String contactNo = "";
        @SerializedName("usertype")
        private String userType = "";
        @SerializedName("email")
        private String email = "";
        @SerializedName("profilepic")
        private String profilePic = "";
        private String isFollow = "";

        public String getFollowUserid() {
            return followUserid;
        }

        public void setFollowUserid(String followUserid) {
            this.followUserid = followUserid;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
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

        public String getIsFollow() {
            return isFollow;
        }

        public void setIsFollow(String isFollow) {
            this.isFollow = isFollow;
        }
    }
}