package com.blackspider.gallery.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackspider.gallery.R;
import com.blackspider.gallery.ui.albumDetails.AlbumDetailsActivity;
import com.blackspider.gallery.data.model.AlbumItem;
import com.blackspider.gallery.data.constant.AppConstants;
import com.blackspider.util.Converter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by USER on 3/9/2018.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
    private List<AlbumItem> mAlbumItems;
    private Context mContext;

    public AlbumAdapter(Context context, List<AlbumItem> albumItems) {
        mContext = context;
        mAlbumItems = albumItems;
    }

    public List<AlbumItem> getAlbumItems() {
        return mAlbumItems;
    }

    public AlbumItem getAlbumItem(int position){
        if(position == 0) return null;

        if(getItemCount()<position) return mAlbumItems.get(0);

        return mAlbumItems.get(position);
    }

    public void setAlbumItems(List<AlbumItem> albumItems) {
        this.mAlbumItems = albumItems;
        notifyDataSetChanged();
    }

    @Override
    public AlbumAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View albumView = inflater.inflate(R.layout.item_album, parent, false);
        AlbumAdapter.MyViewHolder viewHolder = new AlbumAdapter.MyViewHolder(albumView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.MyViewHolder holder, int position) {

        AlbumItem albumItem = mAlbumItems.get(position);

        Glide.with(mContext)
                .load(albumItem.getData())
                .placeholder(R.drawable.app_icon)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.mAlbumCover);

        holder.mAlbumTitle.setText(albumItem.getBucketName());
        holder.mAlbumPhotoCount.setText("Total photo: " + albumItem.getTotalCount());
        holder.mAlbumCreationDate.setText("Created on: " + Converter.toDateStr(Long.valueOf(albumItem.getDateTaken())));
    }

    @Override
    public int getItemCount() {
        return (mAlbumItems.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mAlbumCover;
        public TextView mAlbumTitle, mAlbumPhotoCount, mAlbumCreationDate;

        public MyViewHolder(View itemView) {

            super(itemView);
            mAlbumCover = itemView.findViewById(R.id.album_cover);
            mAlbumTitle = itemView.findViewById(R.id.album_title);
            mAlbumPhotoCount = itemView.findViewById(R.id.album_photo_count);
            mAlbumCreationDate = itemView.findViewById(R.id.album_creation_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                AlbumItem albumItem = mAlbumItems.get(position);
                Intent intent = new Intent(mContext, AlbumDetailsActivity.class);
                intent.putExtra(AppConstants.EXTRA_ALBUM_ITEM, albumItem);
                mContext.startActivity(intent);
            }
        }
    }
}
