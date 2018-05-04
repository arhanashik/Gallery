package com.blackspider.gallery.ui.albumDetails;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blackspider.gallery.R;
import com.blackspider.gallery.data.constant.AppConstants;
import com.blackspider.gallery.data.model.ImageItem;
import com.blackspider.gallery.ui.image.ImageItemActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by USER on 3/9/2018.
 */

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyViewHolder> {
    private Context mContext;
    private List<ImageItem> mImageItems;

    public AlbumDetailsAdapter(Context context, List<ImageItem> imageItems) {
        mContext = context;
        mImageItems = imageItems;
    }

    public List<ImageItem> getImageItems() {
        return mImageItems;
    }

    public ImageItem getImageItem(int position){
        if(getItemCount() == 0) return null;

        if(getItemCount()<position) return mImageItems.get(0);

        return mImageItems.get(position);
    }

    public void setImageItems(List<ImageItem> mImageItems) {
        this.mImageItems = mImageItems;
        notifyDataSetChanged();
    }

    @Override
    public AlbumDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.item_image, parent, false);
        AlbumDetailsAdapter.MyViewHolder viewHolder = new AlbumDetailsAdapter.MyViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumDetailsAdapter.MyViewHolder holder, int position) {

        ImageItem imageItem = mImageItems.get(position);
        ImageView imageView = holder.mPhotoImageView;

        Glide.with(mContext)
                .load(imageItem.getUrl())
                .placeholder(R.drawable.app_icon)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return (mImageItems.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mPhotoImageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                ImageItem imageItem = mImageItems.get(position);
                Intent intent = new Intent(mContext, ImageItemActivity.class);
                intent.putExtra(AppConstants.EXTRA_IMAGE_ITEM, imageItem);
                mContext.startActivity(intent);
            }
        }
    }
}
