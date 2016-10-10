package com.example.serega.instagramapi.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.serega.instagramapi.Constants;
import com.example.serega.instagramapi.R;
import com.example.serega.instagramapi.models.TokenResponse;
import com.example.serega.instagramapi.models.User;
import com.example.serega.instagramapi.services.InstagramService;

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
    private InstagramService service;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(InstagramService.class);
    }

    private void executeRequest() {

        Call<TokenResponse> tokenResponseCall = service.getTokenAccess(
                Constants.CLIENT_ID,
                Constants.CLIENT_SECRET,
                Constants.GRANT_TYPE,
                Constants.REDIRECT_URI,
                codeFromUrl
        );

        tokenResponseCall.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    TokenResponse tokenResponse = response.body();
                    User user =  tokenResponse.getUser();

                    Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                    intent.putExtra("username", user.getUsername());
                    intent.putExtra("bio", user.getBio());
                    intent.putExtra("website", user.getWebsite());
                    intent.putExtra("profilePicture", user.getProfilePicture());
                    intent.putExtra("fullName", user.getFullName());
                    intent.putExtra("id", user.getId());
                    startActivity(intent);

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
