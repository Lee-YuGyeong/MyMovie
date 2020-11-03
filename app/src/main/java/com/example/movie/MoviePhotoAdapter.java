package com.example.movie;

import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.graphics.Rect;
import android.media.MediaRecorder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie.ui.home.MovieDetailFragment;

import java.util.ArrayList;

public class MoviePhotoAdapter extends RecyclerView.Adapter<MoviePhotoAdapter.ViewHolder> {

    ArrayList<MoviePhotoItem> items = new ArrayList<MoviePhotoItem>();

    private static Context context;

    OnItemClickListener listener;

    public static interface OnItemClickListener {
        public void OnItemClick(ViewHolder holder, View view, int position);
    }

    public MoviePhotoAdapter(Context context) {
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
        View itemView = inflater.inflate(R.layout.moviephoto_item, null, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePhotoAdapter.ViewHolder holder, int position) {
        MoviePhotoItem item = items.get(position);
        holder.setItem(item);

        holder.setOnItemClickListener(listener);
    }

    public void addItem(MoviePhotoItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<MoviePhotoItem> items) {
        this.items = items;
    }

    public MoviePhotoItem getItem(int position) {
        return items.get(position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }//클릭 이벤트


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageView playImageView;
        boolean video;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            playImageView = (ImageView) itemView.findViewById(R.id.playImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.OnItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(MoviePhotoItem item) {
            Glide.with(context.getApplicationContext()).load(item.getImage()).into(imageView);

            if(!item.isVideo()) {
                playImageView.setVisibility(View.INVISIBLE);
            }else {
                playImageView.setVisibility(View.VISIBLE);
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }


    }


}
