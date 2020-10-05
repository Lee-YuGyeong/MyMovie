package com.example.movie.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.movie.MainActivity;
import com.example.movie.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;


    Fragment_home_1 fragment_home_1;
    ViewPager pager;

    MainActivity activity;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        pager = (ViewPager) root.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(6);

        MoviePagerAdapter adapter = new MoviePagerAdapter(getChildFragmentManager());


        Fragment_home_1 fragment_home_1 = new Fragment_home_1();
        adapter.addItem(fragment_home_1);

        Fragment_home_2 fragment_home_2 = new Fragment_home_2();
        adapter.addItem(fragment_home_2);

        Fragment_home_3 fragment_home_3 = new Fragment_home_3();
        adapter.addItem(fragment_home_3);

        Fragment_home_4 fragment_home_4 = new Fragment_home_4();
        adapter.addItem(fragment_home_4);

        Fragment_home_5 fragment_home_5 = new Fragment_home_5();
        adapter.addItem(fragment_home_5);

        Fragment_home_6 fragment_home_6 = new Fragment_home_6();
        adapter.addItem(fragment_home_6);


        pager.setAdapter(adapter);

        return root;
    }

    class MoviePagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> items = new ArrayList<Fragment>();


        public MoviePagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }


        public void addItem(Fragment item) {
            items.add(item);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }


    }
}

