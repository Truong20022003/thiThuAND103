package com.example.testthi1;

import static com.example.testthi1.ApiService.DOMAIN;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private ApiService requestInterface;

    public HttpRequest() {
        requestInterface = new Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
    }
    public ApiService callAPI() {
        return requestInterface;
    }
}
