package com.example.movie.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.movie.database.MovieDetailDatabase;
import com.example.movie.database.MovieDetailVo;
import com.example.movie.MainActivity;
import com.example.movie.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Fragment_home_1 extends Fragment {

    TextView textView;
    TextView textView2;
    ImageView imageView;

    MainActivity activity;

    ArrayList<MovieDetailVo> arrayList;

    int id;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home_1, container, false);


        textView = (TextView) rootView.findViewById(R.id.textView);
        textView2 = (TextView) rootView.findViewById(R.id.textView2);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);


        arrayList = new ArrayList<MovieDetailVo>();

        setDatabaseData();

        textView.setText("1. " + arrayList.get(0).getTitle());
        textView2.setText("예매율  " + arrayList.get(0).getReservation_rate() + "% | " + arrayList.get(0).getGrade() + "세 관람가 | " + arrayList.get(0).getDateValue() + " 개봉");

        String url = arrayList.get(0).getImage();
        Glide.with(getActivity()).load(url).into(imageView);

        id = arrayList.get(0).getId();


        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // activity.onFragmentChanged(0, "1");
                Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                intent.putExtra("key", String.valueOf(id));
                startActivity(intent);
            }
        });

        return rootView;
    }


    public void setDatabaseData() {

        String sort = getArguments().getString("sort");
        sortingMovie(sort);
    }

    public void sortingMovie(String sort) {

        if (sort == "reservation_rate") {

            arrayList = MovieDetailDatabase.selectDetailList();


        } else if (sort == "audience_rating") {

            arrayList = MovieDetailDatabase.selectDetailList();

            Collections.sort(arrayList, new Comparator<MovieDetailVo>() {
                @Override
                public int compare(MovieDetailVo o1, MovieDetailVo o2) {
                    if (o1.getAudience_rating() < o2.getAudience_rating()) {
                        return -1;
                    } else if (o1.getAudience_rating() == o2.getAudience_rating()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });

        } else {

            arrayList = MovieDetailDatabase.selectDetailList();

            Collections.sort(arrayList, new Comparator<MovieDetailVo>() {
                @Override
                public int compare(MovieDetailVo o1, MovieDetailVo o2) {
                    return o1.getDateValue().compareTo(o2.getDateValue());
                }
            });

        }
    }


}



