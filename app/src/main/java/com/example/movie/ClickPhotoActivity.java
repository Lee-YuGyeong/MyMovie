package com.example.movie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class ClickPhotoActivity extends AppCompatActivity {

    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_photo);

        photoView = (PhotoView) findViewById(R.id.photoView);

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");

        Glide.with(getApplicationContext().getApplicationContext()).load(url).into(photoView);


    }
}
