package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by HP on 2/11/2018.
 */

public class GetBoardListResponse {

    private String responseMessage;
    private String responseCode;
    @SerializedName("allboard")
    private ArrayList<Board> boardArrayList;

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

    public ArrayList<Board> getBoardArrayList() {
        return boardArrayList;
    }

    public void setBoardArrayList(ArrayList<Board> boardArrayList) {
        this.boardArrayList = boardArrayList;
    }

    public class Board {
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
        private String dateTime = "";
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

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
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
