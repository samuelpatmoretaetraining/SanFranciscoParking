package com.muelpatmore.sanfranciscoparking.data.network.networkutils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Samuel on 01/12/2017.
 */

public class ServerConnection {
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;

    public static RequestInterface getParkingConnection() {

        okHttpClient= new OkHttpClient.Builder().
                addInterceptor(new HttpLoggingInterceptor()).build();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Network_Constants.PARKING_BASE_URL)
                .client(okHttpClient)
                .build();

        return retrofit.create(RequestInterface.class);    }
}
