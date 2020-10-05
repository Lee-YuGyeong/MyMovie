package com.example.movie.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentItem implements Parcelable {

    int resId;
    String id;
    String time;
    String comment;
    String commentLikeCount;
    float rating;



    public CommentItem(int resId, String id, String time, String comment,float rating) {
        this.resId = resId;
        this.id = id;
        this.time = time;
        this.comment = comment;
        this.rating = rating;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

//    public String getCommentLikeCount() {
//        return commentLikeCount;
//    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public CommentItem(Parcel src){

        resId = src.readInt();
        id= src.readString();
        time= src.readString();
        comment= src.readString();
        rating= src.readFloat();

    }


    public void setCommentLikeCount(String commentLikeCount) {
        this.commentLikeCount = commentLikeCount;
    }

    public static final Creator CREATOR = new Creator() {
        public CommentItem createFromParcel(Parcel in) {
            return new CommentItem(in);
        }

        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resId);
        dest.writeString(id);
        dest.writeString(time);
        dest.writeString(comment);
        dest.writeFloat(rating);


    }


}
