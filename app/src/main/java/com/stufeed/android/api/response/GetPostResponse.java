package com.stufeed.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetPostResponse {

    private String responseMessage;
    private String responseCode;
    @SerializedName("getpost")
    private ArrayList<Post> post;

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

    public ArrayList<Post> getPost() {
        return post;
    }

    public void setPost(ArrayList<Post> post) {
        this.post = post;
    }

    public static class Post implements Parcelable {
        public static final Creator<Post> CREATOR = new Creator<Post>() {
            @Override
            public Post createFromParcel(Parcel source) {
                return new Post(source);
            }

            @Override
            public Post[] newArray(int size) {
                return new Post[size];
            }
        };
        @SerializedName("Userid")
        private String userId = "";
        @SerializedName("Postid")
        private String postId = "";
        @SerializedName("Fullname")
        private String fullName = "";
        @SerializedName("Email")
        private String email = "";
        @SerializedName("Title")
        private String title = "";
        @SerializedName("Description")
        private String description = "";
        @SerializedName("Alowcomment")
        private String allowComment = "";
        @SerializedName("Alowrepost")
        private String allowRePost = "";
        @SerializedName("Posttype")
        private String postType = "";
        @SerializedName("Postdatetime")
        private String dateTime = "";
        @SerializedName("Filepath")
        private String filePath = "";
        @SerializedName("Profilepic")
        private String profilePic = "";
        @SerializedName("Image")
        private String image = "";
        @SerializedName("Totallike")
        private String totalLike = "0";
        @SerializedName("Totalcomment")
        private String totalComment = "0";

        public Post() {
        }

        protected Post(Parcel in) {
            this.userId = in.readString();
            this.postId = in.readString();
            this.fullName = in.readString();
            this.email = in.readString();
            this.title = in.readString();
            this.description = in.readString();
            this.allowComment = in.readString();
            this.allowRePost = in.readString();
            this.postType = in.readString();
            this.dateTime = in.readString();
            this.filePath = in.readString();
            this.profilePic = in.readString();
            this.image = in.readString();
            this.totalLike = in.readString();
            this.totalComment = in.readString();
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

        public String getAllowComment() {
            return allowComment;
        }

        public void setAllowComment(String allowComment) {
            this.allowComment = allowComment;
        }

        public String getAllowRePost() {
            return allowRePost;
        }

        public void setAllowRePost(String allowRePost) {
            this.allowRePost = allowRePost;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTotalLike() {
            return totalLike;
        }

        public void setTotalLike(String totalLike) {
            this.totalLike = totalLike;
        }

        public String getTotalComment() {
            return totalComment;
        }

        public void setTotalComment(String totalComment) {
            this.totalComment = totalComment;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.userId);
            dest.writeString(this.postId);
            dest.writeString(this.fullName);
            dest.writeString(this.email);
            dest.writeString(this.title);
            dest.writeString(this.description);
            dest.writeString(this.allowComment);
            dest.writeString(this.allowRePost);
            dest.writeString(this.postType);
            dest.writeString(this.dateTime);
            dest.writeString(this.filePath);
            dest.writeString(this.profilePic);
            dest.writeString(this.image);
            dest.writeString(this.totalLike);
            dest.writeString(this.totalComment);
        }
    }
}
