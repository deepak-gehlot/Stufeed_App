package com.stufeed.android.api.response;

import com.google.gson.annotations.SerializedName;

public class CollegeDetails {

    private String responseMessage = "";
    private String responseCode = "";
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("collegeid")
        private String collegeId = "";
        @SerializedName("institutiontype")
        private String institutionType = "";
        @SerializedName("address")
        private String address = "";
        @SerializedName("affiliation_no")
        private String affiliationNo = "";
        private String city = "";
        @SerializedName("college_name")
        private String collegeName = "";
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

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCollegeId() {
            return collegeId;
        }

        public void setCollegeId(String collegeId) {
            this.collegeId = collegeId;
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

        public String getCollegeName() {
            return collegeName;
        }

        public void setCollegeName(String collegeName) {
            this.collegeName = collegeName;
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
    }
}
