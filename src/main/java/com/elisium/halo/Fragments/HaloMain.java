package com.elisium.halo.Fragments;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elisium.halo.ConnectionUtilities.CardObject;
import com.elisium.halo.ConnectionUtilities.CardsAdapter;
import com.elisium.halo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

//TODO: implement the cards adapter as taking the titles as data imported into the fragment as args
public class HaloMain extends Fragment implements CardsAdapter.MenuListener {
    public static final String CARDS_LIST = "cards_title_list";
    public static final String IMAGES = "images";
    private View haloMainView;
    private Context mContext;
    private Activity mActivity;
    private ArrayList<String> cardsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        haloMainView = inflater.inflate(R.layout.fragment_halo_main, container, false);
        return haloMainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        if (args != null
                && !Objects.requireNonNull(
                        args.getStringArrayList(CARDS_LIST))
                .isEmpty()) {
            cardsList = args.getStringArrayList(CARDS_LIST);
            RecyclerView cL = haloMainView.findViewById(R.id.CardsList);
            assert cardsList != null;
            CardObject cards[] = new CardObject[cardsList.size()];

            for (int i = 0; i < cardsList.size(); i++) {
                CardObject card = new CardObject(mContext);
                card.title = cardsList.get(i);
                cards[i] = card;
            }

            CardsAdapter cardsAdapter = new CardsAdapter(mContext, cards);
            cardsAdapter.addCustomEventListener(this);
            cL.setAdapter(cardsAdapter);

            
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (Activity) context;

    }


    @Override
    public void buttonPushed(String usersChoice) {
        //TODO: initialize fragment passing the right title and set of cards
        File dir = new File(requireActivity().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES),
                usersChoice
        );
        ArrayList<String> listOfCards = new ArrayList<>();
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile())
                listOfCards.add(file.getPath());
        }
        HaloFragment fCard = new HaloFragment();
        Bundle arg = new Bundle();
        arg.putStringArrayList(HaloFragment.IMG_LIST, listOfCards);
        arg.putInt(HaloFragment.IMG_COUNT, listOfCards.size());
        fCard.setArguments(arg);
        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.halo_container, fCard, usersChoice)
                .commit();
    }
}
