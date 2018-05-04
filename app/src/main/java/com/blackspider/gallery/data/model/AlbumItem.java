package com.blackspider.gallery.data.model;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AlbumItem implements Parcelable{
	
	
	private long bucketId;
	private String bucketName;
	private String dateTaken;
	private String data;
	private String albumCoverUrl;
	private int totalCount;

	public AlbumItem() {
	}

    protected AlbumItem(Parcel in) {
        bucketId = in.readLong();
        bucketName = in.readString();
        dateTaken = in.readString();
        data = in.readString();
        albumCoverUrl = in.readString();
        totalCount = in.readInt();
    }

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public long getBucketId() {
		return bucketId;
	}

	public void setBucketId(long bucketId) {
		this.bucketId = bucketId;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getDateTaken() {
		return dateTaken;
	}

	public void setDateTaken(String dateTaken) {
		this.dateTaken = dateTaken;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }

    public static final Creator<AlbumItem> CREATOR = new Creator<AlbumItem>() {
		@Override
		public AlbumItem createFromParcel(Parcel in) {
			return new AlbumItem(in);
		}

		@Override
		public AlbumItem[] newArray(int size) {
			return new AlbumItem[size];
		}
	};

	public static List<AlbumItem> getAlbumList(Activity activity) {
		List<AlbumItem> albumItemList = new ArrayList<>();

		String[] PROJECTION_BUCKET = { MediaStore.Images.ImageColumns.BUCKET_ID,
				MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DATE_TAKEN,
				MediaStore.Images.ImageColumns.DATA };

		String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
		String BUCKET_ORDER_BY = "MAX(datetaken) DESC";


		Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

		ContentResolver contentResolver = activity.getContentResolver();
		Cursor cur = contentResolver.query(images, PROJECTION_BUCKET,
				BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);

		Log.v("ListingImages", " query count=" + cur.getCount());

		AlbumItem album;

		if (cur.moveToFirst()) {
			String bucket, date, data;
			long bucketId;

			int bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
			int dateColumn = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
			int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
			int coverColumn = cur.getColumnIndex(MediaStore.Images.Media.MINI_THUMB_MAGIC);
			int bucketIdColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

			do {
				// Get the field values
				bucket = cur.getString(bucketColumn);
				date = cur.getString(dateColumn);
				data = cur.getString(dataColumn);
				bucketId = cur.getInt(bucketIdColumn);

				if (bucket != null && bucket.length() > 0) {
					album = new AlbumItem();
					album.setBucketId(bucketId);
					album.setBucketName(bucket);
					album.setDateTaken(date);
					album.setData(data);
					album.setTotalCount(photoCountByAlbum(contentResolver, bucket));
					albumItemList.add(album);
					// Do something with the values.
					Log.v("ListingImages", " bucket=" + bucket
							+ "  date_taken=" + date + "  _data=" + data
							+ " bucket_id=" + bucketId);
				}

			} while (cur.moveToNext());
		}
		cur.close();


		return albumItemList;
	}

	private static int photoCountByAlbum(ContentResolver contentResolver, String bucketName) {
		try {
			final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
			String searchParams = "bucket_display_name = \"" + bucketName + "\"";

			final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
			Cursor mPhotoCursor = contentResolver.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
					searchParams, null, orderBy + " DESC");

			if (mPhotoCursor.getCount() > 0) {
				return mPhotoCursor.getCount();
			}
			mPhotoCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeLong(bucketId);
		parcel.writeString(bucketName);
		parcel.writeString(dateTaken);
		parcel.writeString(data);
		parcel.writeString(albumCoverUrl);
		parcel.writeInt(totalCount);
	}
}
