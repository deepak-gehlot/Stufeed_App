package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Deepak Gehlot on 1/29/2018.
 */

public class GetAllCollegeResponse {

    private String responseMessage = "";
    private String responseCode = "";
    @SerializedName("getpost")
    private ArrayList<College> collegeArrayList = new ArrayList<>();

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

    public ArrayList<College> getCollegeArrayList() {
        return collegeArrayList;
    }

    public void setCollegeArrayList(ArrayList<College> collegeArrayList) {
        this.collegeArrayList = collegeArrayList;
    }

    public static class College {
        private String collegeId = "";
        private String collegeName = "";

        public String getCollegeId() {
            return collegeId;
        }

        public void setCollegeId(String collegeId) {
            this.collegeId = collegeId;
        }

        public String getCollegeName() {
            return collegeName;
        }

        public void setCollegeName(String collegeName) {
            this.collegeName = collegeName;
        }
    }
}
