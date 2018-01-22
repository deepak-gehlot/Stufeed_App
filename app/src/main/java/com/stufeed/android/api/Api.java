package com.stufeed.android.api;

import com.stufeed.android.api.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "http://65.60.10.51/~stufeed/stufeed-webservice/";
    String SUCCESS = "200";

    @FormUrlEncoded
    @POST("registration")
    Call<LoginResponse> register(@Field("firstname") String firstName,
                                 @Field("lastname") String lastName,
                                 @Field("email") String email,
                                 @Field("password") String password,
                                 @Field("contactno") String contactNumber,
                                 @Field("usertype") int userType);
}
