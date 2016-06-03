package com.naveenthontepu.imagesearchapp.RecyclerViewAdapterViewHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naveenthontepu.imagesearchapp.R;
import com.naveenthontepu.imagesearchapp.Utilities;
import com.naveenthontepu.imagesearchapp.image.OnDownLoadCompleteListener;

import java.util.Collections;
import java.util.List;

/**
 * Created by naveen thontepu on 01-06-2016.
 */
public class SearchImagesResultAdaptor extends RecyclerView.Adapter<ImageViewHolder> {

    List<String> data = Collections.emptyList();
    LayoutInflater layoutInflater;
    Utilities utilities;
    Context context;

    public SearchImagesResultAdaptor(List<String> data, Context context) {
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        utilities = new Utilities();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View searchResultImages = layoutInflater.inflate(R.layout.image_view_holder, parent, false);
        return new ImageViewHolder(searchResultImages);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        utilities.printLog("the position = " + position);
        final Integer fallbackResource = null;
        holder.smartImageView.setImageUrl(data.get(position), R.drawable.grey_screen, new OnDownLoadCompleteListener() {
            @Override
            public void onComplete(Bitmap bitmap) {
                utilities.printLog("the bitmap size = " + bitmap.getByteCount()/1024.0f);
                if (bitmap != null) {
                    holder.smartImageView.setImageBitmap(bitmap);
                } else {
                    if (fallbackResource != null) {
                        holder.smartImageView.setImageResource(fallbackResource);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
