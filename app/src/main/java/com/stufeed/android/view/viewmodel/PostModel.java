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

public class PostModel extends BaseObservable {
    private String userId = "";
    private String title = "";
    private String description = "";
    private File file;
    private int allowComment = 1;
    private int allowRePost = 1;
    private int type = 3;
    private String videoUrl = "";

    @Bindable
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        notifyPropertyChanged(BR.userId);
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
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        notifyPropertyChanged(BR.videoUrl);
    }


    //{"userid":1,"title":"test title","description":"test description","file":"file(image,audio)","alowcomment":0,
    // "alowrepost":0,"type":3,"videourl":""}
    public Map<String, RequestBody> getPostBody() {
        MediaType mediaTypeText = MediaType.parse("text/plain");
        MediaType mediaTypeImage = MediaType.parse("image/*");

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("userid", RequestBody.create(mediaTypeText, getUserId()));
        requestBodyMap.put("title", RequestBody.create(mediaTypeText, getTitle()));
        requestBodyMap.put("description", RequestBody.create(mediaTypeText, getDescription()));
        requestBodyMap.put("alowcomment", RequestBody.create(mediaTypeText, String.valueOf(getAllowComment
                ())));
        requestBodyMap.put("alowrepost", RequestBody.create(mediaTypeText, String.valueOf(getAllowRePost()
        )));
        requestBodyMap.put("type", RequestBody.create(mediaTypeText, String.valueOf(getType())));
        requestBodyMap.put("videourl", RequestBody.create(mediaTypeText, getVideoUrl()));
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
