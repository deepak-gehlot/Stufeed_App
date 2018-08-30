package com.stufeed.android.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidquery.AQuery;
import com.github.chrisbanes.photoview.PhotoView;
import com.stufeed.android.R;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        AQuery aQuery = new AQuery(this);
        aQuery.id(photoView).image(getUrlFromBundle(), true, true, 0, R.drawable.user_default);
    }

    private String getUrlFromBundle() {
        return getIntent().getExtras().getString("image");
    }
}
