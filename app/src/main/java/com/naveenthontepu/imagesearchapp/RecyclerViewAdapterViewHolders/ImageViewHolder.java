package com.naveenthontepu.imagesearchapp.RecyclerViewAdapterViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.naveenthontepu.imagesearchapp.R;
import com.naveenthontepu.imagesearchapp.image.WebImageView;

/**
 * Created by naveen thontepu on 01-06-2016.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {
    WebImageView smartImageView;
    public ImageViewHolder(View itemView) {
        super(itemView);
        smartImageView = (WebImageView) itemView.findViewById(R.id.searchResultImageView);
    }
}
