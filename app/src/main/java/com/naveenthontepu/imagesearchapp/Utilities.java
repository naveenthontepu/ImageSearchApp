package com.naveenthontepu.imagesearchapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by naveen thontepu on 31-05-2016.
 */
public class Utilities {
    public void printLog(String message){
        printLog("imageSearch",message);
    }

    public void printLog(String Tag,String message){
        if (true){
            Log.i(Tag,message);
        }
    }

    public boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = null;
        try {
            connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager!=null){
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info!=null && info.isConnected()){
                    printLog("network","isNetworkAvailable = true");
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        printLog("network","isNetworkAvailable = false");
        return false;

    }



}
