package com.stufeed.android.api.response;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.stufeed.android.BR;

import java.util.ArrayList;

public class GetUserDetailsResponse {

    private String responseMessage;
    private String responseCode;
    @SerializedName("alldetails")
    private Details allDetails = new Details();

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

    public Details getAllDetails() {
        return allDetails;
    }

    public void setAllDetails(Details allDetails) {
        this.allDetails = allDetails;
    }

    public class Details extends BaseObservable {
        @SerializedName("userid")
        private String userId = "";
        @SerializedName("devicetoken")
        private String deviceToken = "";
        @SerializedName("collegeid")
        private String collegeId = "";
        @SerializedName("college_name")
        private String collegeName = "";
        @SerializedName("fullname")
        private String fullName = "";
        @SerializedName("email")
        private String email = "";
        @SerializedName("profilepic")
        private String profilePic = "";
        @SerializedName("usertype")
        private String userType = "";
        @SerializedName("contactno")
        private String contactNo = "";
        @SerializedName("joiningyear")
        private String joiningYear = "";
        @SerializedName("aboutus")
        private String aboutUs = "";
        @SerializedName("gender")
        private String gender = "";
        @SerializedName("birthdate")
        private String birthDate = "";
        @SerializedName("degree")
        private String degree = "";
        @SerializedName("branch")
        private String branch = "";
        @SerializedName("designation")
        private String designation = "";
        @SerializedName("department")
        private String department = "";
        @SerializedName("skills")
        private String skills = "";
        @SerializedName("register_date")
        private String registerDate = "";
        @SerializedName("confirmationcode")
        private String confirmationCode = "";
        @SerializedName("verifycode")
        private String verifyCode = "";
        @SerializedName("is_active")
        private String is_active = "";
        @SerializedName("is_deleted")
        private String is_deleted = "";
        @SerializedName("postcount")
        private String postCount = "";
        @SerializedName("followercount")
        private String followerCount = "";
        @SerializedName("joincount")
        private String joinCount = "";
        @SerializedName("is_follow")
        private String isFollow = "";
        @SerializedName("is_block")
        private String isBlock = "";
        @SerializedName("description")
        private String about = "";
        @SerializedName("achivment")
        private ArrayList<GetAchievementListResponse.Achievement> achievementArrayList = new ArrayList<>();

        @Bindable
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
            notifyPropertyChanged(BR.userId);
        }

        @Bindable
        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
            notifyPropertyChanged(BR.deviceToken);
        }

        @Bindable
        public String getCollegeId() {
            return collegeId;
        }

        public void setCollegeId(String collegeId) {
            this.collegeId = collegeId;
            notifyPropertyChanged(BR.collegeId);
        }

        @Bindable
        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
            notifyPropertyChanged(BR.fullName);
        }

        @Bindable
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
            notifyPropertyChanged(BR.email);
        }

        @Bindable
        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
            notifyPropertyChanged(BR.profilePic);
        }

        @Bindable
        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
            notifyPropertyChanged(BR.userType);
        }

        @Bindable
        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
            notifyPropertyChanged(BR.contactNo);
        }

        @Bindable
        public String getJoiningYear() {
            return joiningYear;
        }

        public void setJoiningYear(String joiningYear) {
            this.joiningYear = joiningYear;
            notifyPropertyChanged(BR.joiningYear);
        }

        @Bindable
        public String getAboutUs() {
            return aboutUs;
        }

        public void setAboutUs(String aboutUs) {
            this.aboutUs = aboutUs;
            notifyPropertyChanged(BR.aboutUs);
        }

        @Bindable
        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
            notifyPropertyChanged(BR.gender);
        }

        @Bindable
        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(String birthDate) {
            this.birthDate = birthDate;
            notifyPropertyChanged(BR.birthDate);
        }

        @Bindable
        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
            notifyPropertyChanged(BR.degree);
        }

        @Bindable
        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
            notifyPropertyChanged(BR.branch);
        }

        @Bindable
        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
            notifyPropertyChanged(BR.designation);
        }

        @Bindable
        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
            notifyPropertyChanged(BR.department);
        }

        @Bindable
        public String getSkills() {
            return skills;
        }

        public void setSkills(String skills) {
            this.skills = skills;
            notifyPropertyChanged(BR.skills);
        }

        @Bindable
        public String getRegisterDate() {
            return registerDate;
        }

        public void setRegisterDate(String registerDate) {
            this.registerDate = registerDate;
            notifyPropertyChanged(BR.registerDate);
        }

        @Bindable
        public String getConfirmationCode() {
            return confirmationCode;
        }

        public void setConfirmationCode(String confirmationCode) {
            this.confirmationCode = confirmationCode;
            notifyPropertyChanged(BR.confirmationCode);
        }

        @Bindable
        public String getVerifyCode() {
            return verifyCode;
        }

        public void setVerifyCode(String verifyCode) {
            this.verifyCode = verifyCode;
            notifyPropertyChanged(BR.verifyCode);
        }

        @Bindable
        public String getIs_active() {
            return is_active;
        }

        public void setIs_active(String is_active) {
            this.is_active = is_active;
            notifyPropertyChanged(BR.is_active);
        }

        @Bindable
        public String getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(String is_deleted) {
            this.is_deleted = is_deleted;
            notifyPropertyChanged(BR.is_deleted);
        }

        @Bindable
        public String getCollegeName() {
            return collegeName;
        }

        public void setCollegeName(String collegeName) {
            this.collegeName = collegeName;
            notifyPropertyChanged(BR.collegeName);
        }

        public String getPostCount() {
            return postCount;
        }

        public void setPostCount(String postCount) {
            this.postCount = postCount;
        }

        public String getFollowerCount() {
            return followerCount;
        }

        public void setFollowerCount(String followerCount) {
            this.followerCount = followerCount;
        }

        public String getJoinCount() {
            return joinCount;
        }

        public void setJoinCount(String joinCount) {
            this.joinCount = joinCount;
        }

        public String getIsFollow() {
            return isFollow;
        }

        public void setIsFollow(String isFollow) {
            this.isFollow = isFollow;
        }

        public String getIsBlock() {
            return isBlock;
        }

        public void setIsBlock(String isBlock) {
            this.isBlock = isBlock;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public ArrayList<GetAchievementListResponse.Achievement> getAchievementArrayList() {
            return achievementArrayList;
        }

        public void setAchievementArrayList(ArrayList<GetAchievementListResponse.Achievement> achievementArrayList) {
            this.achievementArrayList = achievementArrayList;
        }
    }
}