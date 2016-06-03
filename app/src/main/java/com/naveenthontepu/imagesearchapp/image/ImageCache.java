package com.naveenthontepu.imagesearchapp.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.naveenthontepu.imagesearchapp.Utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageCache {
    private static final String CACHE_PATH = "/image_cache/";

    private HashMap<String, Bitmap> memoryCache;
    private String cachePath;
    private boolean cacheDirExits = false;
    private ExecutorService executorService;
    private static ImageCache imageCache;
    Utilities utilities;

    private ImageCache(Context context) {
        memoryCache = new HashMap<>();
        utilities = new Utilities();
        cachePath = context.getCacheDir().getAbsolutePath() + CACHE_PATH;
        File outFile = new File(cachePath);
        outFile.mkdirs();
        cacheDirExits = outFile.exists();
        executorService = Executors.newSingleThreadExecutor();
    }

    public static ImageCache getInstance(Context context){
        if (imageCache==null){
            synchronized (ImageCache.class){
                imageCache = new ImageCache(context);
            }
        }
        return imageCache;
    }

    public Bitmap get(final String url) {
        Bitmap bitmap = getBitmapFromMemory(url);
        if(bitmap == null) {
            bitmap = getBitmapFromDisk(url);
            if(bitmap != null) {
                cacheBitmapToMemory(url, bitmap);
            }
        }
        return bitmap;
    }

    public void put(String url, Bitmap bitmap) {
        cacheBitmapToMemory(url, bitmap);
        cacheBitmapToDisk(url, bitmap);
    }

    public void clearMemoryCache(){
        memoryCache.clear();
    }

    public void clearDiskCache() {
        utilities.printLog("memory cashe called = "+cachePath);
        File cachedFileDir = new File(cachePath);
        utilities.printLog("dish cashe cleared = "+(cachedFileDir.exists() && cachedFileDir.isDirectory()));
        if(cachedFileDir.exists() && cachedFileDir.isDirectory()) {
            File[] cachedFiles = cachedFileDir.listFiles();
            for(File f : cachedFiles) {
                if(f.exists() && f.isFile()) {
                    f.delete();
                    utilities.printLog("cleared");
                }
            }
        }
    }

    private void cacheBitmapToMemory(String url, Bitmap bitmap) {
        utilities.printLog("image stored in memory");
        memoryCache.put(getCacheKey(url),bitmap);
    }

    private void cacheBitmapToDisk(final String url, final Bitmap bitmap) {
        utilities.printLog("image stored in disk");
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if(cacheDirExits) {
                    BufferedOutputStream ostream = null;
                    try {
                        ostream = new BufferedOutputStream(new FileOutputStream(new File(cachePath, getCacheKey(url))), 2*1024);
                        bitmap.compress(CompressFormat.PNG, 100, ostream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(ostream != null) {
                                ostream.flush();
                                ostream.close();
                            }
                        } catch (Exception ignored) {}
                    }
                }
            }
        });
    }

    private Bitmap getBitmapFromMemory(String url) {
        return memoryCache.get(getCacheKey(url));
    }

    private Bitmap getBitmapFromDisk(String url) {
        Bitmap bitmap = null;
        if(cacheDirExits){
            String filePath = getFilePath(url);
            File file = new File(filePath);
            if(file.exists()) {
                bitmap = BitmapFactory.decodeFile(filePath);
            }
        }
        return bitmap;
    }

    private String getFilePath(String url) {
        return cachePath + getCacheKey(url);
    }

    private String getCacheKey(String url) {
        if(url != null){
            return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
        } else {
            throw new RuntimeException("Url not passed");
        }
    }
}
