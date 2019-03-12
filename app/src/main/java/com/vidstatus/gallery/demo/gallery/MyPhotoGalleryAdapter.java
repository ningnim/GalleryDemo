package com.vidstatus.gallery.demo.gallery;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vidstatus.gallery.demo.R;
import com.vivalab.library.gallery.adapter.GalleryAdapter;
import com.vivalab.library.gallery.bean.Media;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class MyPhotoGalleryAdapter extends GalleryAdapter {

    private static final String TAG = "MyPhotoGalleryAdapter";

    private Context context;
    private int selectMax = 3;

    public MyPhotoGalleryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 2) {
            return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item_time, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item_photo, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (videos.get(position).mediaType == -1) {
            return 2;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == 2) {
            ((TitleViewHolder) holder).bindItem(position);
        } else if (viewType == 1) {
            ((ViewHolder) holder).bindItem(position);
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView iv;
        public TextView mTvSelect;
        public FrameLayout mFlSelectBg;

        public Media media;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && media != null) {
                        listener.onClick(media);
                    }
                }
            });
            iv = itemView.findViewById(R.id.iv);
            mTvSelect = itemView.findViewById(R.id.tv_select);
            mFlSelectBg = itemView.findViewById(R.id.fl_select_bg);

        }

        public void bindItem(int position) {
            this.media = videos.get(position);


            RequestOptions requestOptions = new RequestOptions()
                    .error(R.drawable.vid_gallery_error);


            Glide.with(context)
                    .load(media.getPath())
                    .apply(requestOptions)
                    .into(iv);

            if (isMulti) {
                mTvSelect.setVisibility(View.VISIBLE);

                if (selectMedias.contains(media)) {
                    mTvSelect.setText(String.valueOf(selectMedias.indexOf(media) + 1));
                    mFlSelectBg.setVisibility(View.VISIBLE);
                    mFlSelectBg.setBackgroundResource(R.color.color_f98900_p80);
                } else {
                    mTvSelect.setVisibility(View.GONE);
                    mFlSelectBg.setVisibility(View.GONE);

                    if (selectMedias.size() == selectMax){
                        mFlSelectBg.setVisibility(View.VISIBLE);
                        mFlSelectBg.setBackgroundResource(R.color.black_p70);
                    }
                }

            } else {
                mTvSelect.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {


        public TextView textViewTime;

        public Media media;

        public TitleViewHolder(View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.tv_time);

        }

        public void bindItem(int position) {
            this.media = videos.get(position);


            String type = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                type = android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMM");
            } else {
                type = "MMM yyyy";
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
            String time = simpleDateFormat.format(new Date(media.modified));
            textViewTime.setText(time);


        }
    }

    public int getSelectMax() {
        return selectMax;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }
}
