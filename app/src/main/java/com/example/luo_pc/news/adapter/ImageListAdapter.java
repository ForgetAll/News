package com.example.luo_pc.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luo_pc.news.R;
import com.example.luo_pc.news.bean.ImageBean;
import com.example.luo_pc.news.view.SquareImageView;

import java.util.ArrayList;

/**
 * Created by luo-pc on 2016/7/7.
 */
public class ImageListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ImageListAdapter";
    ArrayList<ImageBean> imageList = null;
    Context context;

    //列表元素
    public static final int TYPE_ITEM = 0;
    //脚布局
    public static final int TYPE_FOOTER = 1;


    private OnItemClickListener onItemClickListener;

    public ImageListAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<ImageBean> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View root = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_image_item, parent, false);
            ItemViewHolder vh = new ItemViewHolder(root);
            return vh;
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
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder vh = (ItemViewHolder) holder;
            vh.tv_img_desc.setText(imageList.get(position).getDesc());
            Glide.with(context).load(imageList.get(position).getUrl())
                    .error(R.drawable.ic_image_loadfail).crossFade().into(vh.siv_img);


//            Log.i(TAG, "height=" + height + "width=" + width);
//        ViewGroup.LayoutParams params = itemViewHolder.siv_img.getLayoutParams();
//        params.height = height;
        }
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

        private final SquareImageView siv_img;
        private final TextView tv_img_desc;

        public ItemViewHolder(View itemView) {
            super(itemView);
            siv_img = (SquareImageView) itemView.findViewById(R.id.siv_img);
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
