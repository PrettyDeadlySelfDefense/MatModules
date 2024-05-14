package com.elisium.halo.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.elisium.halo.DTOs.HaloResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DefaultSettings {

    private static final String PREFS_NAME = "com.elisium.halo.DefaultSettings";
    private static final String HALO_TOPICS_GROUPS = "halo_topics";
    private static final String HALO_CARDS_SET = "halo_cards_set";
    
    private SharedPreferences saves;
    private static DefaultSettings settings;


    private DefaultSettings(Context c) {
        saves = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static DefaultSettings getInstance(Context context) {
        if (settings == null)
            settings = new DefaultSettings(context);

        return settings;
    }

    public void saveHaloTopics(List<String> haloTopics) {
        Gson gson = new Gson();
        String json = gson.toJson(haloTopics);

        set(
                HALO_TOPICS_GROUPS,
                json
        );
    }

    public List<String> getHaloTopics() {
        String obj = saves.getString(
                HALO_TOPICS_GROUPS,
                null
        );
        if (obj != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>(){}
                    .getType();
            return gson.fromJson(
                    obj,
                    type
            );
        }
        return  null;
    }

    public void saveHaloCards(HaloResponse haloResponse) {
        Gson gson = new Gson();
        String json = gson.toJson(haloResponse);

        set(
                HALO_CARDS_SET,
                json
        );
    }

    public HaloResponse getHaloCards() {
        String obj = saves.getString(
                HALO_CARDS_SET,
                null
        );
        if (obj != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<HaloResponse>(){}
                    .getType();
            return gson.fromJson(
                    obj,
                    type
            );
        }
        return  null;
    }

    public void set(String key, String value) {
        saves.edit()
                .putString(
                        key,
                        value
                )
                .apply();
    }
}
