package com.blackspider.gallery.ui.albumDetails;

import android.app.ProgressDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.blackspider.gallery.R;
import com.blackspider.gallery.ui.base.BaseActivity;
import com.blackspider.gallery.data.model.AlbumItem;
import com.blackspider.gallery.data.constant.AppConstants;
import com.blackspider.gallery.data.model.ImageItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlbumDetailsActivity extends BaseActivity<AlbumDetailsMvpView, AlbumDetailsPresenter> implements AlbumDetailsMvpView, View.OnClickListener{
    private AlbumItem albumItem;
    private ProgressDialog pd;
    private RecyclerView recyclerView;
    private List<ImageItem> imageItems;
    private AlbumDetailsAdapter imageAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ViewFlipper viewFlipper;
    private ImageView flpImg1, flpImg2;
    private GestureDetector detector;
    private int randNum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_details;
    }

    @Override
    protected void startUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        albumItem = getIntent().getParcelableExtra(AppConstants.EXTRA_ALBUM_ITEM);
        setTitle(albumItem.getBucketName());

        initViewFlipper();

        initRecyclerView();
    }

    @Override
    protected void stopUI() {

    }

    @Override
    protected AlbumDetailsPresenter initPresenter() {
        return new AlbumDetailsPresenter();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(AlbumDetailsActivity.this, message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPhotos(final List<ImageItem> imageItems) {
        if(pd.isShowing()) pd.dismiss();
        this.imageItems.clear();
        this.imageItems = imageItems;
        presenter.updateDataOnForegroundThread(imageAdapter, imageItems);
        if(albumItem.getTotalCount()>1) viewFlipper.startFlipping();
    }

    Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            randNum = new Random().nextInt(albumItem.getTotalCount() - 1);
            ImageItem imageItem = imageAdapter.getImageItem(randNum);
            if(imageItem!=null){
                if(viewFlipper.getCurrentView()==flpImg1){
                    Glide.with(AlbumDetailsActivity.this)
                            .load(imageItem.getUrl())
                            .placeholder(R.drawable.app_icon)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(flpImg2);
                }
                else {
                    Glide.with(AlbumDetailsActivity.this)
                            .load(imageItem.getUrl())
                            .placeholder(R.drawable.app_icon)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(flpImg1);
                }
            }
        }
    };

    private void initViewFlipper(){
        viewFlipper = findViewById(R.id.flp_container);
        flpImg1 = findViewById(R.id.flp_img1);
        flpImg2 = findViewById(R.id.flp_img2);

//        detector = new GestureDetector(new SwipeGestureDetector(this, viewFlipper));
//        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(final View view, final MotionEvent event) {
//                detector.onTouchEvent(event);
//                return true;
//            }
//        });

        Glide.with(AlbumDetailsActivity.this)
                .load(albumItem.getData())
                .placeholder(R.drawable.app_icon)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(flpImg2);
        if(albumItem.getTotalCount()>1){
            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
            viewFlipper.setAutoStart(true);
            viewFlipper.setFlipInterval(3000);
            viewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
        }
    }

    private void initRecyclerView(){
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView = findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        imageItems = new ArrayList<>();
        imageAdapter = new AlbumDetailsAdapter(this, imageItems);
        recyclerView.setAdapter(imageAdapter);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading "+albumItem.getBucketName()+" images...");
        pd.show();
        presenter.loadPhotosOnBackgroundThread(this, albumItem.getBucketName());
    }
}
