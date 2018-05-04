package com.blackspider.gallery.ui.image;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.MediaStore;

import com.blackspider.gallery.ui.base.BasePresenter;

import java.io.File;

public class ImageItemPresenter extends BasePresenter<ImageItemMvpView> {

    public void showMessage(final String message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getMvpView().showMessage(message);
            }
        }).start();
    }

    public boolean deleteImage(Context context, String imagePath){
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "=?" , new String[]{ imagePath });

        if (new File(imagePath).exists()) {
            getMvpView().showMessage("Image not deleted!");
            return false;
        }

        getMvpView().showMessage("Image deleted!");

        return true;
    }
}
