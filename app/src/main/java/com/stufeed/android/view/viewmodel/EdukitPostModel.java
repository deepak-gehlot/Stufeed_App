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
    private String type = ""; // 0 = board, 1 = edukit
    private String postValue = ""; // 3 image
    private String boardId = "0";
    private String edukitId = "0";

    /*For post in board*/
    private int postType = 3;       // type of post in {@link PostModel}
    private String videoUrl = "";
    private String pollQuestion = "";
    private String pollOption1 = "";
    private String pollOption2 = "";
    private String pollOption = "";
    private String article_title = "";
    private String article_thumbnail = "";

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

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPollQuestion() {
        return pollQuestion;
    }

    public void setPollQuestion(String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    public String getPollOption1() {
        return pollOption1;
    }

    public void setPollOption1(String pollOption1) {
        this.pollOption1 = pollOption1;
    }

    public String getPollOption2() {
        return pollOption2;
    }

    public void setPollOption2(String pollOption2) {
        this.pollOption2 = pollOption2;
    }

    public String getPollOption() {
        return pollOption;
    }

    public void setPollOption(String pollOption) {
        this.pollOption = pollOption;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_thumbnail() {
        return article_thumbnail;
    }

    public void setArticle_thumbnail(String article_thumbnail) {
        this.article_thumbnail = article_thumbnail;
    }

    public String getPostValue() {
        return postValue;
    }

    public void setPostValue(String postValue) {
        this.postValue = postValue;
    }

    /*user_id:45
    title:amit demo
    desc:amit desc
    board_id:220
    post_value:1
    post_type:0
    edukit_id:44
    file : (optional)
    image : (optional)
    */
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
        requestBodyMap.put("post_value", RequestBody.create(mediaTypeText, String.valueOf(getPostValue())));
        requestBodyMap.put("edukit_id", RequestBody.create(mediaTypeText, getEdukitId()));
        requestBodyMap.put("board_id", RequestBody.create(mediaTypeText, getBoardId()));
        return requestBodyMap;
    }

    public Map<String, RequestBody> getBoardPostBody() {
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
        requestBodyMap.put("type", RequestBody.create(mediaTypeText, String.valueOf(getPostType())));
        requestBodyMap.put("videourl", RequestBody.create(mediaTypeText, getVideoUrl()));
        requestBodyMap.put("article_title", RequestBody.create(mediaTypeText, getArticle_title()));
        requestBodyMap.put("article_thumbnail", RequestBody.create(mediaTypeText, getArticle_thumbnail()));
        requestBodyMap.put("pollquestion", RequestBody.create(mediaTypeText, getPollQuestion()));
        requestBodyMap.put("polloption", RequestBody.create(mediaTypeText, getPollOption()));
        requestBodyMap.put("boardid", RequestBody.create(mediaTypeText, getBoardId()));
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
