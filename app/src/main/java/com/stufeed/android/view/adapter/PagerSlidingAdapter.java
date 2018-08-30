package com.stufeed.android.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.stufeed.android.R;

public class PagerSlidingAdapter extends PagerAdapter {
    private int[] images;
    private LayoutInflater inflater;
    private Context context;

    public PagerSlidingAdapter(int[] images, Context context) {
        this.images = images;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        final View imageLayout = inflater.inflate(R.layout.row_viewpager_images, container, false);

        assert imageLayout != null;
        AdView mAdView = imageLayout.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        container.addView(imageLayout, 0);
        return (imageLayout);


       /* MobileAds.initialize(context, "ca-app-pub-3553456737383858~3003894872");
        final AdLoader.Builder builder = new AdLoader.Builder(context, "ca-app-pub-3553456737383858/1698035148")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        final FrameLayout frameLayout = imageLayout.findViewById(R.id.frameLayout);

                        UnifiedNativeAdView adView = (UnifiedNativeAdView) inflater
                                .inflate(R.layout.native_ads, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);

                    }
                });
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {

                Toast.makeText(context, "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();



        adLoader.loadAd(new AdRequest.Builder().build());
        return (imageLayout);*/


    }


    private void populateUnifiedNativeAdView(UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView adView) {

        TextView headline = adView.findViewById(R.id.tvHeadlines);
        headline.setText(unifiedNativeAd.getHeadline());
        adView.setHeadlineView(headline);

        TextView subHeadline = adView.findViewById(R.id.tvSubHeadlines);
        subHeadline.setText(unifiedNativeAd.getBody());
        adView.setBodyView(subHeadline);

        MediaView mediaView = adView.findViewById(R.id.imgAd);
        adView.setMediaView(mediaView);
        adView.setNativeAd(unifiedNativeAd);

    }


}
