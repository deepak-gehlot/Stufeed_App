package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 3/11/2018.
 */

public class UpdateProfileResponse {

    private String responseMessage;
    private String responseCode;
    @SerializedName("profilepic")
    private String profilePic;

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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
