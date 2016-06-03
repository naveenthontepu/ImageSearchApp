package com.naveenthontepu.imagesearchapp.image;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.naveenthontepu.imagesearchapp.Utilities;

public class GetImageFromUrl {
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 10000;

    private static ImageCache webImageCache;
    Utilities utilities;

    private String url;

    public GetImageFromUrl(String url) {
        this.url = url;
        utilities = new Utilities();
    }

    public Bitmap getBitmap(Context context) {
        utilities.printLog("asdfgh","getBitmap = "+webImageCache);
        if(webImageCache == null) {
            webImageCache = ImageCache.getInstance(context);
            Log.i("imageSearch","webImageCache = "+webImageCache);
        }
        Bitmap bitmap = null;
        if(url != null) {
            bitmap = webImageCache.get(url);
            if(bitmap == null) {
                bitmap = getBitmapFromUrl(url);
                if(bitmap != null){
                    webImageCache.put(url, bitmap);
                }
            }
        }
        return bitmap;
    }

    private Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap = null;

        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            bitmap = BitmapFactory.decodeStream((InputStream) conn.getContent());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
