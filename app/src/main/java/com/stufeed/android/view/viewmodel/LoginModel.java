package com.stufeed.android.view.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.stufeed.android.BR;

public class LoginModel extends BaseObservable {
    private String email;
    private String password;
    private String deviceToken;

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
        notifyPropertyChanged(BR.deviceToken);
    }
}
