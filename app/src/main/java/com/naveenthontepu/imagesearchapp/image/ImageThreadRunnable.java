package com.naveenthontepu.imagesearchapp.image;

import android.content.Context;
import android.graphics.Bitmap;

public class ImageThreadRunnable implements Runnable {
    private static final int BITMAP_READY = 0;

    private boolean cancelled = false;
    private OnDownLoadCompleteHandler onDownLoadCompleteHandler;
    private GetImageFromUrl image;
    private Context context;

    public ImageThreadRunnable(Context context, GetImageFromUrl image) {
        this.image = image;
        this.context = context;
    }

    @Override
    public void run() {
        if(image != null) {
            complete(image.getBitmap(context));
            context = null;
        }
    }

    public void setOnDownLoadCompleteHandler(OnDownLoadCompleteHandler handler){
        this.onDownLoadCompleteHandler = handler;
    }

    public void cancel() {
        cancelled = true;
    }

    public void complete(Bitmap bitmap){
        if(onDownLoadCompleteHandler != null && !cancelled) {
            onDownLoadCompleteHandler.sendMessage(onDownLoadCompleteHandler.obtainMessage(BITMAP_READY, bitmap));
        }
    }
}