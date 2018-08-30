package com.stufeed.android.api;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static volatile Retrofit retrofit = null;

    private APIClient() {
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (APIClient.class) {
                // Double check
                if (retrofit == null) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger
                            () {
                        @Override
                        public void log(String message) {
                            Log.e("OkHttp", message);
                        }
                    });
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Api.BASE_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return retrofit;
    }
}