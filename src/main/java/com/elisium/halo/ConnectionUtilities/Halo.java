package com.elisium.halo.ConnectionUtilities;



import com.elisium.halo.API.APIRequests;
import com.elisium.halo.API.HaloAPI;
import com.elisium.halo.DTOs.HaloResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class Halo {
    private static final String TAG = Halo.class.getSimpleName();
    private Response<HaloResponse> res;

    synchronized public HaloResponse getHaloCards(String eId, float lat, float lon) throws IOException, InterruptedException {

        APIRequests prepHalo = HaloAPI.prepHalo();
        Call<HaloResponse> getCards = prepHalo.getCards("Car", eId, lat, lon, null);
        Thread gfgThread = new Thread(() -> {
            try  {
                res = getCards.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        gfgThread.start();
        gfgThread.join();


        return res != null ? res.body() : null;
    }
}
