package com.xtelsolution.xmec.view.fragment.diseases;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.DiseaseObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by vivu on 11/27/17
 * xtel.vn
 */
public class RecentDiseaseAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<DiseaseObject> listData;
    OnItemCLick iClickItem;

    public RecentDiseaseAdapter(OnItemCLick iClickItem, List<DiseaseObject> listData) {
        this.iClickItem = iClickItem;
        this.listData = listData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disease, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder h = (ViewHolder) holder;
            h.swipeLayout.animateReset();
            h.setData(listData.get(position));
            h.tvNameDrug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    h.swipeLayout.animateReset();
                    iClickItem.onClickItem(position, listData.get(position));
                }
            });

            h.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeItem(position);
                    iClickItem.onDeleteItem();
                }
            });

            h.swipeLayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
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
    }

    private void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public List<DiseaseObject> getList() {
        return listData;
    }

    public void updateCollection(List<DiseaseObject> list, boolean clearOld) {
        if (clearOld) {
            listData.clear();
        }
        this.listData.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_search_drug)
        ImageView imgSearchDrug;
        @BindView(R.id.tv_name_drug)
        TextView tvNameDrug;
        @BindView(R.id.tv_group_drug)
        TextView tvGroupDrug;
        @BindView(R.id.tv_delete)
        TextView tv_delete;
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

}