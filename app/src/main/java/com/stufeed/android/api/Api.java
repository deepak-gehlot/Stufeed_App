package com.stufeed.android.api;

import com.stufeed.android.api.response.CommentResponse;
import com.stufeed.android.api.response.DeletePostResponse;
import com.stufeed.android.api.response.FollowResponse;
import com.stufeed.android.api.response.GetAllCommentResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.LikeResponse;
import com.stufeed.android.api.response.LoginResponse;
import com.stufeed.android.api.response.PostResponse;
import com.stufeed.android.api.response.RePostResponse;
import com.stufeed.android.api.response.SavePostResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    @Multipart
    @POST("post")
    Call<PostResponse> post(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);

    @GET("getpost")
    Call<GetPostResponse> getAllPost();

    @FormUrlEncoded
    @POST("commentpost")
    Call<CommentResponse> postComment(@Field("userid") String userId,
                                      @Field("postid") String postId,
                                      @Field("comment") String comment);

    @FormUrlEncoded
    @POST("getcomment")
    Call<GetAllCommentResponse> getAllComment(@Field("postid") String postId);

    @FormUrlEncoded
    @POST("likepost")
    Call<LikeResponse> likePost(@Field("userid") String userId,
                                @Field("postid") String postId);

    @FormUrlEncoded
    @POST("postdelete")
    Call<DeletePostResponse> deletePost(@Field("userid") String userId,
                                        @Field("postid") String postId);

    @FormUrlEncoded
    @POST("repost")
    Call<RePostResponse> rePost(@Field("userid") String userId,
                                @Field("postid") String postId);

    @FormUrlEncoded
    @POST("savepost")
    Call<SavePostResponse> savePost(@Field("userid") String userId,
                                    @Field("postid") String postId);

    @FormUrlEncoded
    @POST("follow")
    Call<FollowResponse> follow(@Field("userid") String userId,
                                @Field("followuserid") String followedUserId);

    @FormUrlEncoded
    @POST("addpollanswer")
    Call<FollowResponse> follow(@Field("userid") String userId,
                                @Field("questionid") String followedUserId,
                                @Field("optionid") String optionId);
}
