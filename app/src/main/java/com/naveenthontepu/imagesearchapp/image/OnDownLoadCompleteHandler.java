package com.naveenthontepu.imagesearchapp.image;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

/**
 * Created by naveen thontepu on 02-06-2016.
 */
public class OnDownLoadCompleteHandler extends Handler {
    OnDownLoadCompleteListener onDownLoadCompleteListener;

    public OnDownLoadCompleteHandler(OnDownLoadCompleteListener onDownLoadCompleteListener) {
        this.onDownLoadCompleteListener = onDownLoadCompleteListener;
    }

    @Override
    public void handleMessage(Message msg) {
        Bitmap bitmap = (Bitmap)msg.obj;
        onDownLoadCompleteListener.onComplete(bitmap);
    }

}
