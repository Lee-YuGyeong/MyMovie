package com.example.movie.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.movie.AppHelper;
import com.example.movie.MainActivity;
import com.example.movie.R;
import com.example.movie.data.MovieList;
import com.example.movie.data.ResponseInfo;
import com.google.gson.Gson;

public class Fragment_home_2 extends Fragment {

    TextView textView;
    TextView textView2;
    ImageView imageView;

    MainActivity activity;

    String movieTitle;

    @Override
    public void onStart() {
        super.onStart();
        requestMovieList();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home_1, container,false);


        textView = (TextView) rootView.findViewById(R.id.textView);
        textView2 = (TextView) rootView.findViewById(R.id.textView2);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);

        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.onFragmentChanged(0,"2");
            }
        });

        return rootView;
    }

    public void requestMovieList(){

        String url = "http://"+AppHelper.host + ":" + AppHelper.port + "/movie/readMovieList";
        url += "?" + "type=1";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponse(String response){
        Gson gson = new Gson();

        ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        if(info.code == 200) {

            MovieList movieList = gson.fromJson(response,MovieList.class);

            String url = movieList.result.get(1).image;

            Glide.with(activity).load(url).into(imageView);

            textView.setText("2. " + movieList.result.get(1).title.toString());
            textView2.setText("예매율  " + movieList.result.get(1).reservation_rate + "% | " + movieList.result.get(1).grade + "세 관람가 | " + movieList.result.get(1).date + " 개봉");

        }
    }



}



