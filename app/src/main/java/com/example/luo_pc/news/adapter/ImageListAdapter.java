package com.example.luo_pc.news.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.luo_pc.news.R;
import com.example.luo_pc.news.bean.ImageBean;
import com.example.luo_pc.news.widget.RatioImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luo-pc on 2016/7/7.
 * desc:
 */
public class ImageListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ImageListAdapter";
    List<ImageBean> imageList = null;
    Context context;
    Map<Integer, Float> rationList;

    //列表元素
    public static final int TYPE_ITEM = 0;
    //脚布局
    public static final int TYPE_FOOTER = 1;


    private OnItemClickListener onItemClickListener;

    public ImageListAdapter(Context context) {
        this.context = context;
        setHasStableIds(true);
    }

    public void setData(List<ImageBean> imageList) {
        if (this.imageList != null) {
            notifyItemRangeChanged(this.imageList.size() + 1, imageList.size());
        } else {
            this.imageList = imageList;
            notifyDataSetChanged();
            return;
        }
        this.imageList = imageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View root = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_image_item, parent, false);
            return new ItemViewHolder(root);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_listview_footer, null);

            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (rationList == null) rationList = new HashMap<Integer, Float>();
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder vh = (ItemViewHolder) holder;
            vh.tv_img_desc.setText(imageList.get(position).getDesc());
            if (rationList.get(position) != null) {
                vh.siv_img.setOriginalSize(rationList.get(position));
                Log.e(TAG, "position:" + position + " ration:" + rationList.get(position) + " width:" + vh.siv_img.getWidth() + " height:" + vh.siv_img.getHeight());
            }

            Glide.with(context)
                    .load(imageList.get(position).getUrl())
                    .error(R.drawable.ic_image_loadfail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade().into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    float ration = (float) resource.getIntrinsicWidth() / (float) resource.getIntrinsicHeight();
                    rationList.put(position, ration);
                    vh.siv_img.setImageDrawable(resource);
                }
            });

        }
    }

    @Override
    public long getItemId(int position) {
        if (imageList == null || position == imageList.size())
            return super.getItemId(position);
        return imageList.get(position).getUrl().hashCode()
                + imageList.get(position).getDesc().hashCode();
    }

    private boolean isShow = true;

    @Override
    public int getItemCount() {
        int count = isShow ? 1 : 0;
        if (imageList == null) {
            return count;
        }
        return count + imageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //将最后一个元素设置成脚布局
        if (!isShow) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setisShow(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean getisShow() {
        return isShow;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RatioImageView siv_img;
        private final TextView tv_img_desc;

        public ItemViewHolder(View itemView) {
            super(itemView);
            siv_img = (RatioImageView) itemView.findViewById(R.id.siv_img);
            tv_img_desc = (TextView) itemView.findViewById(R.id.tv_img_desc);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, this.getLayoutPosition());
            }
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View view) {
            super(view);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() == TYPE_FOOTER) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }
}
