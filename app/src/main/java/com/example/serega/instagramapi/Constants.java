package com.example.serega.instagramapi;

public class Constants {

    public static final String API_BASE_URL = "https://api.instagram.com";
    public static final String CLIENT_ID = "f881449d8c8a4270acd6919eee5a33a8";
    public static final String CLIENT_SECRET = "0d01121146544866a079913352037eeb";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String REDIRECT_URI = "http://yourcallback.com/";

    public static final String CONTAINS_URI = REDIRECT_URI + "?code=";
    public static final String POST_URI = API_BASE_URL + "/oauth/authorize/?client_id="
            + CLIENT_ID + "&redirect_uri="
            + REDIRECT_URI + "&response_type=code";

    public static final String HOME_URL = "file:///android_asset/index.html";
    public static final String SIGN_IN_URL = "file:///android_asset/go";
}
