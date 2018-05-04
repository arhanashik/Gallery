package com.blackspider.gallery.ui.albumDetails;

import com.blackspider.gallery.ui.base.MvpView;
import com.blackspider.gallery.data.model.ImageItem;

import java.util.List;

public interface AlbumDetailsMvpView extends MvpView {
    void showMessage(String message);
    void showPhotos(List<ImageItem> imageItems);
}
