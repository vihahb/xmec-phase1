package com.xtelsolution.xmec.view.fragment.diseases;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.OnLoadMoreListener;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.DiseaseObject;
import com.xtelsolution.xmec.view.fragment.news.CategoryListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by vivu on 11/20/17.
 */

public class AdapterDisease extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<DiseaseObject> listData;
    OnItemCLick iClickItem;
    private boolean loadMore = false;
    private OnLoadMoreListener loadMoreListener;
    private int VIEW_LOAD = 0, VIEW_ITEM = 1;

    /**
     * Constructor Adapter
     *
     * @param iClickItem
     * @param loadMoreListener
     */
    public AdapterDisease(OnItemCLick iClickItem, OnLoadMoreListener loadMoreListener) {
        this.iClickItem = iClickItem;
        listData = new ArrayList<>();
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * Inflate Layout View
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disease, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_LOAD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loadmore_layout, parent, false);
            ButterKnife.bind(this, view);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    /**
     * set boolean loadmore
     *
     * @param loadMore
     */
    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }


    /**
     * Nếu holder instance LoadHolder
     * Nếu load more = true => Callback Load more excute
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder h = (ViewHolder) holder;
            h.setData(listData.get(position));
            h.swipeLayout.setRightSwipeEnabled(false);
            h.swipeLayout.setLeftSwipeEnabled(false);
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickItem.onClickItem(position, listData.get(position));
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            if (loadMore) {
                loadMoreListener.onLoadMore();
                viewHolder.itemView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.itemView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Trả về type view
     * position >= listData.size() => TYPE_LOAD
     * else => TYPE_VIEW
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        /**
         * return the type of the row,
         * the last row indicates the user that the RecyclerViewView is loading more data
         */
        return (position >= listData.size()) ? VIEW_LOAD
                : VIEW_ITEM;
    }

    /**
     * return All data List + Position Load (Item Load More)
     * @return
     */
    @Override
    public int getItemCount() {
        return listData.size() + 1;
    }

    public List<DiseaseObject> getList() {
        return listData;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_search_drug)
        ImageView imgSearchDrug;
        @BindView(R.id.tv_name_drug)
        TextView tvNameDrug;
        @BindView(R.id.tv_group_drug)
        TextView tvGroupDrug;
        @BindView(R.id.swipeLayout)
        SwipeLayout swipeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(DiseaseObject data) {
            tvNameDrug.setText(data.getName());
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

    /**
     * if load more => clearOld = false
     * if load all = > clearOld = true
     * @param list
     * @param clearOld
     */
    public void updateCollection(List<DiseaseObject> list, boolean clearOld) {
        if (clearOld) {
            listData.clear();
        }
        this.listData.addAll(list);
        notifyDataSetChanged();
    }
}
