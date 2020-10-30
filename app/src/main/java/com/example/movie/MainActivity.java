package com.example.movie;

import android.os.Bundle;


import com.amitshekhar.DebugDB;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movie.data.CommentList;
import com.example.movie.data.MovieList;
import com.example.movie.data.ResponseInfo;
import com.example.movie.database.CommentDatabase;
import com.example.movie.database.MovieDetailDatabase;
import com.example.movie.database.OutlineDatabase;
import com.example.movie.ui.home.Fragment_home_1;
import com.example.movie.ui.home.Fragment_home_2;
import com.example.movie.ui.home.MovieDetailFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    Fragment_home_1 fragment_home_1;
    Fragment_home_2 fragment_home_2;
    MovieDetailFragment movieDetailFragment;
    CommentWrite commentWrite;

    @Override
    protected void onStart() {
        super.onStart();
        networkStatus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragment_home_1 = new Fragment_home_1();
        fragment_home_2 = new Fragment_home_2();
        movieDetailFragment = new MovieDetailFragment();
        commentWrite = new CommentWrite();

        database();

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


        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

    }

    public void requestMovieList() {

        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovieList";
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

    public void processResponse(String response) {
        Gson gson = new Gson();

        ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        if (info.code == 200) {
            MovieList movieList = gson.fromJson(response, MovieList.class);

            for (int i = 0; i < movieList.result.size(); i++) {

                OutlineDatabase.insertOutlineData(movieList.result.get(i).id,
                        movieList.result.get(i).title,
                        movieList.result.get(i).title_eng,
                        movieList.result.get(i).date,
                        movieList.result.get(i).user_rating,
                        movieList.result.get(i).audience_rating,
                        movieList.result.get(i).reviewer_rating,
                        movieList.result.get(i).reservation_rate,
                        movieList.result.get(i).reservation_grade,
                        movieList.result.get(i).grade,
                        movieList.result.get(i).thumb,
                        movieList.result.get(i).image);

            }
        }

    }

    public void urlCount(){

        requestMovieDetailList(1);
        requestMovieDetailList(2);
        requestMovieDetailList(3);
        requestMovieDetailList(4);
        requestMovieDetailList(5);


    }

    public void requestMovieDetailList(int i) {

        // http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=1

        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovie";
        url += "?" + "id=" + String.valueOf(i);



        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse2(response);
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

    public void processResponse2(String response) {
        Gson gson = new Gson();

        ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        if (info.code == 200) {
            MovieList movieList = gson.fromJson(response, MovieList.class);

            MovieDetailDatabase.insertDetailData(movieList.result.get(0).id,
                    movieList.result.get(0).title,
                    movieList.result.get(0).date,
                    movieList.result.get(0).user_rating,
                    movieList.result.get(0).audience_rating,
                    movieList.result.get(0).reviewer_rating,
                    movieList.result.get(0).reservation_rate,
                    movieList.result.get(0).reservation_grade,
                    movieList.result.get(0).grade,
                    movieList.result.get(0).thumb,
                    movieList.result.get(0).image,
                    movieList.result.get(0).photos,
                    movieList.result.get(0).videos,
                    movieList.result.get(0).outlinks,
                    movieList.result.get(0).genre,
                    movieList.result.get(0).duration,
                    movieList.result.get(0).audience,
                    movieList.result.get(0).synopsis,
                    movieList.result.get(0).director,
                    movieList.result.get(0).actor,
                    movieList.result.get(0).like,
                    movieList.result.get(0).dislike);

        }
    }

    public void commentCrlCount(){

        requestComment(1);
        requestComment(2);
        requestComment(3);
        requestComment(4);
        requestComment(5);


    }

    public void requestComment(int i) {

        //http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=1

        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readCommentList";
        url += "?" + "id=" + i;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse3(response);
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

    public void processResponse3(String response) {
        Gson gson = new Gson();

        ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        if (info.code == 200) {
            CommentList commentList = gson.fromJson(response, CommentList.class);

            for (int i = 0; i < commentList.result.size(); i++) {
                CommentDatabase.insertCommentData(commentList.result.get(i).id,
                        commentList.result.get(i).writer,
                        commentList.result.get(i).movieId,
                        commentList.result.get(i).writer_image,
                        commentList.result.get(i).time,
                        commentList.result.get(i).timestamp,
                        commentList.result.get(i).rating,
                        commentList.result.get(i).contents,
                        commentList.result.get(i).recommend,
                        info.totalCount);


            }
        }

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

    public void onFragmentChanged(int index, String key) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, movieDetailFragment).addToBackStack(null).commit();

            Bundle bundle = new Bundle();
            bundle.putString("key", key);
            movieDetailFragment.setArguments(bundle);

        }
    }

    public void networkStatus() {
        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatus.TYPE_MOBILE) {
            requestMovieList();
            urlCount();
            commentCrlCount();
            Toast.makeText(getApplicationContext(), "인터넷이 연결되어 있습니다. 데이터베이스에 저장함.", Toast.LENGTH_LONG).show();
        } else if (status == NetworkStatus.TYPE_WIFI) {
            requestMovieList();
            urlCount();
            commentCrlCount();
            Toast.makeText(getApplicationContext(), "인터넷이 연결되어 있습니다. 데이터베이스에 저장함.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "인터넷이 연결되어 있지 않습니다. 데이터베이스로부터 로딩함.", Toast.LENGTH_LONG).show();
        }
    }//네트워크 연결 여부


    public void database() {
        DebugDB.getAddressLog(); //log http 검색

        OutlineDatabase.openDatabase(getApplicationContext(), "movie");
        OutlineDatabase.createTable("outline"); //메모리 만들어지면서 자동

        MovieDetailDatabase.openDatabase(getApplicationContext(), "movie");
        MovieDetailDatabase.createTable("detail"); //메모리 만들어지면서 자동

        CommentDatabase.openDatabase(getApplicationContext(), "movie");
        CommentDatabase.createTable("comment");

    }

}
