package com.example.movie;

import android.widget.ImageView;

public class MoviePhotoItem {

    String image;
    int playImage;
    boolean video;

    public MoviePhotoItem(String image,boolean video) {
        this.image = image;
        this.video = video;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPlayImage() {
        return playImage;
    }

    public void setPlayImage(int playImage) {
        this.playImage = playImage;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }
}
