package com.stufeed.android.binding;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.stufeed.android.util.Constant;
import com.stufeed.android.util.TimeUtil;

/**
 * Created by Deepak Gehlot on 1/29/2018.
 */
public class TimeBinding {
    @BindingAdapter({"time"})
    public static void setTimeAgo(TextView textView, String time) {
        String timeAgo = TimeUtil.getTimeAgo(textView.getContext(), time, Constant.FORMAT_DATE_TIME);
        textView.setText(timeAgo);
    }
}