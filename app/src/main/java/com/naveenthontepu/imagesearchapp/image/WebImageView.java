package com.naveenthontepu.imagesearchapp.image;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by naveen thontepu on 01-06-2016.
 */
public class WebImageView extends ImageView {
    private static final int LOADING_THREADS = 4;
    private ImageThreadRunnable currentTask;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(LOADING_THREADS);

    public WebImageView(Context context) {
        super(context);
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(String url, OnDownLoadCompleteListener completeListener) {
        setImage(new GetImageFromUrl(url),null, completeListener);
    }

    public void setImageUrl(String url, Integer loadingResource, OnDownLoadCompleteListener completeListener){
        setImage(new GetImageFromUrl(url),loadingResource,completeListener);
    }

    public void setImage(final GetImageFromUrl image, final Integer loadingResource, final OnDownLoadCompleteListener completeListener) {
        if(loadingResource != null){
            setImageResource(loadingResource);
        }
        if(currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }

        currentTask = new ImageThreadRunnable(getContext(), image);
        OnDownLoadCompleteHandler onDownLoadCompleteHandler = new OnDownLoadCompleteHandler(completeListener);
        currentTask.setOnDownLoadCompleteHandler(onDownLoadCompleteHandler);
        threadPool.execute(currentTask);
    }



}
