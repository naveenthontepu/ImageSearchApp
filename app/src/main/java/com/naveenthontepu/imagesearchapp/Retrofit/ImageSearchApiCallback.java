package com.naveenthontepu.imagesearchapp.Retrofit;

import com.google.gson.JsonElement;
import com.naveenthontepu.imagesearchapp.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by naveen thontepu on 01-06-2016.
 */
public abstract class ImageSearchApiCallback implements Callback<JsonElement> {
    Utilities utilities;

    public ImageSearchApiCallback() {
        utilities = new Utilities();
    }

    @Override
    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
        processResponse(response);
    }

    public abstract void processResponse(Response<JsonElement> response);

    @Override
    public void onFailure(Call<JsonElement> call, Throwable t) {
        utilities.printLog(t.getMessage());
        t.printStackTrace();
        processError(t.getMessage());
    }

    protected abstract void processError(String message);
}
