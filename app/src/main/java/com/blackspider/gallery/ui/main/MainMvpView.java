package com.blackspider.gallery.ui.main;

import com.blackspider.gallery.ui.base.MvpView;
import com.blackspider.gallery.data.model.AlbumItem;

import java.util.List;

public interface MainMvpView extends MvpView {
    void showMessage(String message);
    void showAlbums(List<AlbumItem> albumItems);
}
