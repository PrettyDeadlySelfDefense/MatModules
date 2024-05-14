package com.elisium.halo.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elisium.halo.DTOs.DataItem;
import com.elisium.halo.DTOs.HaloResponse;
import com.elisium.halo.DTOs.ImagesItem;
import com.elisium.halo.R;
import com.elisium.halo.Utilities.DefaultSettings;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class HaloCardFragment extends Fragment {
    private Context mContex;
    public static final String IMG_PATH = "path";
    public ViewGroup mainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_halo_card, container, false);
        return mainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        TextView topText = mainView.findViewById(R.id.card_text_top);
        TextView bottomText = mainView.findViewById(R.id.card_text_bottom);
        ImageView card = mainView.findViewById(R.id.imageView2);
        HaloResponse cards = DefaultSettings.getInstance(mContex).getHaloCards();
        if (args == null) {
            //TODO: handle the absence of IMG_PATH
        } else {
            File imgFile = new File(Objects.requireNonNull(args.getString(IMG_PATH)));
            for (DataItem c : cards.getData()) {
                for (ImagesItem imago : c.getImages()) {
                    String imag = imago.getUrl();
                    String img = imgFile.getName();

                    int count = 0;
                    for(int i=0; i < img.length(); i++)
                    {    if(img.charAt(i) == '_')
                        count++;
                    }
                    if (count == 2) {
                        img = img.substring(
                                0,
                                img.lastIndexOf('_')
                        );
                    }

                    if (imag.contains(img)) {
                        topText.setText(imago.getDescription());
                        bottomText.setText(imago.getDescription());
                    }
                }
            }
            if (imgFile.exists()) {
                Bitmap bit = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                card.setImageBitmap(bit);
                card.setOnClickListener(v -> {
                    if (topText.getVisibility() == View.GONE) {
                        topText.setVisibility(View.VISIBLE);
                        bottomText.setVisibility(View.GONE);
                    } else if (bottomText.getVisibility() == View.GONE) {
                        bottomText.setVisibility(View.VISIBLE);
                        topText.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContex = context;
    }
}
