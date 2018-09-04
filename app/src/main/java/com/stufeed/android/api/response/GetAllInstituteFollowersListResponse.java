package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllInstituteFollowersListResponse {
    private String responseMessage = "";
    private String responseCode = "";
    @SerializedName("allfollowers")
    private ArrayList<InstituteFollower> followerArrayList;

    public ArrayList<InstituteFollower> getFollowerArrayList() {
        return followerArrayList;
    }

    public void setFollowerArrayList(ArrayList<InstituteFollower> followerArrayList) {
        this.followerArrayList = followerArrayList;
    }

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


    public class InstituteFollower {

        @SerializedName("userid")
        private String userId = "";
        @SerializedName("devicetoken")
        private String deviceToken = "";
        @SerializedName("collegeid")
        private String collegeId = "";
        @SerializedName("user_instituteId")
        private String userInsituteId = "";
        @SerializedName("usertype")
        private String userType = "";
        @SerializedName("fullname")
        private String fullName = "";
        @SerializedName("firstname")
        private String firstName = "";
        @SerializedName("lastname")
        private String lastName = "";
        @SerializedName("email")
        private String email = "";
        @SerializedName("profilepic")
        private String profilePic = "";
        @SerializedName("password")
        private String password = "";
        @SerializedName("contactno")
        private String contactNo = "";
        @SerializedName("joiningyear")
        private String joinInYear = "";
        @SerializedName("aboutus")
        private String aboutUs = "";
        @SerializedName("description")
        private String description = "";
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
        private String isActive = "";
        @SerializedName("is_deleted")
        private String isDeleted = "";
        @SerializedName("verifyStatus")
        private String verifyStatus = "";

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getCollegeId() {
            return collegeId;
        }

        public void setCollegeId(String collegeId) {
            this.collegeId = collegeId;
        }

        public String getUserInsituteId() {
            return userInsituteId;
        }

        public void setUserInsituteId(String userInsituteId) {
            this.userInsituteId = userInsituteId;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getJoinInYear() {
            return joinInYear;
        }

        public void setJoinInYear(String joinInYear) {
            this.joinInYear = joinInYear;
        }

        public String getAboutUs() {
            return aboutUs;
        }

        public void setAboutUs(String aboutUs) {
            this.aboutUs = aboutUs;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(String birthDate) {
            this.birthDate = birthDate;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getSkills() {
            return skills;
        }

        public void setSkills(String skills) {
            this.skills = skills;
        }

        public String getRegisterDate() {
            return registerDate;
        }

        public void setRegisterDate(String registerDate) {
            this.registerDate = registerDate;
        }

        public String getConfirmationCode() {
            return confirmationCode;
        }

        public void setConfirmationCode(String confirmationCode) {
            this.confirmationCode = confirmationCode;
        }

        public String getVerifyCode() {
            return verifyCode;
        }

        public void setVerifyCode(String verifyCode) {
            this.verifyCode = verifyCode;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public String getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(String isDeleted) {
            this.isDeleted = isDeleted;
        }

        public String getVerifyStatus() {
            return verifyStatus;
        }

        public void setVerifyStatus(String verifyStatus) {
            this.verifyStatus = verifyStatus;
        }
    }
}
