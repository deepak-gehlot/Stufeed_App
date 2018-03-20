package com.stufeed.android.view.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.stufeed.android.BR;

/**
 * Created by HP on 3/16/2018.
 */

public class AddAchievementModel extends BaseObservable {

    public String title;
    public String issueBy;
    public String issueDate;
    public String achievmentType;
    public String description;

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(String issueBy) {
        this.issueBy = issueBy;
        notifyPropertyChanged(BR.issueBy);
    }

    @Bindable
    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
        notifyPropertyChanged(BR.issueDate);
    }

    @Bindable
    public String getAchievmentType() {
        return achievmentType;
    }

    public void setAchievmentType(String achievmentType) {
        this.achievmentType = achievmentType;
        notifyPropertyChanged(BR.achievmentType);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.designation);
    }
}