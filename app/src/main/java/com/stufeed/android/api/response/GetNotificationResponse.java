package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetNotificationResponse {

    private String responseMessage = "";
    private String responseCode = "";
    private int count = 0;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
        @SerializedName("un_type")
        private String unType = "";
        @SerializedName("un_postId")
        private String unPostId = "";
        @SerializedName("profilepic")
        private String profilePic = "";

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

        public String getUnType() {
            return unType;
        }

        public void setUnType(String unType) {
            this.unType = unType;
        }

        public String getUnPostId() {
            return unPostId;
        }

        public void setUnPostId(String unPostId) {
            this.unPostId = unPostId;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }
    }
}
