package com.blackspider.gallery.data.model;

import android.app.Activity;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 3/9/2018.
 */

public class ImageItem implements Parcelable {
    private int mId;
    private String mUrl;
    private String mTitle;
    private long mDate;
    private long mSize;

    public ImageItem() {
    }

    public ImageItem(int id, String url, String title, long date, long size) {
        mId = id;
        mUrl = url;
        mTitle = title;
        mDate = date;
        mSize = size;
    }

    protected ImageItem(Parcel in) {
        mId = in.readInt();
        mUrl = in.readString();
        mTitle = in.readString();
        mDate = in.readLong();
        mSize = in.readLong();
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel in) {
            return new ImageItem(in);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public long getmDate() {
        return mDate;
    }

    public void setmDate(long mDate) {
        this.mDate = mDate;
    }

    public long getmSize() {
        return mSize;
    }

    public void setmSize(long mSize) {
        this.mSize = mSize;
    }

    public static List<ImageItem> getImageItems(Activity activity) {
        int count;
        List<ImageItem> images = new ArrayList<>();
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor imageCursor = activity.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
        count = imageCursor.getCount();
        for (int i = 0; i < count; i++) {
            imageCursor.moveToPosition(i);
            int id = imageCursor.getInt(image_column_index);
            int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //ImageItem imageItem= new ImageItem(id, imageCursor.getString(dataColumnIndex), "Image " + i);
            //images.add(imageItem);
            //Log.d("Loading "+i+":", imageItem.getUrl());

            if(i>150) break;
        }

        return images;
    }

    public static List<ImageItem> getAlbumPhotos(Activity activity, String bucketName) {
        List<ImageItem> imageItems = new ArrayList<>();
        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams = "bucket_display_name = \"" + bucketName + "\"";

            //final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
            Cursor mPhotoCursor = activity.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    searchParams, null, orderBy + " DESC");
            int image_column_index = mPhotoCursor.getColumnIndex(MediaStore.Images.Media._ID);
            int dataColumnIndex = mPhotoCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int titleColumnIndex = mPhotoCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int dateColumnIndex = mPhotoCursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
            int sizeColumnIndex = mPhotoCursor.getColumnIndex(MediaStore.Images.Media.SIZE);

            if (mPhotoCursor.getCount() > 0) {
                if (mPhotoCursor != null && mPhotoCursor.moveToFirst()) {
                    for(int i = 0; i<mPhotoCursor.getCount(); i++){
                        int imgId = mPhotoCursor.getInt(image_column_index);
                        String filePath = mPhotoCursor.getString(dataColumnIndex);
                        String fileName = mPhotoCursor.getString(titleColumnIndex);
                        long fileDate = mPhotoCursor.getLong(dateColumnIndex);
                        long fileSize = mPhotoCursor.getLong(sizeColumnIndex);

                        ImageItem imageItem = new ImageItem(imgId, filePath, fileName, fileDate, fileSize);
                        imageItems.add(imageItem);

                        mPhotoCursor.moveToNext();

                        if(i>150) break;
                    }
                    mPhotoCursor.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
        parcel.writeLong(mDate);
        parcel.writeLong(mSize);
    }
}
