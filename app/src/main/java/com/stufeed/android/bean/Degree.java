package com.stufeed.android.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 3/11/2018.
 */

public class Degree {

    @SerializedName("Short form")
    private String shortForm;
    private String degree;

    public String getShortForm() {
        return shortForm;
    }

    public void setShortForm(String shortForm) {
        this.shortForm = shortForm;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
