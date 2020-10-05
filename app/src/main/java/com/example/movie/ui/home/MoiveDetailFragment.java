package com.example.movie.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie.CommentListActivity;
import com.example.movie.CommentWrite;
import com.example.movie.FragmentCallBack;
import com.example.movie.MainActivity;
import com.example.movie.R;

import java.util.ArrayList;

public class MoiveDetailFragment extends Fragment {



    CommentAdapter adapter;
    ListView listView;

    Button likeButton;
    TextView likeCountView;

    Button dislikeButton;
    TextView dislikeCountView;

    boolean likeState = false;
    int likeCount=15;

    boolean dislikeState = false;
    int dislikeCount=1;

    ArrayList<CommentItem> arrayList;
    ArrayList<CommentItem> arrayList2;

    MainActivity activity;
    CommentWrite commentWriteActivity;

    FragmentCallBack callBack;

    String contents;
    Float rating;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (MainActivity) getActivity();

        if (context instanceof FragmentCallBack) {
            callBack = (FragmentCallBack) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        activity = null;

        if (callBack !=null)
            callBack = null;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_datail,container,false);


        Button button = (Button) rootView.findViewById(R.id.button_write);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(getContext(),CommentWrite.class),101);

            }
        });


        Button lookButton = (Button) rootView.findViewById(R.id.lookButton);

        arrayList = new ArrayList<CommentItem>();
        arrayList2 = new ArrayList<CommentItem>();


        listView = (ListView) rootView.findViewById(R.id.listView);

        adapter = new CommentAdapter();
//        adapter.addItem(new CommentItem(R.drawable.user1,"kym71**","10분전","적당히 재밌다.오랜만에 잠 안오는 영화 봤네요.",2.0f));

        listView.setAdapter(adapter);



        lookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext() , CommentListActivity.class);

                intent.putParcelableArrayListExtra("key", arrayList);

                startActivity(intent);


            }
        });

        likeButton = (Button) rootView.findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likeState){
                    decrLikeCount();
                }
                else{
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
                if(dislikeState){

                    decrDislikeCount();
                }
                else{
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

    class CommentAdapter extends BaseAdapter {
        ArrayList<CommentItem> items = new ArrayList<CommentItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(CommentItem item){
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
            }else{
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

    public void incrLikeCount(){
        likeCount +=1;
        likeCountView.setText(String.valueOf(likeCount));

        likeButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);
    }
    public void decrLikeCount(){
        likeCount -=1;
        likeCountView.setText(String.valueOf(likeCount));

        likeButton.setBackgroundResource(R.drawable.ic_thumb_up);


    }
    public void incrDislikeCount(){
        dislikeCount +=1;
        dislikeCountView.setText(String.valueOf(dislikeCount));

        dislikeButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);

    }
    public  void decrDislikeCount(){
        dislikeCount -=1;
        dislikeCountView.setText(String.valueOf(dislikeCount));

        dislikeButton.setBackgroundResource(R.drawable.ic_thumb_down);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);



        if(requestCode == 101) {
            if( intent != null ) {
                String contents = intent.getStringExtra("contents");
                float rating = intent.getFloatExtra("rating",0.0f);

                adapter.addItem(new CommentItem(R.drawable.user1,"lyg64**","5분전",contents,rating));
                adapter.notifyDataSetChanged();

                arrayList.add(new CommentItem(R.drawable.user1,"lyg64**","5분전",contents,rating));//


//                Intent intent1 = new Intent();
//                intent1.putExtra("contents",contents);
//                intent1.putExtra("rating",rating);
//                setResult(RESULT_OK, intent);
            }
        }



    }



}
