package com.vidstatus.gallery.demo;

import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.quvideo.xiaoying.common.ToastUtils;
import com.vidstatus.gallery.demo.gallery.MyVideoGalleryAdapter;
import com.vivalab.library.gallery.VidSimpleGalleryFragment;
import com.vivalab.library.gallery.bean.Media;
import com.vivalab.library.gallery.bean.PhotoDirectory;
import com.vivalab.library.gallery.util.FilePickerConst;

import java.util.List;

public class VideoActivity extends AppCompatActivity {

    private VidSimpleGalleryFragment videoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoFragment = VidSimpleGalleryFragment.newInstance(
                FilePickerConst.MediaType.Video,
                "",
                new VidSimpleGalleryFragment.Listener() {
                    @Override
                    public void onDataGet(List<PhotoDirectory> dirs) {
                        //videoDirectories = dirs;
                        videoFragment.setData(dirs.get(0));
                    }

                    @Override
                    public void onSelect(Media media) {
                        ToastUtils.show(VideoActivity.this, "视频地址:" + media.getPath(),
                                Toast.LENGTH_LONG);

                    }
                });

        MyVideoGalleryAdapter myVideoGalleryAdapter = new MyVideoGalleryAdapter(this);
        videoFragment.setAdapter(myVideoGalleryAdapter);


        videoFragment.setSpanCount(2);
        videoFragment.setNeedLoadAll(true);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.fragment_video, videoFragment);
        transaction.commit();
    }
}
