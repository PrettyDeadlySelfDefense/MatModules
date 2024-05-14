package com.elisium.halo.API;


import com.elisium.halo.DTOs.GenericResponse;
import com.elisium.halo.DTOs.HaloResponse;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIRequests {

    /**
     * Halo API Test call
     * @return
     */
    @GET("test/prova/")
    Observable<GenericResponse> getTestCall();

    @GET("/v1/cards")
    Call<HaloResponse> getCards(@Query("category") String cat,
                                @Query("userId") String eId,
                                @Query("lat") float lat,
                                @Query("lon") float lon,
                                @Query("gender") String gen);
}
