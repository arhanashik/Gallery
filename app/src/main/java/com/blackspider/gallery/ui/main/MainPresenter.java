package com.blackspider.gallery.ui.main;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.blackspider.gallery.ui.base.BasePresenter;
import com.blackspider.gallery.data.model.AlbumItem;

import java.util.List;

public class MainPresenter extends BasePresenter<MainMvpView> {
    private Handler foregroundHandler;
    private Handler backgroundHandler;

    private HandlerThread handlerThread;

    public MainPresenter() {
        foregroundHandler = new Handler(Looper.getMainLooper());
        handlerThread = new HandlerThread("Background", Thread.MAX_PRIORITY);
        handlerThread.start();

        backgroundHandler = new Handler(handlerThread.getLooper());
    }

    public void showMessage(String message){
        getMvpView().showMessage(message);
    }

    public void updateDataOnForegroundThread(final AlbumAdapter albumAdapter, final List<AlbumItem> albumItems){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                albumAdapter.setAlbumItems(albumItems);
            }
        };

        foregroundHandler.post(runnable);
    }

    public void loadPhotosOnBackgroundThread(final Activity activity){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    List<AlbumItem> albumItems = AlbumItem.getAlbumList(activity);
                    getMvpView().showAlbums(albumItems);
                    //getMvpView().showMessage("Total "+albumItems.size()+" albums found! ");
                } catch (Exception e) {
                    Log.d("Rc err:", e.getMessage());
                }
            }
        };

        backgroundHandler.post(runnable);
    }
}
