package com.example.movie.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.movie.AppHelper;
import com.example.movie.NetworkStatus;
import com.example.movie.R;
import com.example.movie.data.CommentList;
import com.example.movie.data.ResponseInfo;
import com.example.movie.database.CommentDatabase;
import com.example.movie.database.CommentVo;
import com.example.movie.ui.home.CommentItem;
import com.example.movie.ui.home.CommentItemView;
import com.example.movie.ui.home.CommentWrite;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;


public class CommentListActivity extends AppCompatActivity {

    CommentAdapter adapter;
    ListView listView;
    ArrayList<CommentItem> arrayList;
    ArrayList<CommentItem> arrayList2;

    TextView textView_title;
    ImageView imageView_grade;
    RatingBar ratingBar;
    TextView textView_rating;

    String intent_title;
    int resID;

    String key;

    ArrayList<CommentVo> CommentList;

    @Override
    protected void onStart() {
        super.onStart();
        networkStatus();
    }

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
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if (status == NetworkStatus.TYPE_NOT_CONNECTED) {
                    Toast.makeText(getApplicationContext(), "인터넷을 연결해 주세요.", Toast.LENGTH_LONG).show();
                } else {

                    showCommentWriteActivity();
                }
            }
        });


        listView = (ListView) findViewById(R.id.listView);

        adapter = new CommentAdapter();

        listView.setAdapter(adapter);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        intent_title = intent.getStringExtra("title").toString();
        textView_title.setText(intent_title);
        resID = intent.getIntExtra("resID", 0);
        imageView_grade.setImageResource(resID);
        ratingBar.setRating(intent.getFloatExtra("rating", 0f) / 2);

        String totalCount = String.valueOf(intent.getIntExtra("totalCount", 0));

        textView_rating.setText((intent.getFloatExtra("rating", 0f)) + "  (" + currentpoint(totalCount) + "명 참여)");


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

            adapter.items.clear();

            if (adapter.isEmpty()) {

                for (int i = 0; i < commentList.result.size(); i++) {

                    adapter.addItem(new CommentItem(R.drawable.user1, commentList.result.get(i).writer, commentList.result.get(i).time, commentList.result.get(i).contents, commentList.result.get(i).rating));

                }
                adapter.notifyDataSetChanged();
            }

        }
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

        intent.putExtra("key", key);
        intent.putExtra("title", intent_title);
        intent.putExtra("resID", resID);

        startActivityForResult(intent, 101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 101) {
            if (intent != null) {
                networkStatus();
            }
        }

    }

    public void setCommentDatabaseData() {

        CommentList = new ArrayList<CommentVo>();

        CommentList = CommentDatabase.selectCommentList(Integer.parseInt(key));

        for (int i = 0; i < CommentList.size(); i++) {
            adapter.addItem(new CommentItem(R.drawable.user1, CommentList.get(i).getWriter(), CommentList.get(i).getTime(), CommentList.get(i).getContents(), CommentList.get(i).getRating()));
        }
        adapter.notifyDataSetChanged();

    }

    public void networkStatus() {
        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatus.TYPE_MOBILE) {
            requestCommentList();
        } else if (status == NetworkStatus.TYPE_WIFI) {
            requestCommentList();
        } else {
            setCommentDatabaseData();
        }
    }


}

