package com.stufeed.android.api;

import com.stufeed.android.api.response.BlockedUserListResponse;
import com.stufeed.android.api.response.CollegeDetails;
import com.stufeed.android.api.response.CommentResponse;
import com.stufeed.android.api.response.CreateBoardResponse;
import com.stufeed.android.api.response.DeletePostResponse;
import com.stufeed.android.api.response.FollowResponse;
import com.stufeed.android.api.response.GetAchievementListResponse;
import com.stufeed.android.api.response.GetAllCollegeResponse;
import com.stufeed.android.api.response.GetAllCommentResponse;
import com.stufeed.android.api.response.GetAllSkillsResponse;
import com.stufeed.android.api.response.GetArchiveBoardListResponse;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.GetBoardMemberListResponse;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.GetEdukitResponse;
import com.stufeed.android.api.response.GetFollowerListResponse;
import com.stufeed.android.api.response.GetInstituteRegistrationResponse;
import com.stufeed.android.api.response.GetJoinBoardListResponse;
import com.stufeed.android.api.response.GetJoinBoardRequestResponse;
import com.stufeed.android.api.response.GetNotificationResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.GetSavedPostResponse;
import com.stufeed.android.api.response.GetSettingResponse;
import com.stufeed.android.api.response.GetUserDescriptionResponse;
import com.stufeed.android.api.response.GetUserDetailsResponse;
import com.stufeed.android.api.response.JoinBoardResponse;
import com.stufeed.android.api.response.LikeResponse;
import com.stufeed.android.api.response.LoginResponse;
import com.stufeed.android.api.response.PostResponse;
import com.stufeed.android.api.response.RePostResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.api.response.SavePostResponse;
import com.stufeed.android.api.response.SaveSettingResponse;
import com.stufeed.android.api.response.SinglePost;
import com.stufeed.android.api.response.UpdateCollegeResponse;
import com.stufeed.android.api.response.UpdateProfileResponse;
import com.stufeed.android.api.response.VerifyResponse;

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
    String IMAGE_URL = "http://65.60.10.51/~stufeed/uploads/";
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

    @FormUrlEncoded
    @POST("getallcolleges")
    Call<GetAllCollegeResponse> getColleges(@Field("sarchtext") String college);

    @FormUrlEncoded
    @POST("updatecollegeid")
    Call<UpdateCollegeResponse> setCollegeId(@Field("userid") String userId,
                                             @Field("collegeid") String collegeId);

    @FormUrlEncoded
    @POST("verifyuseremail")
    Call<VerifyResponse> verifyEmail(@Field("userid") String userId, @Field("verifycode") String code);

    @Multipart
    @POST("post")
    Call<PostResponse> post(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);

    @Multipart
    @POST("post")
    Call<PostResponse> post(@PartMap Map<String, RequestBody> map);

    @FormUrlEncoded
    @POST("editPost")
    Call<PostResponse> editPost(@Field("userid") String userId,
                                @Field("postid") String postId,
                                @Field("boardid") String boardId,
                                @Field("title") String title,
                                @Field("description") String description,
                                @Field("alowcomment") String allowComment,
                                @Field("Alowrepost") String allowRePost
    );

    @FormUrlEncoded
    @POST("getpost")
    Call<GetPostResponse> getAllPost(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("getuserpost")
    Call<GetPostResponse> getUserAllPost(@Field("userid") String userId);

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
                                @Field("postid") String postId,
                                @Field("type") String type);

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
    @POST("removePost")
    Call<SavePostResponse> removePost(@Field("userid") String userId,
                                      @Field("postid") String postId);

    @FormUrlEncoded
    @POST("follow")
    Call<FollowResponse> follow(@Field("userid") String userId,
                                @Field("followuserid") String followedUserId);

    @FormUrlEncoded
    @POST("getuserfollowers")
    Call<GetFollowerListResponse> getUserFollowers(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("getuserfollowing")
    Call<Response> getUserFollowing(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("addpollanswer")
    Call<FollowResponse> addPollAnswer(@Field("userid") String userId,
                                       @Field("questionid") String questionId,
                                       @Field("optionid") String optionId);

    @FormUrlEncoded
    @POST("getallcollegeusers")
    Call<GetCollegeUserResponse> getCollegeUsers(@Field("userid") String userId,
                                                 @Field("collegeid") String collegeId,
                                                 @Field("usertype") String userType);

    @FormUrlEncoded
    @POST("createboard")
    Call<CreateBoardResponse> createBoard(@Field("userid") String userId,
                                          @Field("boardname") String title,
                                          @Field("boarddescription") String description,
                                          @Field("isprivate") int isPrivate,
                                          @Field("iscircle") int isCircle);

    @FormUrlEncoded
    @POST("updateboard")
    Call<CreateBoardResponse> updateBoard(@Field("boardid") String boardId,
                                          @Field("boardname") String title,
                                          @Field("boarddescription") String description,
                                          @Field("isprivate") int isPrivate,
                                          @Field("iscircle") int isCircle);

    @FormUrlEncoded
    @POST("getalluserboard")
    Call<GetBoardListResponse> getBoardList(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("getalluserbaordwithcount")
    Call<GetBoardListResponse> getAllBoardList(@Field("userid") String userId,
                                               @Field("loginuserid") String loginUserId);

    @FormUrlEncoded
    @POST("getuserJoinboardMy")
    Call<GetJoinBoardListResponse> getAllBoardListMy(@Field("userid") String userId,
                                                     @Field("loginuserid") String loginUserId);

    @FormUrlEncoded
    @POST("joinboard")
    Call<JoinBoardResponse> joinBoard(@Field("userid") String userId,
                                      @Field("boardid") String boardId,
                                      @Field("joinerid") String joinUserId);

    @FormUrlEncoded
    @POST("privateboardjoinrequest")
    Call<JoinBoardResponse> requestJoinBoard(@Field("userid") String userId,
                                             @Field("boardid") String boardId,
                                             @Field("joinerid") String joinUserId);

    @FormUrlEncoded
    @POST("removeJoinBoardUser")
    Call<Response> removeBoardMember(@Field("userid") String userId,
                                     @Field("boardid") String boardId,
                                     @Field("joinerid") String joinerId);


    @FormUrlEncoded
    @POST("getuserJoinboard")
    Call<GetJoinBoardListResponse> getJoinBoardList(@Field("userid") String userId,
                                                    @Field("loginId") String loginUserId);

    @FormUrlEncoded
    @POST("getallusersavedpost")
    Call<GetSavedPostResponse> getSavedPost(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("getallboardjoinrequestlist")
    Call<GetJoinBoardRequestResponse> getJoinBoardRequestList(@Field("userid") String userId);


    @FormUrlEncoded
    @POST("savesettingsbyuser")
    Call<SaveSettingResponse> saveSetting(@Field("userid") String userId,
                                          @Field("collegeid") String collageId,
                                          @Field("issearch") String search,
                                          @Field("issound") String sound,
                                          @Field("institute_code") String instituteCode,
                                          @Field("isnotifications") String notification);

    @FormUrlEncoded
    @POST("getusersettings")
    Call<GetSettingResponse> getSetting(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("archiveboard")
    Call<Response> archiveBoard(@Field("boardid") String boardId);

    @FormUrlEncoded
    @POST("unarchiveboard")
    Call<Response> unarchiveBoard(@Field("boardid") String boardId);

    @FormUrlEncoded
    @POST("deleteboard")
    Call<Response> deleteBoard(@Field("boardid") String boardId);

    @FormUrlEncoded
    @POST("getalluserarchiveboard")
    Call<GetArchiveBoardListResponse> getArchiveBoardList(@Field("userid") String userId);

    //updateAllProfileData
    //updateAllProfileData
    @FormUrlEncoded
    @POST("profileupdate")
    Call<UpdateProfileResponse> updateAllProfileData(@Field("userid") String userId,
                                                     @Field("fullname") String name,
                                                     @Field("joiningyear") String joinYear,
                                                     @Field("aboutus") String aboutUs,
                                                     @Field("gender") String gender,
                                                     @Field("contactno") String mobile,
                                                     @Field("birthdate") String birthDate,
                                                     @Field("degree") String degree,
                                                     @Field("branch") String branch,
                                                     @Field("designation") String designation,
                                                     @Field("department") String department
    );

    @FormUrlEncoded
    @POST("changepassword")
    Call<Response> changePassword(@Field("userid") String userId,
                                  @Field("newpassword") String newPassword,
                                  @Field("oldpassword") String oldPassword);


    @Multipart
    @POST("updateprofilepic")
    Call<UpdateProfileResponse> uploadProfile(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("getallalluserdetials")
    Call<GetUserDetailsResponse> getUserAllInfo(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("saveuserskils")
    Call<Response> saveSkills(@Field("userid") String userId,
                              @Field("skills") String skills);

    @FormUrlEncoded
    @POST("getuserskills")
    Call<GetAllSkillsResponse> getUserSkills(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("saveuserachievement")
    Call<Response> saveAchievement(@Field("userid") String userId,
                                   @Field("title") String title,
                                   @Field("issudby") String issueBy,
                                   @Field("issuddate") String issueDate,
                                   @Field("achievmenttype") String achievementType,
                                   @Field("description") String description);

    @FormUrlEncoded
    @POST("updateachievement")
    Call<Response> updateAchievement(@Field("userid") String userId,
                                     @Field("achievmentid") String achievementId,
                                     @Field("title") String title,
                                     @Field("issudby") String issueBy,
                                     @Field("issuddate") String issueDate,
                                     @Field("achievmenttype") String achievementType,
                                     @Field("description") String description);

    @FormUrlEncoded
    @POST("getalluserarchivements")
    Call<GetAchievementListResponse> getAllAchievements(@Field("userid") String userId);


    @FormUrlEncoded
    @POST("achivementdelete")
    Call<Response> deleteAchievements(@Field("achievmentid") String id);

    @FormUrlEncoded
    @POST("forgot")
    Call<Response> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("instuteregistration")
    Call<GetInstituteRegistrationResponse> registerInstitute(@Field("collegeid") String collegeId,
                                                             @Field("college_name") String collegeName,
                                                             @Field("email") String email,
                                                             @Field("password") String password,
                                                             @Field("usertype") String userTyp,
                                                             @Field("contactno") String number,
                                                             @Field("institutiontype") String instituteType,
                                                             @Field("university_name") String universityName,
                                                             @Field("address") String address,
                                                             @Field("instituteid") String instituteId,
                                                             @Field("city") String city,
                                                             @Field("state") String state,
                                                             @Field("website") String website,
                                                             @Field("specialised_in") String specialisedIn,
                                                             @Field("year_of_establishment") String yearEstablishment,
                                                             @Field("managedby") String managedBy,
                                                             @Field("location") String location);

    @FormUrlEncoded
    @POST("blockusers")
    Call<Response> blockUser(@Field("userid") String userId,
                             @Field("blockuserid") String blockUserId);

    @FormUrlEncoded
    @POST("unblockusers")
    Call<Response> unblockUser(@Field("userid") String userId,
                               @Field("blockuserid") String blockUserId);

    @FormUrlEncoded
    @POST("blockuserslist")
    Call<BlockedUserListResponse> blockedUserList(@Field("userid") String userId);

    //saveuserdescription  {"userid":"1","description":"Helllo my test description"}
    //getuserdescription  {"userid":"1"}
    //boardmemberlist  {"userid":"1","boardid":"2"}
    //getpostbyboardid {"userid":"1","boardid":"2"}

    @FormUrlEncoded
    @POST("saveuserdescription")
    Call<Response> saveUserDescription(@Field("userid") String userId,
                                       @Field("description") String description);

    @FormUrlEncoded
    @POST("getuserdescription")
    Call<GetUserDescriptionResponse> getUserDescription(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("boardmemberlist")
    Call<GetBoardMemberListResponse> getBoardMemberList(@Field("userid") String userId,
                                                        @Field("boardid") String boardId);

    @FormUrlEncoded
    @POST("getpostbyboardid")
    Call<GetPostResponse> getPostByBoardId(@Field("userid") String userId,
                                           @Field("boardid") String boardId);


    @Multipart
    @POST("institutePost")
    Call<PostResponse> institutePost(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("getUserNotification")
    Call<GetNotificationResponse> getUserNotification(@Field("user_id") String userId);


    @FormUrlEncoded
    @POST("getEdukitPost")
    Call<GetEdukitResponse> getEdukitPost(@Field("user_id") String userId,
                                          @Field("edukit_id") String edukitId);

    @FormUrlEncoded
    @POST("getUserStatus")
    Call<GetUserDetailsResponse> getUserDetails(@Field("userid") String userId,
                                                @Field("otherUserId") String otherUserId);

    @FormUrlEncoded
    @POST("getPostById")
    Call<SinglePost> getPostById(@Field("postid") String postId);

    @FormUrlEncoded
    @POST("getCollegeDetials")
    Call<CollegeDetails> getCollegeDetails(@Field("collegeid") String collegeId);

    @FormUrlEncoded
    @POST("editInstituteProfile")
    Call<Response> editInstituteProfile(@Field("userId") String userId,
                                        @Field("collageId") String collageId,
                                        @Field("institutiontype") String instituteType,
                                        @Field("university_name") String universityName,
                                        @Field("address") String address,
                                        @Field("affiliation_no") String affiliationNo,
                                        @Field("college_name") String collegeName,
                                        @Field("city") String city,
                                        @Field("state") String state,
                                        @Field("website") String website,
                                        @Field("specialised_in") String specialisedIn,
                                        @Field("year_of_establishment") String yearOfEstablishment,
                                        @Field("managedby") String managedBy,
                                        @Field("location") String location,
                                        @Field("Contactno") String contactNo
    );

    @FormUrlEncoded
    @POST("acceptJoinboard")
    Call<JoinBoardResponse> acceptJoinBoard(@Field("userid") String userId,
                                            @Field("boardid") String boardId,
                                            @Field("joinerid") String joinUserId);

    @FormUrlEncoded
    @POST("rejectJoinboard")
    Call<JoinBoardResponse> rejectJoinBoard(@Field("userid") String userId,
                                            @Field("boardid") String boardId,
                                            @Field("joinerid") String joinUserId);

    @FormUrlEncoded
    @POST("deleteComment")
    Call<Response> deleteComment(@Field("userId") String userId,
                                 @Field("commentId") String commentId,
                                 @Field("postid") String postId);

    @FormUrlEncoded
    @POST("deleteInstitutePost")
    Call<Response> deleteInstitutePost(@Field("user_id") String userId,
                                       @Field("post_id") String postId);

}