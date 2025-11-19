package com.example.proto4.api;

import com.example.proto4.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/auth/register")
    Call<String> registerUser(@Body User user);

    @POST("api/auth/login")
    Call<String> loginUser(@Body User user);
}
