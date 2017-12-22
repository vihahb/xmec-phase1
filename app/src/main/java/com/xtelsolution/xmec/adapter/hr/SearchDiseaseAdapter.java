package com.xtelsolution.xmec.adapter.hr;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.UserDiseases;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/23/2017
 * Email: leconglongvu@gmail.com
 */
public class SearchDiseaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserDiseases> listData;

    private final int LOADING_TYPE = 0;
    private final int ITEM_TYPE = 2;

    private SparseBooleanArray choose;

    public SearchDiseaseAdapter() {
        listData = new ArrayList<>();
        choose = new SparseBooleanArray();
    }

    public void clearData() {
        choose.clear();
        listData.clear();
        notifyDataSetChanged();
    }

    public void setListData(List<UserDiseases> list) {
        choose.clear();

        listData.clear();
        listData.addAll(list);

        notifyDataSetChanged();
        notifyItemRangeChanged(0, listData.size());
    }

    public List<UserDiseases> getListData() {
        List<UserDiseases> list = new ArrayList<>();

        for (UserDiseases userDiseases : listData) {
            if (choose.get(userDiseases.getId())) {
                list.add(userDiseases);
            }
        }

        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case LOADING_TYPE:
                return new ProgressHolder(layoutInflater.inflate(R.layout.item_loading, parent, false));
            case ITEM_TYPE:
                return new ItemHolder(layoutInflater.inflate(R.layout.item_search_disease, parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            final ItemHolder itemHolder = (ItemHolder) holder;

            final UserDiseases userDiseases = listData.get(position);

            itemHolder.ckChoose.setOnCheckedChangeListener(null);

            itemHolder.ckChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        choose.put(userDiseases.getId(), true);
                    } else
                        choose.put(userDiseases.getId(), false);
                }
            });

            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isChecked = !itemHolder.ckChoose.isChecked();
                    itemHolder.ckChoose.setChecked(isChecked);
                }
            });

            itemHolder.setData(userDiseases);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size() > 5 ? 5 : listData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    private class ProgressHolder extends RecyclerView.ViewHolder {

        ProgressHolder(View itemView) {
            super(itemView);
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private CheckBox ckChoose;

        ItemHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_name);
            ckChoose = itemView.findViewById(R.id.ck_choose);
        }

        private void setData(UserDiseases userDiseases) {
            txtName.setText(userDiseases.getName());

            boolean isSelected = choose.get(userDiseases.getId());
            ckChoose.setChecked(isSelected);
        }
    }
}