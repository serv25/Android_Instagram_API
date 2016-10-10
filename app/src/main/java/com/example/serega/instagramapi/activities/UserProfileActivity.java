package com.example.serega.instagramapi.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.serega.instagramapi.R;

import java.io.InputStream;

public class UserProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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
        ImageView profilePictureImageView = (ImageView) findViewById(R.id.imageView);

        usernameView.setText(username);
        bioView.setText(bio);
        fullNameView.setText(fullName);
        idView.setText(id);
        websiteView.setText(website);

        if (profilePicture != null) {
            new DownloadImageTask(profilePictureImageView)
                    .execute(profilePicture);
        }
    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}