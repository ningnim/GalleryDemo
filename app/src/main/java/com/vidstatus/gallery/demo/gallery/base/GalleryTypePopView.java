package com.vidstatus.gallery.demo.gallery.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.quvideo.vivashow.library.commonutils.XYSizeUtils;
import com.vidstatus.gallery.demo.R;
import com.vivalab.library.gallery.bean.PhotoDirectory;

import java.util.List;

/**
 * Created by Chris on 2018/4/25.
 * update by ALiu 2019/1/11
 */

public class GalleryTypePopView extends PopupWindow {

    public static final int MENU_TYPE_VIDEO_PHOTO = 11;
    public static final int MENU_TYPE_VIDEO = 12;
    public static final int MENU_TYPE_PHOTO = 13;
    public static final int MENU_TYPE_WHATSAPP = 14;

    OnItemClickListener listener;
    List<PhotoDirectory> directories;

    TextView viewVideoAndPhoto;
    RecyclerView rv;
    private GalleryPopAdapter adapter;

    public GalleryTypePopView(@NonNull Context context,
                              List<PhotoDirectory> directories,
                              OnItemClickListener onItemClickListener) {
        super(View.inflate(context, R.layout.vidstatus_picker_gallery_pop, null));
        listener = onItemClickListener;
        this.directories = directories;
        setWidth(XYSizeUtils.dp2px(context, 240));
        int heightdp = this.directories.size() * 65 + 30;
        heightdp = heightdp > 340 ? 340 : heightdp;
        setHeight(XYSizeUtils.dp2px(context, heightdp));
        viewVideoAndPhoto = getContentView().findViewById(R.id.tv);
        rv = getContentView().findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        adapter = new GalleryPopAdapter(context, this.directories, new GalleryPopAdapter.Listener() {
            @Override
            public void onClick(PhotoDirectory directory) {
                if (listener != null) {
                    listener.onItemClick(directory);
                }
                dismiss();
            }
        });
        rv.setAdapter(adapter);

    }

    public void setSelect(PhotoDirectory directory) {
        adapter.setSelect(directory);
    }


    public interface OnItemClickListener {

        void onItemClick(PhotoDirectory directory);
    }

}
