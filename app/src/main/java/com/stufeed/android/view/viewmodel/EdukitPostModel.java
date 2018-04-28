package com.stufeed.android.view.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.stufeed.android.BR;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EdukitPostModel extends BaseObservable {

    private String userId = "";
    private String title = "";
    private String description = "";
    private File file;
    private int allowComment = 1; // 1 = true, 0 = false
    private int allowRePost = 1;   // 1 = true, 0 = false
    private String type = ""; // 1 = board, 2 = edukit
    private String boardId = "0";
    private String edukitId = "0";

    @Bindable
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        notifyPropertyChanged(BR.file);
    }

    @Bindable
    public int getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(int allowComment) {
        this.allowComment = allowComment;
        notifyPropertyChanged(BR.allowComment);
    }

    @Bindable
    public int getAllowRePost() {
        return allowRePost;
    }

    public void setAllowRePost(int allowRePost) {
        this.allowRePost = allowRePost;
        notifyPropertyChanged(BR.allowRePost);
    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
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
    public String getEdukitId() {
        return edukitId;
    }

    public void setEdukitId(String edukitId) {
        this.edukitId = edukitId;
        notifyPropertyChanged(BR.edukitId);
    }


    /*user_id:1
title:demo
desc:desc
allow_comment:
allow_repost:
post_type:aaaa
edukit_id:1
board_id:1,file*/
    public Map<String, RequestBody> getPostBody() {
        MediaType mediaTypeText = MediaType.parse("text/plain");
        MediaType mediaTypeImage = MediaType.parse("image/*");

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("user_id", RequestBody.create(mediaTypeText, getUserId()));
        requestBodyMap.put("title", RequestBody.create(mediaTypeText, getTitle()));
        requestBodyMap.put("desc", RequestBody.create(mediaTypeText, getDescription()));
        requestBodyMap.put("allow_comment", RequestBody.create(mediaTypeText, String.valueOf(getAllowComment
                ())));
        requestBodyMap.put("allow_repost", RequestBody.create(mediaTypeText, String.valueOf(getAllowRePost()
        )));
        requestBodyMap.put("post_type", RequestBody.create(mediaTypeText, String.valueOf(getType())));
        requestBodyMap.put("edukit_id", RequestBody.create(mediaTypeText, getBoardId()));
        requestBodyMap.put("board_id", RequestBody.create(mediaTypeText, getBoardId()));
        return requestBodyMap;
    }

    @NonNull
    public MultipartBody.Part prepareFilePart(String partName, File file) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils
        // /FileUtils.java
        // use the FileUtils to get the actual file by uri
        // create RequestBody instance from file
        MediaType mediaTypeImage = MediaType.parse("image/*");
        RequestBody requestFile =
                RequestBody.create(
                        mediaTypeImage,
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
