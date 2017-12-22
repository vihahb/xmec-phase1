package com.xtelsolution.xmec.adapter.hr;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.DrugSearchEntity;
import com.xtelsolution.xmec.view.activity.inf.IClickItem;
import com.xtelsolution.xmec.view.fragment.searchDrug.DrugSearchFragment;

import java.util.ArrayList;
import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by NutIT on 07/11/2017.
 */

public class RecentDrugAdapter extends RecyclerView.Adapter<RecentDrugAdapter.ViewHolder> {

    private static final String TAG = "RecentDrugAdapter";
    IClickItem iClickItem;
    private Context ctx;
    private List<DrugSearchEntity> listData;

    public RecentDrugAdapter(IClickItem iClickItem, List<DrugSearchEntity> recentDrug) {
        this.iClickItem = iClickItem;
        listData = recentDrug;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecentDrugAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drug_swipe, parent, false));
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final RecentDrugAdapter.ViewHolder holder, final int position) {
        final DrugSearchEntity drug = listData.get(position);
        holder.swipeLayout.animateReset();
        holder.swipeLayout.setOffset(listData.get(position).getId());
        holder.setData(drug);
        holder.tvNameDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + position);
                iClickItem.onClickItem(position, drug);
                holder.swipeLayout.animateReset();
            }
        });
        holder.tvGroupDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + position);
                iClickItem.onClickItem(position, drug);
                holder.swipeLayout.animateReset();
            }
        });

        if (holder.tv_delete != null) {
            holder.tv_delete.setClickable(true);
            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeItem(position);
                    iClickItem.onDeleteItem(position, drug);
                }
            });
        }

        holder.swipeLayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {
            }

            @Override
            public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {
            }

            @Override
            public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
            }

            @Override
            public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
            }
        });
    }

    private void getPost(int position) {
        Intent intent = new Intent(ctx, DrugSearchFragment.class);
        intent.putExtra("pos", position);
        ctx.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());
    }

    public void clearData() {
        listData.clear();
        notifyDataSetChanged();
    }

    public List<DrugSearchEntity> getList() {
        return listData;
    }

    public void setListData(List<DrugSearchEntity> list) {
        listData.addAll(list);
        notifyDataSetChanged();
        notifyItemRangeChanged(0, listData.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        private TextView tvNameDrug, tvGroupDrug, tv_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNameDrug = itemView.findViewById(R.id.tv_name_drug);
            tvGroupDrug = itemView.findViewById(R.id.tv_group_drug);
            swipeLayout = itemView.findViewById(R.id.swipeLayout);
            tv_delete = itemView.findViewById(R.id.tv_delete);
        }

        private void setData(DrugSearchEntity drug) {
            String name = drug.getDrugName() != null ? drug.getDrugName() : "Chưa cập nhật";
            tvNameDrug.setText(name);
            String group = drug.getDrugType() != null ? drug.getDrugType() : "Chưa cập nhật";
            tvGroupDrug.setText(group);
        }
    }
}