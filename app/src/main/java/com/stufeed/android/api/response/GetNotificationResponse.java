package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetNotificationResponse {

    private String responseMessage = "";
    private String responseCode = "";
    @SerializedName("data")
    private ArrayList<NotiItem> notiItems;

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

    public ArrayList<NotiItem> getNotiItems() {
        return notiItems;
    }

    public void setNotiItems(ArrayList<NotiItem> notiItems) {
        this.notiItems = notiItems;
    }

    public class NotiItem {
        @SerializedName("un_id")
        private String unId = "";
        @SerializedName("user_id")
        private String userId = "";
        @SerializedName("user_recv_id")
        private String userRecvId = "";
        @SerializedName("un_msg")
        private String unMsg = "";
        @SerializedName("un_read")
        private String unRead = "";
        @SerializedName("un_date")
        private String unDate = "";

        public String getUnId() {
            return unId;
        }

        public void setUnId(String unId) {
            this.unId = unId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserRecvId() {
            return userRecvId;
        }

        public void setUserRecvId(String userRecvId) {
            this.userRecvId = userRecvId;
        }

        public String getUnMsg() {
            return unMsg;
        }

        public void setUnMsg(String unMsg) {
            this.unMsg = unMsg;
        }

        public String getUnRead() {
            return unRead;
        }

        public void setUnRead(String unRead) {
            this.unRead = unRead;
        }

        public String getUnDate() {
            return unDate;
        }

        public void setUnDate(String unDate) {
            this.unDate = unDate;
        }
    }
}
