package com.example.serega.instagramapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.serega.instagramapi.interfaces.InstagramClient;
import com.example.serega.instagramapi.models.TokenRequest;
import com.example.serega.instagramapi.models.TokenResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {

    private String codeFromUrl;
    private InstagramClient service;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WebView webView = (WebView) findViewById(R.id.web_view);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(Constants.HOME_URL);

        initRetrofit();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.equals(Constants.SIGN_IN_URL)) {
                view.loadUrl(Constants.POST_URI);
                return false;
            }
            if (url.contains(Constants.CONTAINS_URI)) {
                codeFromUrl = url.substring(Constants.CONTAINS_URI.length(), url.length());
                Log.i(">>> Url", url);
                Log.i(">>> Code from url", codeFromUrl);
                executeRequest();
                /* add new page here */
            }

            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private void initRetrofit(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(InstagramClient.class);
    }

    private void executeRequest() {

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClient_id(Constants.CLIENT_ID);
        tokenRequest.setClient_secret(Constants.CLIENT_SECRET);
        tokenRequest.setGrant_type(Constants.GRANT_TYPE);
        tokenRequest.setRedirect_uri(Constants.REDIRECT_URI);
        tokenRequest.setCode(codeFromUrl);

        Call<TokenResponse> tokenResponseCall = service.getTokenAccess(tokenRequest);

        tokenResponseCall.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    TokenResponse tokenResponse = response.body();
                    Log.i(">>> TokenResponse", tokenResponse.toString());
                } else {
                    printErrorLogs(response);
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.i(">>> onFailure", t.getMessage());
            }
        });
    }

    private void printErrorLogs(Response<TokenResponse> response){

        Log.e(">>> Response Code", Integer.toString(response.code()));
        Log.e(">>> Response Success: ", Boolean.toString(response.isSuccessful()));
        Log.e(">>> Response Message: ", response.message());
        Log.i(">>> Code from url", codeFromUrl);

        try {
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            Log.e(">>> Error Body", jObjError.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
