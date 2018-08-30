package com.stufeed.android.api.response;

/**
 * Created by HP on 3/24/2018.
 */

public class GetInstituteRegistrationResponse {

    private String responseMessage;
    private String responseCode;
    private String userid;

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
