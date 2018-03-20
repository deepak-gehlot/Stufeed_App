package com.stufeed.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by HP on 3/17/2018.
 */

public class GetAchievementListResponse {

    private String responseMessage;
    private String responseCode;
    @SerializedName("allachivement")
    private ArrayList<Achievement> achievementArrayList;

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

    public ArrayList<Achievement> getAchievementArrayList() {
        return achievementArrayList;
    }

    public void setAchievementArrayList(ArrayList<Achievement> achievementArrayList) {
        this.achievementArrayList = achievementArrayList;
    }

    public static class Achievement implements Parcelable {
        @SerializedName("achievmentid")
        private String achievmentId = "";
        @SerializedName("userid")
        private String userId = "";
        @SerializedName("title")
        private String title = "";
        @SerializedName("issudby")
        private String issuedBy = "";
        @SerializedName("issuddate")
        private String issuedDate = "";
        @SerializedName("achievmenttype")
        private String achievmentType = "";
        @SerializedName("description")
        private String description = "";
        @SerializedName("adddatetime")
        private String dateTime = "";

        public String getAchievmentId() {
            return achievmentId;
        }

        public void setAchievmentId(String achievmentId) {
            this.achievmentId = achievmentId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIssuedBy() {
            return issuedBy;
        }

        public void setIssuedBy(String issuedBy) {
            this.issuedBy = issuedBy;
        }

        public String getIssuedDate() {
            return issuedDate;
        }

        public void setIssuedDate(String issuedDate) {
            this.issuedDate = issuedDate;
        }

        public String getAchievmentType() {
            return achievmentType;
        }

        public void setAchievmentType(String achievmentType) {
            this.achievmentType = achievmentType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.achievmentId);
            dest.writeString(this.userId);
            dest.writeString(this.title);
            dest.writeString(this.issuedBy);
            dest.writeString(this.issuedDate);
            dest.writeString(this.achievmentType);
            dest.writeString(this.description);
            dest.writeString(this.dateTime);
        }

        public Achievement() {
        }

        protected Achievement(Parcel in) {
            this.achievmentId = in.readString();
            this.userId = in.readString();
            this.title = in.readString();
            this.issuedBy = in.readString();
            this.issuedDate = in.readString();
            this.achievmentType = in.readString();
            this.description = in.readString();
            this.dateTime = in.readString();
        }

        public static final Parcelable.Creator<Achievement> CREATOR = new Parcelable.Creator<Achievement>() {
            @Override
            public Achievement createFromParcel(Parcel source) {
                return new Achievement(source);
            }

            @Override
            public Achievement[] newArray(int size) {
                return new Achievement[size];
            }
        };
    }
}
