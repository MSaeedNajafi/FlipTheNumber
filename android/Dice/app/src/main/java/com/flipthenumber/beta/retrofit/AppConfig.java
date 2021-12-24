package com.flipthenumber.beta.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class AppConfig {

    //private static String BASE_URL = "http://141.136.36.151/api/";
    private static String BASE_URL = "https://flipthenumber.com/api/";

    public static Retrofit getRetrofit() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .readTimeout(3000, TimeUnit.SECONDS).build();
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

}