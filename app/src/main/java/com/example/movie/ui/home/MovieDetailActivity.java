package com.example.movie.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.movie.AppHelper;
import com.example.movie.NetworkStatus;
import com.example.movie.R;
import com.example.movie.data.CommentList;
import com.example.movie.data.MovieList;
import com.example.movie.data.ResponseInfo;
import com.example.movie.database.CommentDatabase;
import com.example.movie.database.CommentVo;
import com.example.movie.database.MovieDetailDatabase;
import com.example.movie.database.MovieDetailVo;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class MovieDetailActivity extends AppCompatActivity {

    ArrayList<MovieDetailVo> list;
    ArrayList<CommentVo> CommentList;

    CommentAdapter adapter;
    ListView listView;

    Button likeButton;
    TextView likeCountView;

    Button dislikeButton;
    TextView dislikeCountView;

    boolean likeState = false;
    int likeCount;

    boolean dislikeState = false;
    int dislikeCount;

    ArrayList<CommentItem> arrayList;
    ArrayList<CommentItem> arrayList2;

    ImageView imageView;
    TextView textView_title;
    TextView textView_titleDetail;
    TextView textView_rank;
    TextView textView_score;

    RatingBar ratingBar;
    TextView textView_audience;
    TextView textView_text;
    TextView textView_director;
    TextView textView_actor;
    ImageView imageView_grade;

    String key;
    String intent_title;
    int resID;
    String image;
    float intent_rating;
    int totalCount;
    String photoUrl;
    String videoUrl;
    String UrlArr[];
    int photoUrlCount;
    int videoUrlCount;


    RecyclerView recyclerView;
    MoviePhotoAdapter moviePhotoAdapter;

    @Override
    public void onStart() {
        super.onStart();
        networkStatus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_datail);


        final Intent intent = getIntent();
        key = intent.getStringExtra("key");

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView_grade = (ImageView) findViewById(R.id.imageView_grade);
        textView_title = (TextView) findViewById(R.id.textView_title);
        textView_titleDetail = (TextView) findViewById(R.id.textView_titleDetail);
        textView_rank = (TextView) findViewById(R.id.textView_rank);
        textView_score = (TextView) findViewById(R.id.textView_score);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        textView_audience = (TextView) findViewById(R.id.textView_audience);
        textView_text = (TextView) findViewById(R.id.textView_text);
        textView_director = (TextView) findViewById(R.id.textView_director);
        textView_actor = (TextView) findViewById(R.id.textView_actor);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        moviePhotoAdapter = new MoviePhotoAdapter(getApplicationContext());

        recyclerView.addItemDecoration(new RecyclerViewDecoration(60));
        recyclerView.setAdapter(moviePhotoAdapter);

        moviePhotoAdapter.setOnItemClickListener(new MoviePhotoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(MoviePhotoAdapter.ViewHolder holder, View view, int position) {
                MoviePhotoItem item = moviePhotoAdapter.getItem(position);


                if (position < photoUrlCount) {
                    Intent intent = new Intent(getApplicationContext(), ClickPhotoActivity.class);

                    intent.putExtra("url", UrlArr[position]);
                    startActivity(intent);
                } else {
                    Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlArr[position]));
                    startActivity(youtubeIntent);
                }


            }
        });


        Button button = (Button) findViewById(R.id.button_write);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if(status == NetworkStatus.TYPE_NOT_CONNECTED) {
                    Toast.makeText(getApplicationContext(),"인터넷을 연결해 주세요.",Toast.LENGTH_LONG).show();
                }else {

                    Intent intent = new Intent(getApplicationContext(), CommentWrite.class);

                    intent.putExtra("key", key);
                    intent.putExtra("title", intent_title);
                    intent.putExtra("resID", resID);

                    startActivityForResult(intent, 101);
                }
            }
        });


        Button lookButton = (Button) findViewById(R.id.lookButton);

        arrayList = new ArrayList<CommentItem>();
        arrayList2 = new ArrayList<CommentItem>();


        listView = (ListView) findViewById(R.id.listView);

        adapter = new CommentAdapter();

        listView.setAdapter(adapter);


        lookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), CommentListActivity.class);

                intent.putParcelableArrayListExtra("arr", arrayList);
                intent.putExtra("key", key);
                intent.putExtra("title", intent_title);
                intent.putExtra("resID", resID);
                intent.putExtra("rating", intent_rating);
                intent.putExtra("totalCount", totalCount);


                startActivity(intent);


            }
        });

        likeButton = (Button) findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeState) {
                    decrLikeCount();

                } else {
                    incrLikeCount();

                    if (dislikeState) {
                        decrDislikeCount();
                        dislikeState = !dislikeState;

                    }
                }
                likeState = !likeState;
            }
        });

        dislikeButton = (Button) findViewById(R.id.dislikeButton);
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dislikeState) {
                    decrDislikeCount();

                } else {
                    if (likeState) {
                        decrLikeCount();
                        likeState = !likeState;

                    }
                    incrDislikeCount();

                }
                dislikeState = !dislikeState;
            }
        });

        likeCountView = (TextView) findViewById(R.id.likeCountView);
        dislikeCountView = (TextView) findViewById(R.id.dislikeView);


    }


    public void incrLikeCount() {
        likeCount += 1;
        likeCountView.setText(String.valueOf(likeCount));

        likeButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);

        postRequest("Y","null");
    }

    public void decrLikeCount() {
        likeCount -= 1;
        likeCountView.setText(String.valueOf(likeCount));

        likeButton.setBackgroundResource(R.drawable.ic_thumb_up);

        postRequest("N","null");
    }

    public void incrDislikeCount() {
        dislikeCount += 1;
        dislikeCountView.setText(String.valueOf(dislikeCount));

        dislikeButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);

        postRequest("null","Y");
    }

    public void decrDislikeCount() {
        dislikeCount -= 1;
        dislikeCountView.setText(String.valueOf(dislikeCount));

        dislikeButton.setBackgroundResource(R.drawable.ic_thumb_down);

        postRequest("null","N");
    }


    ///////////////////////////////////////////////////api 시작

    public void postRequest(final String like,final String dislike) {

        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/increaseLikeDisLike";

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

                if(like != "null") {
                    params.put("likeyn", like);
                }
                if(dislike != "null") {
                    params.put("dislikeyn", dislike);
                }
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);


    }


    public void requestCommentList() {

        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readCommentList";
        url += "?" + "id=" + key;

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

            CommentList commentList = gson.fromJson(response, CommentList.class);

            totalCount = info.totalCount;

            adapter.items.clear();

            if (adapter.isEmpty()) {

                for (int i = 0; i < commentList.result.size(); i++) {

                    adapter.addItem(new CommentItem(R.drawable.user1, commentList.result.get(i).writer, commentList.result.get(i).time, commentList.result.get(i).contents, commentList.result.get(i).rating));
                }
                adapter.notifyDataSetChanged();
            }

        }
    }

    public void requestMovieList() {

        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovie";
        url += "?" + "id=" + key;

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

            String url = movieList.result.get(0).thumb;
            Glide.with(getApplicationContext().getApplicationContext()).load(url).into(imageView);

            image = "@drawable/ic_";
            image += movieList.result.get(0).grade;
            String packageName = getPackageName();
            resID = getResources().getIdentifier(image, "drawable", packageName);
            imageView_grade.setImageResource(resID);


            textView_title.setText(movieList.result.get(0).title.toString());
            textView_titleDetail.setText(movieList.result.get(0).date + "\n" + movieList.result.get(0).genre + " / " + movieList.result.get(0).duration + "분");
            likeCountView.setText(String.valueOf(movieList.result.get(0).like));
            dislikeCountView.setText(String.valueOf(movieList.result.get(0).dislike));
            likeCount = movieList.result.get(0).like;
            dislikeCount = movieList.result.get(0).dislike;
            textView_rank.setText(movieList.result.get(0).reservation_grade + "위 " + movieList.result.get(0).reservation_rate + "%");
            textView_score.setText(String.valueOf(movieList.result.get(0).audience_rating));
            ratingBar.setRating(movieList.result.get(0).audience_rating / 2);
            String audience = currentpoint(String.valueOf(movieList.result.get(0).audience));
            textView_audience.setText(audience + "명");
            textView_text.setText(movieList.result.get(0).synopsis);
            textView_director.setText(movieList.result.get(0).director);
            textView_actor.setText(movieList.result.get(0).actor);

            intent_title = movieList.result.get(0).title.toString();
            intent_rating = movieList.result.get(0).audience_rating;

            photoUrl = movieList.result.get(0).photos;
            videoUrl = movieList.result.get(0).videos;

            adapterAddUrl();

        }
    }

    public void adapterAddUrl() {
        String str = photoUrl;
        String str2 = videoUrl;

        if (str != null && str2 != null) {
            StringTokenizer result = new StringTokenizer(str, ",");
            StringTokenizer result2 = new StringTokenizer(str2, ",");

            photoUrlCount = result.countTokens();
            videoUrlCount = result2.countTokens();

            UrlArr = new String[photoUrlCount + videoUrlCount];

            int i = 0;

            while (result.hasMoreTokens()) {
                UrlArr[i] = result.nextToken();
                moviePhotoAdapter.addItem(new MoviePhotoItem(UrlArr[i], false));
                i++;
            }
            while (result2.hasMoreTokens()) {
                UrlArr[i] = result2.nextToken();
                String thumb = UrlArr[i].substring(UrlArr[i].lastIndexOf("/") + 1);
                thumb = "https://img.youtube.com/vi/" + thumb + "/default.jpg";
                moviePhotoAdapter.addItem(new MoviePhotoItem(thumb, true));
                i++;
            }
        } else if (str != null && str2 == null) {
            StringTokenizer result = new StringTokenizer(str, ",");
            photoUrlCount = result.countTokens();

            UrlArr = new String[photoUrlCount];

            int i = 0;

            while (result.hasMoreTokens()) {
                UrlArr[i] = result.nextToken();
                moviePhotoAdapter.addItem(new MoviePhotoItem(UrlArr[i], false));
                i++;
            }

        } else if (str == null && str2 != null) {
            StringTokenizer result2 = new StringTokenizer(str2, ",");

            videoUrlCount = result2.countTokens();

            UrlArr = new String[photoUrlCount + videoUrlCount];

            int i = 0;

            while (result2.hasMoreTokens()) {
                UrlArr[i] = result2.nextToken();
                String thumb = UrlArr[i].substring(UrlArr[i].lastIndexOf("/") + 1);
                thumb = "https://img.youtube.com/vi/" + thumb + "/default.jpg";
                moviePhotoAdapter.addItem(new MoviePhotoItem(thumb, true));
                i++;
            }
        }


    }

