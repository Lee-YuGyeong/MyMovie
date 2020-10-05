package com.example.movie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.movie.ui.home.MoiveDetailFragment;

public class CommentWrite extends AppCompatActivity {

    RatingBar ratingBar;
    EditText contentsInput;

    MoiveDetailFragment moiveDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);

        moiveDetailFragment = new MoiveDetailFragment();

        ratingBar = (RatingBar) findViewById(R.id.ratingComment);
        contentsInput = (EditText) findViewById(R.id.contentsInput);

        Button saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMain();

            }
        });

//        final Intent intent = getIntent();
//        processIntent(intent);
//
        Button backButton = (Button) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }

//    private void processIntent(Intent intent) {
//        if (intent != null) {
//            float rating = intent.getFloatExtra("rating", 0.0f);
//            ratingBar.setRating(rating);
//
//        }
//    }



    public void returnToMain() {

        String contents = contentsInput.getText().toString();
        Float rating = ratingBar.getRating();


        Intent intent = new Intent();
        intent.putExtra("contents", contents);
        intent.putExtra("rating", rating);
        setResult(RESULT_OK, intent);



        finish();


    }



}
