package com.example.movie;

import android.os.Bundle;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movie.data.MovieInfo;
import com.example.movie.data.MovieList;
import com.example.movie.data.ResponseInfo;
import com.example.movie.ui.home.Fragment_home_1;
import com.example.movie.ui.home.Fragment_home_2;
import com.example.movie.ui.home.HomeFragment;
import com.example.movie.ui.home.MovieDetailFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;


   // TextView textView;


    Fragment_home_1 fragment_home_1;
    Fragment_home_2 fragment_home_2;
    MovieDetailFragment movieDetailFragment;
    CommentWrite commentWrite;

    String[] movieTitle = {"1zz","2ee","3ff"};
    HomeFragment homeFragment;

    @Override
    protected void onStart() {
        super.onStart();
        requestMovieList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //    textView = (TextView) findViewById(R.id.textView);

        fragment_home_1 = new Fragment_home_1();
        fragment_home_2 = new Fragment_home_2();
        movieDetailFragment = new MovieDetailFragment();
        commentWrite = new CommentWrite();

//        movieTitle = new String[6];
//        homeFragment = new HomeFragment();
//
//
//        Bundle bundle = new Bundle();
//        bundle.putStringArray("state",movieTitle);
//        homeFragment.setArguments(bundle);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.container);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        if(AppHelper.requestQueue==null){
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

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
                        println("응답 받음 -> " +response);

                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 발생 ->" +error.getMessage());
                    }
                }
        );
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        println("영화목록 요청 보냄.");
    }

    public void processResponse(String response){
        Gson gson = new Gson();

        ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        if(info.code == 200) {
            MovieList movieList = gson.fromJson(response,MovieList.class);
            println("영화 갯수 : " + movieList.result.size());

            for(int i=0;i<movieList.result.size();i++){
                MovieInfo movieInfo = movieList.result.get(i);
                println("영화 #" + i + ":" + movieInfo.id + "," + movieInfo.title + "," + movieInfo.grade);
          //      movieTitle[i] = movieInfo.title;

            }

        }

    }

    public void println(String data){
      //  textView.append(data + "\n");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.container);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void onFragmentChanged(int index,String key){
        if (index == 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, movieDetailFragment).addToBackStack(null).commit();

            Bundle bundle = new Bundle();
            bundle.putString("key",key);
            movieDetailFragment.setArguments(bundle);

        }
    }




}
