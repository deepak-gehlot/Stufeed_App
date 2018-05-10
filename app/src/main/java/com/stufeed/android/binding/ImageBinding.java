package com.stufeed.android.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.stufeed.android.R;

public class ImageBinding {

    @BindingAdapter({"profileImageUrl"})
    public static void setImage(ImageView imageView, String url) {
        AQuery aQuery = new AQuery(imageView.getContext());
        aQuery.id(imageView).image(url, true, true, 80, R.drawable.user_default);
    }

    @BindingAdapter({"postImageUrl"})
    public static void setPostImage(ImageView imageView, String url) {
        AQuery aQuery = new AQuery(imageView.getContext());
        aQuery.id(imageView).image(url, true, true, 200, R.drawable.gallery_icon);
    }

    @BindingAdapter({"imageDrawable"})
    public static void setImageFromDrawable(ImageView imageView, int drawable) {
        AQuery aQuery = new AQuery(imageView.getContext());
        aQuery.id(imageView).image(drawable);
    }

}
