package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Deepak Gehlot on 3/29/2018.
 */
public class GetJoinBoardRequestResponse {

    private String responseMessage = "";
    private String responseCode = "";
    @SerializedName("allrequest")
    private ArrayList<Request> requestArrayList;

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

    public ArrayList<Request> getRequestArrayList() {
        return requestArrayList;
    }

    public void setRequestArrayList(ArrayList<Request> requestArrayList) {
        this.requestArrayList = requestArrayList;
    }

    public class Request {
        @SerializedName("boardid")
        private String boardId = "";
        @SerializedName("userid")
        private String userId = "";
        @SerializedName("boardname")
        private String boardName = "";
        @SerializedName("boarddescription")
        private String boardDescription = "";
        @SerializedName("isprivate")
        private String isPrivate = "";
        @SerializedName("iscircle")
        private String isCircle = "";
        @SerializedName("createddatetime")
        private String createdDateTime = "";
        @SerializedName("fullname")
        private String fullName = "";
        @SerializedName("profilepic")
        private String profilePic = "";
        @SerializedName("usertype")
        private String userType = "";
        @SerializedName("contactno")
        private String contactNo = "";

        public String getBoardId() {
            return boardId;
        }

        public void setBoardId(String boardId) {
            this.boardId = boardId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getBoardName() {
            return boardName;
        }

        public void setBoardName(String boardName) {
            this.boardName = boardName;
        }

        public String getBoardDescription() {
            return boardDescription;
        }

        public void setBoardDescription(String boardDescription) {
            this.boardDescription = boardDescription;
        }

        public String getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(String isPrivate) {
            this.isPrivate = isPrivate;
        }

        public String getIsCircle() {
            return isCircle;
        }

        public void setIsCircle(String isCircle) {
            this.isCircle = isCircle;
        }

        public String getCreatedDateTime() {
            return createdDateTime;
        }

        public void setCreatedDateTime(String createdDateTime) {
            this.createdDateTime = createdDateTime;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }
    }
}