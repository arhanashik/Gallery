package com.blackspider.gallery.ui.base;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.blackspider.gallery.data.constant.AppConstants;

/*
*  ****************************************************************************
*  * Created by : Sudipta K Paik on 26-Aug-17 at 4:02 PM.
*  * Email : sudipta@w3engineers.com
*  *
*  * Responsibility: Abstract activity that every other Activity in this application must implement.
*  *
*  * Last edited by : Sudipta on 02-11-17.
*  *
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
*  ****************************************************************************
*/
public abstract class BaseActivity<V extends MvpView, P extends BasePresenter<V>> extends AppCompatActivity implements MvpView {

    protected P presenter;
    private int mDefaultValue = -1;

    protected abstract int getLayoutId();
    protected abstract void startUI();
    protected abstract void stopUI();

    protected abstract P initPresenter();


    @SuppressWarnings("unchecked")
    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        if (layoutId > mDefaultValue) {
            setContentView(layoutId);
            presenter = initPresenter();
            presenter.attachView((V) this);
        }
        if(checkStoragePermission()){
            startUI();
        }
        else {
            requestStoragePermission();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopUI();
    }



    private boolean checkStoragePermission(){
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                AppConstants.STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == AppConstants.STORAGE_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startUI();
            }
            else {
                Toast.makeText(this, "Permission required!", Toast.LENGTH_SHORT).show();
                requestStoragePermission();
            }
        }
    }
}
