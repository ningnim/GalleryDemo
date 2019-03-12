package com.vidstatus.gallery.demo.gallery.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vidstatus.gallery.demo.R;
import com.vivalab.library.gallery.bean.PhotoDirectory;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class GalleryPopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "PhotoGridAdapter";

    private List<PhotoDirectory> directories;
    private Context context;
    private Listener listener;
    private PhotoDirectory select;

    public GalleryPopAdapter(Context context,
                             List<PhotoDirectory> directories,
                             Listener listener) {
        this.context = context;
        this.directories = directories;
        this.listener = listener;

        if (this.directories == null) {
            this.directories = new LinkedList<>();
        }

    }

    public void setSelect(PhotoDirectory directory) {
        this.select = directory;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vid_gallery_item_pop, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return directories.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindItem(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView iv;
        public TextView mNumberTextView;
        public TextView mNameTextView;
        private ImageView mIvSelect;

        public PhotoDirectory directory;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && directory != null) {
                        listener.onClick(directory);
                    }
                }
            });
            iv = itemView.findViewById(R.id.iv);
            mNameTextView = itemView.findViewById(R.id.tv_name);
            mNumberTextView = itemView.findViewById(R.id.tv_number);
            mIvSelect = itemView.findViewById(R.id.iv_select);

        }

        public void bindItem(int position) {
            this.directory = directories.get(position);

            mNameTextView.setText(directory.getName());

            if (directory.getMedias() != null && directory.getMedias().size() > 0) {


                RequestOptions requestOptions = new RequestOptions()
                        .error(R.drawable.vid_gallery_folder_error);


                Glide.with(context)
                        .load(directory.getMedias().get(0).getPath())
                        .apply(requestOptions)
                        .into(iv);
                //-1是因为集合里包含了时间戳的item
                mNumberTextView.setText(String.valueOf(directory.getMedias().size() - 1));
            } else {
                Glide.with(context).clear(iv);
                mNumberTextView.setText("0");
            }

            if (this.directory == select) {
                mIvSelect.setVisibility(View.VISIBLE);
            } else {
                mIvSelect.setVisibility(View.INVISIBLE);
            }
        }
    }


    public interface Listener {

        void onClick(PhotoDirectory directory);

    }
}
