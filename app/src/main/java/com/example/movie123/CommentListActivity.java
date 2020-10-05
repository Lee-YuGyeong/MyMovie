package com.example.movie123;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movie123.ui.home.CommentItem;
import com.example.movie123.ui.home.CommentItemView;
import com.example.movie123.ui.home.MoiveDetailFragment;

import java.util.ArrayList;

public class CommentListActivity extends AppCompatActivity {

    CommentAdapter adapter;
    ListView listView;
    ArrayList<CommentItem> arrayList;
    CommentItem list;
    Intent intent;
    MoiveDetailFragment moiveDetailFragment;
    ArrayList<CommentItem> arrayList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        arrayList = new ArrayList<CommentItem>();
        arrayList2 = new ArrayList<CommentItem>();
        ArrayList<CommentItem> items = new ArrayList<CommentItem>();



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

        arrayList = intent.getParcelableArrayListExtra("key");

        for (int i = 0; i < arrayList.size(); i++) {
                  adapter.addItem(new CommentItem(arrayList.get(i).getResId(), arrayList.get(i).getId(), arrayList.get(i).getTime(), arrayList.get(i).getComment(), arrayList.get(i).getRating())); //모두보기 데이터 넘겨주기
        }

        listView.setAdapter(adapter);

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

                arrayList2.add(new CommentItem(R.drawable.user1,"lyg64**","5분전",contents,rating));//////

                adapter.notifyDataSetChanged();


            }
        }

    }


}

