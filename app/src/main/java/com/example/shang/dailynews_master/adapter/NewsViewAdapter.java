package com.example.shang.dailynews_master.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shang.dailynews_master.config.ContentValues;
import com.example.shang.dailynews_master.R;
import com.example.shang.dailynews_master.domain.ZhihuJson;
import com.example.shang.dailynews_master.utils.SpUtils;

import java.util.List;

/**
 * Created by shang on 2017/7/30.
 */

public class NewsViewAdapter extends RecyclerView.Adapter<NewsViewAdapter.NewsViewHolder> {

    private List<ZhihuJson.Stories> newsList;
    private Context context;
    // 加载更多
    private final int TYPE_LOAD_MORE = 100;
    private final int TYPE_NORMAL = 101;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private boolean canLoadMore = true;

    private OnLoadingMore loadingMore;

    public interface OnLoadingMore {
        void onLoadMore();
    }

    public void setLoadingMore(OnLoadingMore loadingMore) {
        this.loadingMore = loadingMore;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public void addData(ZhihuJson.Stories t) {
        newsList.add(t);
        notifyDataSetChanged();
    }

    // 设置点击监听
    onMyItemClickListener onItemClickListener;

    public interface onMyItemClickListener{
        void onItemClick(View v,int pos);
    }

    public void setOnItemClick(onMyItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public NewsViewAdapter(RecyclerView recyclerView,Context context,List<ZhihuJson.Stories>stories){
        this.context = context;
        this.newsList = stories;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
               super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int itemCount = manager.getItemCount();
                int lastPosition = manager.findLastVisibleItemPosition(); // 最后一项

                if (canLoadMore && !isLoading && (lastPosition >= (itemCount - visibleThreshold))) {
                    if (loadingMore != null) {
                        isLoading = true;
                        loadingMore.onLoadMore();
                    }
                }

            }
        });
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_LOAD_MORE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, parent, false);
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
            progressBar.setInterpolator(new AccelerateInterpolator(2)); //  在动画开始的地方速率改变比较慢，然后开始加速
            progressBar.setIndeterminate(true);
        }else {
            view= LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.news_item,parent,false);
        }
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_LOAD_MORE) {
            View itemView = holder.itemView;
            if (canLoadMore && isLoading) {
                if (itemView.getVisibility() != View.VISIBLE) {
                    itemView.setVisibility(View.VISIBLE);
                }
            } else if (itemView.getVisibility() == View.VISIBLE) {
                itemView.setVisibility(View.GONE);
            }
        }else{
            holder.textItem.setText(newsList.get(position).getTitle());
            Glide.with(context).load(newsList.get(position).getImages().get(0)).into(holder.imageItem);
        }

        if (onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    onItemClickListener.onItemClick(holder.itemView,pos);
                    SpUtils.putString(context, ContentValues.ID,newsList.get(pos).getId());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1){
            return TYPE_LOAD_MORE;
        }else {
            return TYPE_NORMAL;
        }

    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        ImageView imageItem;
        TextView textItem;

        public NewsViewHolder(View itemView) {
            super(itemView);
            imageItem = (ImageView) itemView.findViewById(R.id.image_item);
            textItem = (TextView) itemView.findViewById(R.id.text_item);
        }
    }



}
