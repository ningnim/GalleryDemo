package com.vidstatus.gallery.demo.gallery;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quvideo.vivashow.library.commonutils.XYSizeUtils;
import com.vidstatus.gallery.demo.R;
import com.vidstatus.gallery.demo.gallery.base.GalleryTypePopView;
import com.vivalab.library.gallery.VidMultiGalleryFragment;
import com.vivalab.library.gallery.VidSimpleGalleryFragment;
import com.vivalab.library.gallery.bean.Media;
import com.vivalab.library.gallery.bean.PhotoDirectory;
import com.vivalab.library.gallery.util.FilePickerConst;
import com.vivalab.mobile.log.VivaLog;

import java.util.ArrayList;
import java.util.List;


public class GalleryWithFolderFragment extends Fragment {

    private static final String TAG = "GalleryWithFolderFragment";

    private VidSimpleGalleryFragment photoFragment;
    private boolean mIsMulti;


    private TextView textViewTitle;
    private ImageView mIvFolder;

    GalleryWithFolderListener galleryWithFolderListener;

    private List<PhotoDirectory> directories;

    private int mSelectMax = 3;


    public interface GalleryWithFolderListener {
        void onSelected(List<String> paths);
    }

    public static GalleryWithFolderFragment newInstance(GalleryWithFolderListener listener) {
        GalleryWithFolderFragment fragment = new GalleryWithFolderFragment();
        fragment.galleryWithFolderListener = listener;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected int getRes() {
        return R.layout.fragment_vivashow_gallery_tab_frame;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getRes(), container, false);
        mIvFolder = view.findViewById(R.id.iv_folder);
        mIvFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGalleryTypePopWindow();
            }
        });

        View layoutTitle = view.findViewById(R.id.layoutTitle);
        layoutTitle.bringToFront();
        view.findViewById(R.id.viewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPage();
            }
        });


        if (mIsMulti) {
            photoFragment = VidMultiGalleryFragment.newInstance(
                    mSelectMax,
                    FilePickerConst.MediaType.Image,
                    new VidMultiGalleryFragment.Listener() {
                        @Override
                        public void onDataGet(List<PhotoDirectory> dirs) {
                            directories = dirs;
                            photoFragment.setData(dirs.get(0));
                        }

                        @Override
                        public void onSelect(List<Media> medias) {
                            List<String> paths = new ArrayList<>();
                            for (Media media : medias) {
                                paths.add(media.getPath());
                                VivaLog.d(TAG, "path:" + media.getPath());
                            }
                            galleryWithFolderListener.onSelected(paths);
                        }

                    });


            ViewGroup footView = (ViewGroup) inflater.inflate(R.layout.gallery_mulit_bottom_view, null);
            final ImageView nextView = footView.findViewById(R.id.my_ll_next);
            final TextView photoNum = footView.findViewById(R.id.tv_photo_num);
            ((VidMultiGalleryFragment) photoFragment).setFootView(footView);

            final List<String> myPaths = new ArrayList<>();

            ((VidMultiGalleryFragment) photoFragment).setFootViewListener(new VidMultiGalleryFragment.FootViewListener() {
                @Override
                public void onSelect(List<Media> medias) {
                    myPaths.clear();
                    for (Media media : medias) {
                        myPaths.add(media.getPath());
                        VivaLog.d(TAG, "path:" + media.getPath());
                    }
                    if (medias.size() > 0) {
                        nextView.setImageResource(R.drawable.photo_choose);
                    } else {
                        nextView.setImageResource(R.drawable.photo_un_choose);
                    }
                    photoNum.setText(String.format(getResources().getString(R.string.select_photo_nums), medias.size()));
                }

            });

            nextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (String path : myPaths) {
                        VivaLog.d(TAG, "自定义的path:" + path);
                    }

                    if (myPaths.size() > 0) {
                        galleryWithFolderListener.onSelected(myPaths);
                    }
                }
            });

            MyPhotoGalleryAdapter myVideoGalleryAdapter = new MyPhotoGalleryAdapter(getContext());
            myVideoGalleryAdapter.setSelectMax(mSelectMax);
            photoFragment.setAdapter(myVideoGalleryAdapter);


            photoFragment.setSpanCount(3);
            String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses/";
            photoFragment.setFilePath(mFilePath);
            photoFragment.setDirectoryName("My WhatsApp Status");
        } else {
            photoFragment = VidSimpleGalleryFragment.newInstance(
                    FilePickerConst.MediaType.Image,
                    new VidSimpleGalleryFragment.Listener() {
                        @Override
                        public void onDataGet(List<PhotoDirectory> dirs) {
                            directories = dirs;
                            photoFragment.setData(dirs.get(0));
                        }

                        @Override
                        public void onSelect(Media media) {
                            VivaLog.d(TAG, "path:" + media.getPath());
                            List<String> paths = new ArrayList<>();
                            paths.add(media.getPath());
                            galleryWithFolderListener.onSelected(paths);
                        }
                    });

            MyPhotoGalleryAdapter myVideoGalleryAdapter = new MyPhotoGalleryAdapter(getContext());
            myVideoGalleryAdapter.setSelectMax(mSelectMax);
            photoFragment.setAdapter(myVideoGalleryAdapter);

            String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses/";
            photoFragment.setFilePath(mFilePath);
            photoFragment.setDirectoryName("My WhatsApp Status");
        }

        getChildFragmentManager().beginTransaction().add(R.id.fl, photoFragment).commit();


        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewTitle.setText("Photos");

        return view;
    }

    private void finishPage() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().finish();
        }
    }


    GalleryTypePopView pop;

    private void showGalleryTypePopWindow() {
        if (directories == null) {
            return;
        }
        if (pop == null) {
            pop = new GalleryTypePopView(mIvFolder.getContext(), directories, new GalleryTypePopView.OnItemClickListener() {
                @Override
                public void onItemClick(PhotoDirectory directory) {
                    photoFragment.setData(directory);
                    pop.setSelect(directory);
                }
            });
            pop.setTouchable(true);
            pop.setOutsideTouchable(true);
            pop.setBackgroundDrawable(new ColorDrawable(0x00000000));
            pop.setFocusable(true);
            pop.setSelect(directories.get(0));
        }
        pop.showAsDropDown(mIvFolder, -XYSizeUtils.dp2px(mIvFolder.getContext(), 206), 0);
    }

    public boolean isMulti() {
        return mIsMulti;
    }

    public void setIsMulti(boolean mIsMulti) {
        this.mIsMulti = mIsMulti;
    }

    public int getSelectMax() {
        return mSelectMax;
    }

    public void setSelectMax(int mSelectMax) {
        this.mSelectMax = mSelectMax;
    }
}
