package com.example.movie;

import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.graphics.Rect;
import android.media.MediaRecorder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie.ui.home.MovieDetailFragment;

import java.util.ArrayList;

public class MoviePhotoAdapter extends RecyclerView.Adapter<MoviePhotoAdapter.ViewHolder> {

 //   Context context;

    ArrayList<MoviePhotoItem> items = new ArrayList<MoviePhotoItem>();

    private static Context context;

    public MoviePhotoAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     //   View itemView = inflater.inflate(R.layout.moviephoto_item,parent,false); //화면 꽉차게
        View itemView = inflater.inflate(R.layout.moviephoto_item,null,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePhotoAdapter.ViewHolder holder, int position) {
        MoviePhotoItem item = items.get(position);
        holder.setItem(item);
    }

    public void addItem(MoviePhotoItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<MoviePhotoItem> items) {
        this.items = items;
    }

    public MoviePhotoItem getItem(int position){
        return items.get(position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

        public void setItem(MoviePhotoItem item) {
         //   imageView.setImageResource(item.getImage());
          //  String url = "http://movie.phinf.naver.net/20171010_164/1507615090097Sml1w_JPEG/movie_image.jpg?type=m665_443_2";
          //  url = item.getImage();
            Glide.with(context.getApplicationContext()).load(item.getImage()).into(imageView);
        }

    }




}
