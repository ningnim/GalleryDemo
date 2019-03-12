package com.vidstatus.gallery.demo;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vidstatus.gallery.demo.gallery.GalleryWithFolderFragment;
import com.vivalab.mobile.log.VivaLog;

import java.util.List;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        GalleryWithFolderFragment galleryWithFolderFragment =
                GalleryWithFolderFragment.newInstance(new GalleryWithFolderFragment.GalleryWithFolderListener() {

                    @Override
                    public void onSelected(List<String> paths) {
                        for (String path : paths) {
                            //这里是 单选/多选 后返回的
                            VivaLog.d(TAG, "path:" + path);
                        }

                    }
                });

        //这里控制单选/多选， true 多选； false 单选。
        galleryWithFolderFragment.setIsMulti(true);
        //设置最大可选择数
        galleryWithFolderFragment.setSelectMax(3);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.fragment_photo, galleryWithFolderFragment);
        transaction.commit();
    }
}
