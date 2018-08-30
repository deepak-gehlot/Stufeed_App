package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllCommentResponse {

    private String responseMessage = "";
    private String responseCode = "";
    @SerializedName("getpost")
    private ArrayList<Comment> commentArrayList = new ArrayList<>();

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

    public ArrayList<Comment> getCommentArrayList() {
        return commentArrayList;
    }

    public void setCommentArrayList(ArrayList<Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
    }

    public static class Comment {
        private String id;
        @SerializedName("Userid")
        private String userId;
        @SerializedName("Postid")
        private String postId;
        @SerializedName("Fullname")
        private String fullName;
        @SerializedName("Email")
        private String email;
        @SerializedName("Comment")
        private String comment;
        @SerializedName("Datetime")
        private String dateTime;
        @SerializedName("Profilepic")
        private String profilePic;
        @SerializedName("usertype")
        private String userType;

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }
    }
}