///////////////////////////////////////api 끝

    public static String currentpoint(String result) {

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');

        DecimalFormat df = new DecimalFormat("###,###,###,###");
        df.setDecimalFormatSymbols(dfs);

        try {
            double inputNum = Double.parseDouble(result);
            result = df.format(inputNum).toString();
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }

        return result;
    }


    class CommentAdapter extends BaseAdapter {
        ArrayList<CommentItem> items = new ArrayList<CommentItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(CommentItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CommentItemView view = null;
            if (convertView == null) {
                view = new CommentItemView(getApplicationContext());
            } else {
                view = (CommentItemView) convertView;
            }

            CommentItem item = items.get(position);
            view.setId(item.getId());
            view.setComment(item.getComment());
            view.setTime(item.getTime());
            view.setImage(item.getResId());
            view.setRating(item.getRating());

            return view;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 101) {
            if (intent != null) {

                networkStatus();

            }
        }


    }

    /////////////db 시작
    public void networkStatus() {
        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatus.TYPE_MOBILE) {
            requestMovieList();
            requestCommentList();
        } else if (status == NetworkStatus.TYPE_WIFI) {
            requestMovieList();
            requestCommentList();
        } else {
            setDatabaseData();
            setCommentDatabaseData();
        }
    }//네트워크 연결 여부

    public void setDatabaseData() {

        list = new ArrayList<MovieDetailVo>();

        list = MovieDetailDatabase.selectDetailList();

        int i = Integer.parseInt(key) - 1;

        String url = list.get(i).getThumb();
        Glide.with(getApplicationContext().getApplicationContext()).load(url).into(imageView);

        image = "@drawable/ic_";
        image += list.get(i).getGrade();
        String packageName = getPackageName();
        resID = getResources().getIdentifier(image, "drawable", packageName);
        imageView_grade.setImageResource(resID);

        textView_title.setText(list.get(i).getTitle());
        textView_titleDetail.setText(list.get(i).getDateValue() + "\n" + list.get(i).getGenre() + " / " + list.get(i).getDuration() + "분");
        likeCountView.setText(String.valueOf(list.get(i).get_like()));
        dislikeCountView.setText(String.valueOf(list.get(i).getDislike()));
        likeCount = list.get(i).get_like();
        dislikeCount = list.get(i).getDislike();
        textView_rank.setText(list.get(i).getReservation_grade() + "위 " + list.get(i).getReservation_rate() + "%");
        textView_score.setText(String.valueOf(list.get(i).getAudience_rating()));
        ratingBar.setRating(list.get(i).getAudience_rating() / 2);
        String audience = currentpoint(String.valueOf(list.get(i).getAudience()));
        textView_audience.setText(audience + "명");
        textView_text.setText(list.get(i).getSynopsis());
        textView_director.setText(list.get(i).getDirector());
        textView_actor.setText(list.get(i).getActor());

        intent_title = list.get(i).getTitle();
        intent_rating = list.get(i).getAudience_rating();


        photoUrl = list.get(i).getPhotos();
        videoUrl = list.get(i).getVideos();

        adapterAddUrl();


    }

    public void setCommentDatabaseData() {

        CommentList = new ArrayList<CommentVo>();

        CommentList = CommentDatabase.selectCommentList(Integer.parseInt(key));

        for (int i = CommentList.size()-1; i >CommentList.size()-10; i--) {
            adapter.addItem(new CommentItem(R.drawable.user1, CommentList.get(i).getWriter(), CommentList.get(i).getTime(), CommentList.get(i).getContents(), CommentList.get(i).getRating()));
        }
        adapter.notifyDataSetChanged();

        totalCount = CommentList.get(CommentList.size() - 1).getTotalCount();

    }


    ////////////db 끝


}
