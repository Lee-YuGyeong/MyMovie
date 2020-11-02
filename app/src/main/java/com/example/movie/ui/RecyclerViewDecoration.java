package com.example.movie.ui;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

    private final int divWidth;

    public RecyclerViewDecoration(int divWidth)
    {
        this.divWidth = divWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = divWidth;
    }
}