package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String responseMessage;
    private String responseCode;

    @SerializedName("userdetail")
    private UserDetail user;

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

    public UserDetail getUser() {
        return user;
    }

    public void setUser(UserDetail user) {
        this.user = user;
    }
}

