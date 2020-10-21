package com.example.movie;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.movie.data.MovieList;
import com.example.movie.data.ResponseInfo;
import com.example.movie.ui.home.CommentItem;
import com.example.movie.ui.home.CommentItemView;
import com.example.movie.ui.home.MovieDetailFragment;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class CommentListActivity extends AppCompatActivity {

    CommentAdapter adapter;
    ListView listView;
    ArrayList<CommentItem> arrayList;
    CommentItem list;
    Intent intent;
    MovieDetailFragment movieDetailFragment;
    ArrayList<CommentItem> arrayList2;
//    String key;

    TextView textView_title;
    ImageView imageView_grade;
    RatingBar ratingBar;
    TextView textView_rating;

    String intent_title;
    int resID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        arrayList = new ArrayList<CommentItem>();
        arrayList2 = new ArrayList<CommentItem>();
        ArrayList<CommentItem> items = new ArrayList<CommentItem>();

        textView_title = (TextView) findViewById(R.id.textView_title);
        imageView_grade = (ImageView) findViewById(R.id.imageView_grade);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        textView_rating = (TextView) findViewById(R.id.textView_rating);

        Button button = (Button) findViewById(R.id.button_write);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentWriteActivity();
            }
        });


//
        listView = (ListView) findViewById(R.id.listView);
//
//
        adapter = new CommentAdapter();

        Intent intent = getIntent();

        arrayList = intent.getParcelableArrayListExtra("arr");
      //  key = intent.getStringExtra("key");


        for (int i = 0; i < arrayList.size(); i++) {
            adapter.addItem(new CommentItem(arrayList.get(i).getResId(), arrayList.get(i).getId(), arrayList.get(i).getTime(), arrayList.get(i).getComment(), arrayList.get(i).getRating())); //모두보기 데이터 넘겨주기
        }

        listView.setAdapter(adapter);

        intent_title = intent.getStringExtra("title").toString();
        textView_title.setText(intent_title);
        resID = intent.getIntExtra("resID",0);
        imageView_grade.setImageResource(resID);
        ratingBar.setRating(intent.getFloatExtra("rating",0f)/2);

        String totalCount = String.valueOf(intent.getIntExtra("totalCount",0));
        currentpoint(totalCount);
        textView_rating.setText((intent.getFloatExtra("rating",0f)) + "  (" +totalCount + "명 참여)");



    }

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

    public void showCommentWriteActivity() {


        Intent intent = new Intent(getApplicationContext(), CommentWrite.class);

        intent.putExtra("title", intent_title);
        intent.putExtra("resID", resID);

        startActivityForResult(intent, 101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 101) {
            if (intent != null) {
                String contents = intent.getStringExtra("contents");
                float rating = intent.getFloatExtra("rating", 0.0f);

                adapter.addItem(new CommentItem(R.drawable.user1, "lyg64**", "5분전", contents, rating));

                arrayList2.add(new CommentItem(R.drawable.user1, "lyg64**", "5분전", contents, rating));//////

                adapter.notifyDataSetChanged();


            }
        }

    }


}

