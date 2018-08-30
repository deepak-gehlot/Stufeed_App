package com.stufeed.android.view.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.stufeed.android.BR;

public class EditPostModel extends BaseObservable {

    private String userId = "";
    private String postId = "";
    private String boardId = "";
    private String title = "";
    private String description = "";
    private String allowComment = "";
    private String allowRepost = "";

    public EditPostModel(String userId, String postId, String boardId, String title, String description, String allowComment, String allowRepost) {
        this.userId = userId;
        this.postId = postId;
        this.boardId = boardId;
        this.title = title;
        this.description = description;
        this.allowComment = allowComment;
        this.allowRepost = allowRepost;
    }

    @Bindable
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        notifyPropertyChanged(BR.userId);
    }

    @Bindable
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
        notifyPropertyChanged(BR.postId);
    }

    @Bindable
    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
        notifyPropertyChanged(BR.boardId);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(String allowComment) {
        this.allowComment = allowComment;
        notifyPropertyChanged(BR.allowComment);
    }

    @Bindable
    public String getAllowRepost() {
        return allowRepost;
    }

    public void setAllowRepost(String allowRepost) {
        this.allowRepost = allowRepost;
        notifyPropertyChanged(BR.allowRePost);
    }
}

