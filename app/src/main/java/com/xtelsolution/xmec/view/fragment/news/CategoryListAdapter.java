package com.xtelsolution.xmec.view.fragment.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.OnItemClickListener;
import com.xtelsolution.sdk.callback.OnLoadMoreListener;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.CategoryListEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vivu on 12/13/17
 * xtel.vn
 */
public class CategoryListAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = CategoryListAdapter.class.getSimpleName();

    LoadingViewHolder loadingViewHolder;

    Context context;
    private List<CategoryListEntity> list;
    private OnItemClickListener<CategoryListEntity> onItemClickListener;
    private int VIEW_LOAD = 0, VIEW_POST = 1;

    private boolean loadMore = false;
    private OnLoadMoreListener loadMoreListener;

    public CategoryListAdapter(Context context,
                               OnItemClickListener<CategoryListEntity> onItemClickListener, OnLoadMoreListener loadMoreListener) {
        this.context = context;
        this.list = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.loadMoreListener = loadMoreListener;

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int visibleItemCount = layoutManager.getChildCount();
//                int totalItemCount = layoutManager.getItemCount();
//                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
//                if (!isLoad) {
//                    if (pastVisibleItems + visibleItemCount >= totalItemCount) {
//                        if (loadMoreListener != null) {
//                            loadMoreListener.onLoadMore();
//                            isLoad = true;
//                        }
//                    }
//                }
//            }
//        });
    }

    @Override
    public int getItemViewType(int position) {
        /**
         * return the type of the row,
         * the last row indicates the user that the RecyclerViewView is loading more data
         */
        return (position >= list.size()) ? VIEW_LOAD
                : VIEW_POST;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_POST) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
            ButterKnife.bind(this, view);
            return new ViewHolder(view);
        } else if (viewType == VIEW_LOAD) {
            View view = LayoutInflater.from(context).inflate(R.layout.loadmore_layout, parent, false);
            ButterKnife.bind(this, view);
            return new LoadingViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            CategoryListEntity item = list.get(position);
            viewHolder.setData(item, onItemClickListener);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            this.loadingViewHolder = loadingViewHolder;
            loadingViewHolder.progressBar1.setIndeterminate(true);
            if (loadMore) {
                loadMoreListener.onLoadMore();
            }
        }
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public void refreshData(List<CategoryListEntity> data, boolean isClear) {
        Log.e(TAG, "refreshData: " + data.toString());
        if (isClear) {
            list.clear();
        }
        list.addAll(data);
        if (list.size() > 0) {
            notifyItemInserted(list.size()-1);
            notifyItemRangeInserted(list.size() - 1, data.size());
        } else {
            notifyDataSetChanged();
        }
    }

    public void addItemLoading() {
        list.add(null);
        notifyItemInserted(list.size());
    }

    public List<CategoryListEntity> getList() {
        return list;
    }

    public void removeLastItem() {
        loadMore = false;
        loadingViewHolder.itemView.setVisibility(View.GONE);
//        notifyItemRemoved(getItemCount()-2);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Todo Butterknife bindings

        @BindView(R.id.img_post)
        ImageView imgPost;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_resource_coppy)
        TextView tvResourceCoppy;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CategoryListEntity model,
                            final OnItemClickListener<CategoryListEntity> listener) {

            if (!TextUtils.isEmpty(model.getImage())) {
                WidgetUtils.setImageURL(imgPost, model.getImage(), R.mipmap.ic_small_avatar_default);
            }

            if (!TextUtils.isEmpty(model.getTitle())) {
                tvTitle.setText(model.getTitle());
            }

            if (model.getPostDateLong() != null) {
                tvTime.setText("-  " + TimeUtils.convertLongToDate(model.getPostDateLong()));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getLayoutPosition(), model);

                }
            });
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progressBar1)
        ProgressBar progressBar1;
        @BindView(R.id.tv_loading)
        TextView tvLoading;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}