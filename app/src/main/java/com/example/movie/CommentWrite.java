package com.example.movie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.movie.data.MovieList;
import com.example.movie.data.ResponseInfo;
import com.example.movie.ui.home.MovieDetailFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class CommentWrite extends AppCompatActivity {

    RatingBar ratingBar;
    EditText contentsInput;

    MovieDetailFragment movieDetailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);

        movieDetailFragment = new MovieDetailFragment();

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


        textView_title.setText(intent.getStringExtra("title").toString());
        int resID = intent.getIntExtra("resID", 0);
        imageView_grade.setImageResource(resID);

//        if(AppHelper.requestQueue == null){
//            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
//        }
//
//        postRequest();

    }

//    public void postRequest() {
//        //http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=1//
//
//        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=1";
//
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //    processResponse(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("message", "movie readCommentList 성공");
////                params.put("code", "200");
////                params.put("resultType", "list");
////                params.put("totalCount", "4399");
////
////                params.put("id", "7509");
////                params.put("writer", "11111");
////                params.put("movieId", "1");
////                params.put("writer_image", "null");
////                params.put("time", "2020-1-12 1:12:12");
////                params.put("timestamp", "1603386279");
////                params.put("rating", "3.5");
////                params.put("contents", "13333333333333");
////                params.put("recommend", "0");
//
//                params.put("writer", "11111");
//                params.put("contents", "13333333333333");
//
//                return params;
//            }
//        };
//        request.setShouldCache(false);
//        AppHelper.requestQueue.add(request);
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
