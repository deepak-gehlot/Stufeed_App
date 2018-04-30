package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetEdukitResponse {

    private String responseMessage = "";
    private String responseCode = "";
    @SerializedName("data")
    private ArrayList<EdukitPost> postArrayList;

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

    public ArrayList<EdukitPost> getPostArrayList() {
        return postArrayList;
    }

    public void setPostArrayList(ArrayList<EdukitPost> postArrayList) {
        this.postArrayList = postArrayList;
    }

    public class EdukitPost {
        @SerializedName("id")
        private String id = "";
        @SerializedName("user_id")
        private String userId = "";
        @SerializedName("title")
        private String title = "";
        @SerializedName("description")
        private String description = "";
        @SerializedName("image")
        private String image = "";
        @SerializedName("file")
        private String file = "";
        @SerializedName("post_type")
        private String postType = "";
        @SerializedName("allow_comment")
        private String allowComment = "";
        @SerializedName("allow_repost")
        private String allowRepost = "";
        @SerializedName("edukit_id")
        private String edukitId = "";
        @SerializedName("board_id")
        private String boardId = "";
        @SerializedName("date")
        private String date = "";

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getAllowComment() {
            return allowComment;
        }

        public void setAllowComment(String allowComment) {
            this.allowComment = allowComment;
        }

        public String getAllowRepost() {
            return allowRepost;
        }

        public void setAllowRepost(String allowRepost) {
            this.allowRepost = allowRepost;
        }

        public String getEdukitId() {
            return edukitId;
        }

        public void setEdukitId(String edukitId) {
            this.edukitId = edukitId;
        }

        public String getBoardId() {
            return boardId;
        }

        public void setBoardId(String boardId) {
            this.boardId = boardId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
