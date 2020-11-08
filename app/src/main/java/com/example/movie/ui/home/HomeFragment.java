package com.example.movie.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.Volley;
import com.example.movie.AppHelper;
import com.example.movie.MainActivity;
import com.example.movie.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    MoviePagerAdapter adapter;

    ViewPager pager;

    MainActivity activity;

    Animation translateUp;
    Animation translateDown;

    View menuContainer;

    boolean isShown = false;


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

        adapter = new MoviePagerAdapter(getChildFragmentManager());
/////////////////////////////////////////////
//        Button button = (Button) root.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "버튼 클릭", Toast.LENGTH_LONG).show();
//            }
//        });

        translateUp = AnimationUtils.loadAnimation(getContext(), R.anim.translate_up);
        translateDown = AnimationUtils.loadAnimation(getContext(), R.anim.translate_down);

        translateUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                menuContainer.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        menuContainer = root.findViewById(R.id.menuContainer);
        Button button = (Button) root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShown) {
                    menuContainer.startAnimation(translateUp);
                }else{
                    menuContainer.setVisibility(View.VISIBLE);
                    menuContainer.startAnimation(translateDown);
                }

                isShown = !isShown;

            }
        });

        ///////////////////////////////////////////////////////////////


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

        pager.setClipToPadding(false);
        pager.setPadding(120, 0, 120, 0);
        pager.setPageMargin(60);
//        pager.setPageTransformer(false, new ViewPager.PageTransformer() {
//            @Override public void transformPage(View page, float position) {
//                if (pager.getCurrentItem() == 0) {
//                    page.setTranslationX(-120);
//                } else if (pager.getCurrentItem() == adapter.getCount() - 1) {
//                    page.setTranslationX(120);
//                } else {
//                    page.setTranslationX(0);
//                }
//            }
//        });//첫번째 마지막 안보이게 하기

        pager.setAdapter(adapter);


        String userId[] = new String[3];

        if(AppHelper.requestQueue==null){
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());
        }



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

