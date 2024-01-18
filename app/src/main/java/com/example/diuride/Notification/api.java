package com.example.diuride.Notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class api {
    private static Retrofit retrofit = null;
    public static final String BASE_URL = "https://fcm.googleapis.com";

    public static Retrofit getRetrofitClientFCM() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


        }

        return retrofit;
    }
}
