package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

public class GetSettingResponse {

    private String responseMessage;
    private String responseCode;
    @SerializedName("allsettings")
    private Setting setting;

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

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

   public class Setting {
        @SerializedName("userid")
        private String userId = "";
        @SerializedName("issearch")
        private String isSearch = "";
        @SerializedName("issound")
        private String isSound = "";
        @SerializedName("institute_code")
        private String instituteCode = "";
        @SerializedName("isnotifications")
        private String isNotification = "";

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getIsSearch() {
            return isSearch;
        }

        public void setIsSearch(String isSearch) {
            this.isSearch = isSearch;
        }

        public String getIsSound() {
            return isSound;
        }

        public void setIsSound(String isSound) {
            this.isSound = isSound;
        }

        public String getIsNotification() {
            return isNotification;
        }

        public void setIsNotification(String isNotification) {
            this.isNotification = isNotification;
        }

       public String getInstituteCode() {
           return instituteCode;
       }

       public void setInstituteCode(String instituteCode) {
           this.instituteCode = instituteCode;
       }
   }
}