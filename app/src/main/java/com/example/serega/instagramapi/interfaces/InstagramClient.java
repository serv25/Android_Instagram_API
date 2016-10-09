package com.example.serega.instagramapi.interfaces;


import com.example.serega.instagramapi.models.TokenRequest;
import com.example.serega.instagramapi.models.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InstagramClient {

    @POST("/oauth/access_token")
    Call<TokenResponse> getTokenAccess(@Body TokenRequest tokenRequest);
}
