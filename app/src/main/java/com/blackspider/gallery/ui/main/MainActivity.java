package com.blackspider.gallery.ui.main;

import android.app.ProgressDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.blackspider.gallery.R;
import com.blackspider.gallery.ui.base.BaseActivity;
import com.blackspider.gallery.data.model.AlbumItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<MainMvpView, MainPresenter> implements MainMvpView {
    private ProgressDialog pd;

    private RecyclerView recyclerView;
    private List<AlbumItem> albumItems;
    private AlbumAdapter albumAdapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void startUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initRecyclerView();
    }

    @Override
    protected void stopUI() {

    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAlbums(final List<AlbumItem> albumItems) {
        if(pd.isShowing()) pd.dismiss();
        this.albumItems.clear();
        this.albumItems = albumItems;
        presenter.updateDataOnForegroundThread(albumAdapter, albumItems);
    }

    private void initRecyclerView(){
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView = findViewById(R.id.rv_albums);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        albumItems = new ArrayList<>();
        albumAdapter = new AlbumAdapter(this, albumItems);
        recyclerView.setAdapter(albumAdapter);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading images...");
        pd.show();
        presenter.loadPhotosOnBackgroundThread(this);
    }
}
