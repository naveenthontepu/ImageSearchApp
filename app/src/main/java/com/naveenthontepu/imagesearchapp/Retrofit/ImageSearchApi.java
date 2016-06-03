package com.naveenthontepu.imagesearchapp.Retrofit;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by naveen thontepu on 01-06-2016.
 */
public interface ImageSearchApi {
    @GET
    Call<JsonElement> getImageSearchApi(@Url String url);
}
