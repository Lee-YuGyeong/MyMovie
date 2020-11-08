package com.example.movie.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.movie.AppHelper;
import com.example.movie.R;

import java.util.HashMap;
import java.util.Map;


public class CommentWrite extends AppCompatActivity {

    RatingBar ratingBar;
    EditText contentsInput;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);


        ratingBar = (RatingBar) findViewById(R.id.ratingComment);
        contentsInput = (EditText) findViewById(R.id.contentsInput);

        Button saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMain();
            }
        });

        Button backButton = (Button) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();



        TextView textView_title = (TextView) findViewById(R.id.textView_title);
        ImageView imageView_grade = (ImageView) findViewById(R.id.imageView_grade);

        key = intent.getStringExtra("key");
        textView_title.setText(intent.getStringExtra("title").toString());
        int resID = intent.getIntExtra("resID", 0);
        imageView_grade.setImageResource(resID);

    }


    public void returnToMain() {

        String contents = contentsInput.getText().toString();
        Float rating = ratingBar.getRating();

        Intent intent = new Intent();
        intent.putExtra("contents", contents);
        intent.putExtra("rating", rating);
        setResult(RESULT_OK, intent);

        postRequest(contents, rating);

        finish();


    }

    public void postRequest(final String contents, final float rating) {

        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/createComment";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("id", key);
                params.put("rating", String.valueOf(rating));
                params.put("writer", "test");
                params.put("contents", contents);

                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);


    }


}
