package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 3/24/2018.
 */

public class GetAllSkillsResponse {
    private String responseMessage = "";
    @SerializedName("allskills")
    private String allSkills = "";
    private String responseCode = "";

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getAllSkills() {
        return allSkills;
    }

    public void setAllSkills(String allSkills) {
        this.allSkills = allSkills;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
