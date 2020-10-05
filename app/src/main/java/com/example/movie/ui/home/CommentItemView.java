package com.example.movie.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.movie.R;

public class CommentItemView extends LinearLayout {


    ImageView imageView;
    TextView textView_id;
    TextView textView_time;
    TextView textView_comment;
    RatingBar subRatingBar;

    public CommentItemView(Context context) {
        super(context);
        init(context);
    }

    public CommentItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.comment_item, this,true);

        imageView = (ImageView) findViewById(R.id.imageView);
        textView_id = (TextView) findViewById(R.id.textView_id);
        textView_time = (TextView) findViewById(R.id.textView_time);
        textView_comment = (TextView) findViewById(R.id.textView_comment);
        subRatingBar = (RatingBar) findViewById(R.id.subRatingBar);


    }

    public void setId(String Id){
        textView_id.setText(Id);
    }
    public void setTime(String time){
        textView_time.setText(time);
    }
    public void setComment(String comment){
        textView_comment.setText(comment);
    }
    public void setImage(int resId){
        imageView.setImageResource(resId);
    }
    public void setRating(float rating){ subRatingBar.setRating(rating); }
}
