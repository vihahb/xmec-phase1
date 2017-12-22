package com.xtelsolution.xmec.adapter.hr;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.OnLoadMoreListener;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.DrugSearchEntity;
import com.xtelsolution.xmec.view.activity.inf.IClickItem;
import com.xtelsolution.xmec.view.fragment.searchDrug.DrugSearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by NutIT on 02/11/2017.
 */

public class SearchNameDrugAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context ctx;
    private List<DrugSearchEntity> listData;
    IClickItem iClickItem;
    private boolean loadMore = false;
    private OnLoadMoreListener loadMoreListener;
    private int VIEW_LOAD = 0, VIEW_ITEM = 1;

    /**
     * Constructor Adapter
     *
     * @param iClickItem
     * @param loadMoreListener
     */
    public SearchNameDrugAdapter(IClickItem iClickItem, OnLoadMoreListener loadMoreListener) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_drug, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_LOAD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loadmore_layout, parent, false);
            ButterKnife.bind(this, view);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    /**
     * Nếu holder instance LoadHolder
     * Nếu load more = true => Callback Load more excute
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final DrugSearchEntity drug = listData.get(position);
            Log.d("DrugSearchEntity", "onBindViewHolder: " + drug.toString());
            viewHolder.setData(drug);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: " + position);
                    iClickItem.onClickItem(position, drug);
//                getPost(position);
//////                Toast.makeText(ctx, "Chuyển màn hình", Toast.LENGTH_SHORT).show();
//                if(ctx instanceof IClickItem)
//                Log.d(TAG, "onClick: "+position);
//                iClickItem.onClickItem(position);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;

            if (loadMore)
                loadMoreListener.onLoadMore();
            else
                viewHolder.itemView.setVisibility(View.GONE);

        }

    }

    private void getPost(int position) {
        Intent intent = new Intent(ctx, DrugSearchFragment.class);
        intent.putExtra("pos", position);
        ctx.startActivity(intent);
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

    @Override
    public int getItemCount() {
        return listData.size() + 1;
    }

    /**
     * set boolean loadmore
     *
     * @param loadMore
     */
    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameDrug, tvGroupDrug;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNameDrug = itemView.findViewById(R.id.tv_name_drug);
            tvGroupDrug = itemView.findViewById(R.id.tv_group_drug);
        }

        private void setData(DrugSearchEntity drug) {
            String name = drug.getDrugName() != null ? drug.getDrugName() : "Chưa cập nhật";
            tvNameDrug.setText(name);
            String group = drug.getDrugType() != null ? drug.getDrugType() : "Chưa cập nhật";
            tvGroupDrug.setText(group);

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

    public void clearData() {
        listData.clear();
        notifyDataSetChanged();
    }

    /**
     * if load more => clearOld = false
     * if load all = > clearOld = true
     *
     * @param list
     * @param clearOld
     */
    public void setListData(List<DrugSearchEntity> list, boolean clearOld) {
        if (clearOld)
            listData.clear();

        listData.addAll(list);
        notifyDataSetChanged();
    }

    public List<DrugSearchEntity> getList() {
        return listData;
    }
}
