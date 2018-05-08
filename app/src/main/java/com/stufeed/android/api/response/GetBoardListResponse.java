package com.stufeed.android.api.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetBoardListResponse {

    private String responseMessage;
    private String responseCode;
    @SerializedName("allboard")
    private ArrayList<Board> boardArrayList;
    @SerializedName("allcount")
    private Count allCount;

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

    public Count getAllCount() {
        return allCount;
    }

    public void setAllCount(Count allCount) {
        this.allCount = allCount;
    }

    public static class Board {
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
        @SerializedName("jointype")
        private String joinType;
        @SerializedName("totaljoinboard")
        private String totalJoinBoard;
        @SerializedName("joinCount")
        private String joinCount = "";
        @SerializedName("postCount")
        private String postCount = "";

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

        public String getJoinType() {
            return joinType;
        }

        public void setJoinType(String joinType) {
            this.joinType = joinType;
        }

        public String getTotalJoinBoard() {
            return totalJoinBoard;
        }

        public void setTotalJoinBoard(String totalJoinBoard) {
            this.totalJoinBoard = totalJoinBoard;
        }

        public String getJoinCount() {
            return joinCount;
        }

        public void setJoinCount(String joinCount) {
            this.joinCount = joinCount;
        }

        public String getPostCount() {
            return postCount;
        }

        public void setPostCount(String postCount) {
            this.postCount = postCount;
        }
    }

    public class Count {
        @SerializedName("postcount")
        private String postCount = "0";
        @SerializedName("followercount")
        private String followerCount = "0";
        @SerializedName("joincount")
        private String joinCount = "0";

        public String getPostCount() {
            if (TextUtils.isEmpty(postCount)) {
                return "0";
            } else {
                return postCount;
            }
        }

        public void setPostCount(String postCount) {
            this.postCount = postCount;
        }

        public String getFollowerCount() {
            if (TextUtils.isEmpty(followerCount)) {
                return "0";
            } else {
                return followerCount;
            }
        }

        public void setFollowerCount(String followerCount) {
            this.followerCount = followerCount;
        }

        public String getJoinCount() {
            if (TextUtils.isEmpty(joinCount)) {
                return "0";
            } else {
                return joinCount;
            }
        }

        public void setJoinCount(String joinCount) {
            this.joinCount = joinCount;
        }
    }
}