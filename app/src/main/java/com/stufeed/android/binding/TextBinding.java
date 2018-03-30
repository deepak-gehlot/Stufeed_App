package com.stufeed.android.binding;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.stufeed.android.R;

/**
 * Created by HP on 3/29/2018.
 */
public class TextBinding {

    @BindingAdapter({"setTextValue"})
    public static void setImage(TextView textView, String text) {
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
    }
}
