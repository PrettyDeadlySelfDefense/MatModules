package com.elisium.halo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.elisium.halo.R;
import com.elisium.halo.Utilities.DefaultSettings;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.OnBackPressedCallback;

public class HaloFragment extends Fragment {
    public static final String IMG_COUNT = "total_images";
    public static final String IMG_LIST = "images";
    ImagesAdapter imagesAdapter;
    ViewPager2 viewPager;
    int imgs = 0;
    ArrayList<String> imgList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_halo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imagesAdapter = new ImagesAdapter(this);
        Bundle args = getArguments();
        if (args != null) {
            imgs = args.getInt(IMG_COUNT);
            imgList.addAll(args.getStringArrayList(IMG_LIST));
        }
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(imagesAdapter);

        getActivity().getOnBackPressedDispatcher()
                .addCallback(
                        getViewLifecycleOwner(),
                        new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                List<String> haloTopics = DefaultSettings.getInstance(getContext())
                        .getHaloTopics();
                ArrayList<String> tempArray = new ArrayList<>(haloTopics);
                HaloMain haloMain = new HaloMain();
                Bundle arg = new Bundle();
                arg.putStringArrayList(
                        HaloMain.CARDS_LIST,
                        tempArray
                );
                haloMain.setArguments(arg);
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out,
                                android.R.anim.slide_out_right,
                                android.R.anim.slide_in_left
                        )
                        .replace(
                                R.id.halo_container,
                                haloMain,
                                "HaloMain"
                        )
                        .commit();
            }
        });
    }

    public class ImagesAdapter extends FragmentStateAdapter {
        public ImagesAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment imgFragment = new HaloCardFragment();
            Bundle args = new Bundle();
            args.putString(HaloCardFragment.IMG_PATH, imgList.get(position));
            imgFragment.setArguments(args);
            return imgFragment;
        }

        @Override
        public int getItemCount() {
            return imgs;
        }
    }
}


