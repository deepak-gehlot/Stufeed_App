package com.stufeed.android.api.response;

/**
 * Created by HP on 3/5/2018.
 */

public class SaveSettingResponse {

    private String responseMessage;
    private String responseCode;

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
}
