package com.blackspider.gallery.ui.albumDetails;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.blackspider.gallery.ui.base.BasePresenter;
import com.blackspider.gallery.data.model.ImageItem;

import java.util.List;

public class AlbumDetailsPresenter extends BasePresenter<AlbumDetailsMvpView> {
    private Handler foregroundHandler;
    private Handler backgroundHandler;

    private HandlerThread handlerThread;

    public AlbumDetailsPresenter() {
        foregroundHandler = new Handler(Looper.getMainLooper());
        handlerThread = new HandlerThread("Background", Thread.MAX_PRIORITY);
        handlerThread.start();

        backgroundHandler = new Handler(handlerThread.getLooper());
    }

    public void showMessage(final String message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getMvpView().showMessage(message);
            }
        }).start();
    }

    public void updateDataOnForegroundThread(final AlbumDetailsAdapter albumDetailsAdapter, final List<ImageItem> imageItems){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                albumDetailsAdapter.setImageItems(imageItems);
            }
        };

        foregroundHandler.post(runnable);
    }

    public void loadPhotosOnBackgroundThread(final Activity activity, final String albumName){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    List<ImageItem> imageItems = ImageItem.getAlbumPhotos(activity, albumName);
                    getMvpView().showPhotos(imageItems);
                    //getMvpView().showMessage("Total "+imageItems.size()+" images found! ");
                } catch (Exception e) {
                    Log.d("Rc err:", e.getMessage());
                }
            }
        };

        backgroundHandler.post(runnable);
    }
}
