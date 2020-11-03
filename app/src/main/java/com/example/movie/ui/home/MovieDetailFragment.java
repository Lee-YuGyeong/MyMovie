package com.example.movie.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.movie.AppHelper;
import com.example.movie.CommentListActivity;
import com.example.movie.CommentWrite;
import com.example.movie.MainActivity;
import com.example.movie.MoviePhotoAdapter;
import com.example.movie.MoviePhotoItem;
import com.example.movie.database.CommentDatabase;
import com.example.movie.database.CommentVo;
import com.example.movie.database.MovieDetailDatabase;
import com.example.movie.database.MovieDetailVo;
import com.example.movie.NetworkStatus;
import com.example.movie.R;
import com.example.movie.data.CommentList;
import com.example.movie.data.MovieList;
import com.example.movie.data.ResponseInfo;
import com.example.movie.ui.RecyclerViewDecoration;
import com.google.gson.Gson;

import org.w3c.dom.Comment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class MovieDetailFragment extends Fragment {

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

    MainActivity activity;
    CommentWrite commentWriteActivity;

    String contents;
    Float rating;

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

    RecyclerView recyclerView;
    MoviePhotoAdapter moviePhotoAdapter;

    @Override
    public void onStart() {
        super.onStart();
        networkStatus();
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

    //https://www.youtube.com/watch?v=VJAPZ9cIbs0&feature=youtu.be
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_datail, container, false);


        key = getArguments().getString("key");

        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        imageView_grade = (ImageView) rootView.findViewById(R.id.imageView_grade);
        textView_title = (TextView) rootView.findViewById(R.id.textView_title);
        textView_titleDetail = (TextView) rootView.findViewById(R.id.textView_titleDetail);
        textView_rank = (TextView) rootView.findViewById(R.id.textView_rank);
        textView_score = (TextView) rootView.findViewById(R.id.textView_score);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingbar);
        textView_audience = (TextView) rootView.findViewById(R.id.textView_audience);
        textView_text = (TextView) rootView.findViewById(R.id.textView_text);
        textView_director = (TextView) rootView.findViewById(R.id.textView_director);
        textView_actor = (TextView) rootView.findViewById(R.id.textView_actor);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        moviePhotoAdapter = new MoviePhotoAdapter(getContext());


        recyclerView.addItemDecoration(new RecyclerViewDecoration(60));
        recyclerView.setAdapter(moviePhotoAdapter);

        Button button = (Button) rootView.findViewById(R.id.button_write);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), CommentWrite.class);

                intent.putExtra("title", intent_title);
                intent.putExtra("resID", resID);

                startActivityForResult(intent, 101);

            }
        });


        Button lookButton = (Button) rootView.findViewById(R.id.lookButton);

        arrayList = new ArrayList<CommentItem>();
        arrayList2 = new ArrayList<CommentItem>();


        listView = (ListView) rootView.findViewById(R.id.listView);

        adapter = new CommentAdapter();
        //   adapter.addItem(new CommentItem(R.drawable.user1, "kym71**", "10분전", "적당히 재밌다.오랜만에 잠 안오는 영화 봤네요.", 2.0f));

        listView.setAdapter(adapter);


        lookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), CommentListActivity.class);

                intent.putParcelableArrayListExtra("arr", arrayList);
                //   intent.putExtra("key", key);
                intent.putExtra("title", intent_title);
                intent.putExtra("resID", resID);
                intent.putExtra("rating", intent_rating);
                intent.putExtra("totalCount", totalCount);


                startActivity(intent);


            }
        });

        likeButton = (Button) rootView.findViewById(R.id.likeButton);
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

        dislikeButton = (Button) rootView.findViewById(R.id.dislikeButton);
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

        likeCountView = (TextView) rootView.findViewById(R.id.likeCountView);
        dislikeCountView = (TextView) rootView.findViewById(R.id.dislikeView);


        return rootView;
    }


    ///////////////////////////////////////////////////api 시작

    public void requestCommentList() {
        //  String key = getArguments().getString("key");
        //http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=1

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

            if (adapter.isEmpty()) {
                for (int i = 0; i < commentList.result.size(); i++) {

                    adapter.addItem(new CommentItem(R.drawable.user1, commentList.result.get(i).writer, commentList.result.get(i).time, commentList.result.get(i).contents, commentList.result.get(i).rating));
                    arrayList.add(new CommentItem(R.drawable.user1, commentList.result.get(i).writer, commentList.result.get(i).time, commentList.result.get(i).contents, commentList.result.get(i).rating));
                }
                adapter.notifyDataSetChanged();
            }

        }
    }

    public void requestMovieList() {

        //http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=1//


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
            Glide.with(getActivity().getApplicationContext()).load(url).into(imageView);

            image = "@drawable/ic_";
            image += movieList.result.get(0).grade;
            String packageName = getContext().getPackageName();
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
            adapterAddUrl();

        }
    }

    public void adapterAddUrl() {
        String str = photoUrl;

        if (str != null) {
            while (true) {
                String result = str.substring(str.lastIndexOf(",") + 1);

                moviePhotoAdapter.addItem(new MoviePhotoItem(result));

                if ((str.length() - result.length() - 1) <= 0) break;
                str = str.substring(0, str.length() - result.length() - 1);

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
                view = new CommentItemView(getContext());
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

    public void incrLikeCount() {
        likeCount += 1;
        likeCountView.setText(String.valueOf(likeCount));

        likeButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);
    }

    public void decrLikeCount() {
        likeCount -= 1;
        likeCountView.setText(String.valueOf(likeCount));

        likeButton.setBackgroundResource(R.drawable.ic_thumb_up);


    }

    public void incrDislikeCount() {
        dislikeCount += 1;
        dislikeCountView.setText(String.valueOf(dislikeCount));

        dislikeButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);

    }

    public void decrDislikeCount() {
        dislikeCount -= 1;
        dislikeCountView.setText(String.valueOf(dislikeCount));

        dislikeButton.setBackgroundResource(R.drawable.ic_thumb_down);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (requestCode == 101) {
            if (intent != null) {


                String contents = intent.getStringExtra("contents");
                float rating = intent.getFloatExtra("rating", 0.0f);

                adapter.addItem(new CommentItem(R.drawable.user1, "lyg64**", "5분전", contents, rating));
                adapter.notifyDataSetChanged();

                arrayList.add(new CommentItem(R.drawable.user1, "lyg64**", "5분전", contents, rating));//


            }
        }


    }

    /////////////db 시작
    public void networkStatus() {
        int status = NetworkStatus.getConnectivityStatus(getContext());
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
        Glide.with(getActivity().getApplicationContext()).load(url).into(imageView);

        image = "@drawable/ic_";
        image += list.get(i).getGrade();
        String packageName = getContext().getPackageName();
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


    }

    public void setCommentDatabaseData() {


        CommentList = new ArrayList<CommentVo>();

        CommentList = CommentDatabase.selectCommentList(Integer.parseInt(key));

        for (int i = 0; i < CommentList.size(); i++) {
            adapter.addItem(new CommentItem(R.drawable.user1, CommentList.get(i).getWriter(), CommentList.get(i).getTime(), CommentList.get(i).getContents(), CommentList.get(i).getRating()));
            arrayList.add(new CommentItem(R.drawable.user1, CommentList.get(i).getWriter(), CommentList.get(i).getTime(), CommentList.get(i).getContents(), CommentList.get(i).getRating()));
        }
        adapter.notifyDataSetChanged();

        totalCount = CommentList.get(CommentList.size() - 1).getTotalCount();

    }


    ////////////db 끝

}
