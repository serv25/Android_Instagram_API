package com.example.serega.instagramapi.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.serega.instagramapi.DownLoadImageTask;
import com.example.serega.instagramapi.R;

public class UserProfileActivity extends Activity {

    private ImageView profilePictureImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profilePictureImageView = (ImageView)findViewById(R.id.imageView);

        Intent intent = getIntent();

        String username = "Username: " + intent.getStringExtra("username");
        String bio = "Bio: " + intent.getStringExtra("bio");
        String website = "Website: " + intent.getStringExtra("website");
        String fullName = "Full Name: " + intent.getStringExtra("fullName");
        String id = "ID: " + intent.getStringExtra("id");
        String profilePicture = intent.getStringExtra("profilePicture");

        TextView usernameView = (TextView) findViewById(R.id.username);
        TextView bioView = (TextView) findViewById(R.id.bio);
        TextView fullNameView = (TextView) findViewById(R.id.fullName);
        TextView idView = (TextView) findViewById(R.id.id);
        TextView websiteView = (TextView) findViewById(R.id.website);

        usernameView.setText(username);
        bioView.setText(bio);
        fullNameView.setText(fullName);
        idView.setText(id);
        websiteView.setText(website);

        new DownLoadImageTask(profilePictureImageView).execute(profilePicture);
    }
}
