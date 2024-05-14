package com.elisium.halo.API;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HaloAPI {
    static final String HALO_URL = "https://halo-api-dev.onrender.com";
    static final String HALO_TOKEN = "92f36238-965e-420a-b649-099f3d116f07";
    private static Retrofit halo;

    private static Retrofit halo() {
        if (halo == null) {
            HttpLoggingInterceptor httpLogger = new HttpLoggingInterceptor();
            httpLogger.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("X-Auth-Token", HALO_TOKEN)
                            .build();
                    return chain.proceed(newRequest);
                }
            }).addInterceptor(httpLogger).build();

            halo = new Retrofit.Builder()
                    .baseUrl(HALO_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return halo;
    }

    public static APIRequests prepHalo() {
        return HaloAPI.halo().create(APIRequests.class);
    }
}