package com.stufeed.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Deepak Gehlot on 1/29/2018.
 */
public class GetAllCollegeResponse {

    private String responseMessage = "";
    private String responseCode = "";

    @SerializedName("getpost")
    private ArrayList<College> collegeArrayList = new ArrayList<>();

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

    public ArrayList<College> getCollegeArrayList() {
        return collegeArrayList;
    }

    public void setCollegeArrayList(ArrayList<College> collegeArrayList) {
        this.collegeArrayList = collegeArrayList;
    }

    public static class College implements Parcelable {

        private String followerCount = "";
        private String collegeId = "";
        private String collegeName = "";
        @SerializedName("institutiontype")
        private String institutionType = "";
        @SerializedName("institute_code")
        private String instituteCode = "";
        @SerializedName("address")
        private String address = "";
        @SerializedName("affiliation_no")
        private String affiliationNo = "";
        @SerializedName("college_type")
        private String collegeType = "";
        @SerializedName("district")
        private String district = "";
        @SerializedName("location")
        private String location = "";
        @SerializedName("management")
        private String management = "";
        @SerializedName("specialised_in")
        private String specialisedIn = "";
        @SerializedName("state")
        private String state = "";
        @SerializedName("university_name")
        private String universityName = "";
        @SerializedName("university_type")
        private String universityType = "";
        @SerializedName("upload_year")
        private String uploadYear = "";
        @SerializedName("website")
        private String website = "";
        @SerializedName("year_of_establishment")
        private String yearOfEstablishment = "";

        public String getFollowerCount() {
            return followerCount;
        }

        public void setFollowerCount(String followerCount) {
            this.followerCount = followerCount;
        }

        public String getCollegeId() {
            return collegeId;
        }

        public void setCollegeId(String collegeId) {
            this.collegeId = collegeId;
        }

        public String getCollegeName() {
            return collegeName;
        }

        public void setCollegeName(String collegeName) {
            this.collegeName = collegeName;
        }

        public String getInstitutionType() {
            return institutionType;
        }

        public void setInstitutionType(String institutionType) {
            this.institutionType = institutionType;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAffiliationNo() {
            return affiliationNo;
        }

        public void setAffiliationNo(String affiliationNo) {
            this.affiliationNo = affiliationNo;
        }

        public String getCollegeType() {
            return collegeType;
        }

        public void setCollegeType(String collegeType) {
            this.collegeType = collegeType;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getManagement() {
            return management;
        }

        public void setManagement(String management) {
            this.management = management;
        }

        public String getSpecialisedIn() {
            return specialisedIn;
        }

        public void setSpecialisedIn(String specialisedIn) {
            this.specialisedIn = specialisedIn;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUniversityName() {
            return universityName;
        }

        public void setUniversityName(String universityName) {
            this.universityName = universityName;
        }

        public String getUniversityType() {
            return universityType;
        }

        public void setUniversityType(String universityType) {
            this.universityType = universityType;
        }

        public String getUploadYear() {
            return uploadYear;
        }

        public void setUploadYear(String uploadYear) {
            this.uploadYear = uploadYear;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getYearOfEstablishment() {
            return yearOfEstablishment;
        }

        public void setYearOfEstablishment(String yearOfEstablishment) {
            this.yearOfEstablishment = yearOfEstablishment;
        }

        public String getInstituteCode() {
            return instituteCode;
        }

        public void setInstituteCode(String instituteCode) {
            this.instituteCode = instituteCode;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.followerCount);
            dest.writeString(this.collegeId);
            dest.writeString(this.collegeName);
            dest.writeString(this.institutionType);
            dest.writeString(this.instituteCode);
            dest.writeString(this.address);
            dest.writeString(this.affiliationNo);
            dest.writeString(this.collegeType);
            dest.writeString(this.district);
            dest.writeString(this.location);
            dest.writeString(this.management);
            dest.writeString(this.specialisedIn);
            dest.writeString(this.state);
            dest.writeString(this.universityName);
            dest.writeString(this.universityType);
            dest.writeString(this.uploadYear);
            dest.writeString(this.website);
            dest.writeString(this.yearOfEstablishment);
        }

        public College() {
        }

        protected College(Parcel in) {
            this.followerCount = in.readString();
            this.collegeId = in.readString();
            this.collegeName = in.readString();
            this.institutionType = in.readString();
            this.instituteCode = in.readString();
            this.address = in.readString();
            this.affiliationNo = in.readString();
            this.collegeType = in.readString();
            this.district = in.readString();
            this.location = in.readString();
            this.management = in.readString();
            this.specialisedIn = in.readString();
            this.state = in.readString();
            this.universityName = in.readString();
            this.universityType = in.readString();
            this.uploadYear = in.readString();
            this.website = in.readString();
            this.yearOfEstablishment = in.readString();
        }

        public static final Parcelable.Creator<College> CREATOR = new Parcelable.Creator<College>() {
            @Override
            public College createFromParcel(Parcel source) {
                return new College(source);
            }

            @Override
            public College[] newArray(int size) {
                return new College[size];
            }
        };
    }
}