package com.stufeed.android.binding;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.stufeed.android.R;

public class ImageBinding {

    @BindingAdapter({"profileImageUrl"})
    public static void setImage(ImageView imageView, String url) {
        AQuery aQuery = new AQuery(imageView.getContext());
        if (TextUtils.isEmpty(url)) {
            aQuery.id(imageView).image(R.drawable.person_icon);
        } else {
            aQuery.id(imageView).image(url, true, true, 60, R.drawable.person_icon);
        }
    }

    @BindingAdapter({"postImageUrl"})
    public static void setPostImage(ImageView imageView, String url) {
        AQuery aQuery = new AQuery(imageView.getContext());
        if (TextUtils.isEmpty(url)) {
            aQuery.id(imageView).image(R.drawable.image_placeholder);
        } else {
            aQuery.id(imageView).image(url, true, true, 120, R.drawable.image_placeholder);
        }
    }

    @BindingAdapter({"imageDrawable"})
    public static void setImageFromDrawable(ImageView imageView, int drawable) {
        AQuery aQuery = new AQuery(imageView.getContext());
        aQuery.id(imageView).image(drawable);
    }

}
