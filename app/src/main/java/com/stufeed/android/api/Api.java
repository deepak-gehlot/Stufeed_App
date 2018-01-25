package com.stufeed.android.api;

import com.stufeed.android.api.response.LoginResponse;
import com.stufeed.android.api.response.PostResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface Api {

    String BASE_URL = "http://65.60.10.51/~stufeed/stufeed-webservice/";
    String SUCCESS = "200";

    @FormUrlEncoded
    @POST("registration")
    Call<LoginResponse> register(@Field("fullname") String fullName,
                                 @Field("email") String email,
                                 @Field("password") String password,
                                 @Field("contactno") String contactNumber,
                                 @Field("usertype") int userType);

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(@Field("loginemail") String email,
                              @Field("loginpassword") String password,
                              @Field("devicetoken") String deviceToken);

    //{"userid":1,"title":"test title","description":"test description","file":"file(image,audio)","alowcomment":0,
    // "alowrepost":0,"type":3,"videourl":""}
    @Multipart
    @POST("post")
    Call<PostResponse> post(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);
}
