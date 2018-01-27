package com.stufeed.android.view.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.stufeed.android.BR;

public class CommentModel extends BaseObservable {

    private String userId = "";
    private String postId = "";
    private String comment = "";

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
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        notifyPropertyChanged(BR.comment);
    }
}
