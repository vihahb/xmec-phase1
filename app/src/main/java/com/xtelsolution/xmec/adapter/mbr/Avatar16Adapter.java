package com.xtelsolution.xmec.adapter.mbr;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.view.widget.RoundImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/19/2017
 * Email: leconglongvu@gmail.com
 */
public class Avatar16Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ShareAccounts> listData;

    private final int ITEM_TYPE = 1;
    private final int MORE_TYPE = 2;

    public Avatar16Adapter() {
        listData = new ArrayList<>();
    }

    public void setListData(List<ShareAccounts> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    public void clearData() {
        listData.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case MORE_TYPE:
                return new MoreHolder(layoutInflater.inflate(R.layout.item_avatar_16_add, parent, false));
            case ITEM_TYPE:
                return new ItemHolder(layoutInflater.inflate(R.layout.item_avatar_16, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;

            ShareAccounts shareAccounts = listData.get(position);

            itemHolder.setData(shareAccounts);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listData.get(position) == null)
            return MORE_TYPE;
        else
            return ITEM_TYPE;
    }

    public List<ShareAccounts> getList() {
        return listData;
    }

    public void removeItem(int id) {
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).getId() == id){
                listData.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public void removeObj(ShareAccounts obj) {
        listData.remove(obj);
        notifyDataSetChanged();
    }

    private class MoreHolder extends RecyclerView.ViewHolder {

        public MoreHolder(View itemView) {
            super(itemView);
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private RoundImage imgAvatar;
        private View state;

        ItemHolder(View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_avatar);
            state = itemView.findViewById(R.id.view_status);
        }

        private void setData(ShareAccounts shareAccounts) {
            WidgetUtils.setImageURL(imgAvatar, shareAccounts.getAvatar(), R.mipmap.im_docter);

            state.setVisibility(View.VISIBLE);

            int drawable = R.drawable.background_circle_blue;

            switch (shareAccounts.getShareState()) {
                case 0:
                    drawable = R.drawable.background_circle_green;
                    break;
                case 1:
                    drawable = R.drawable.background_circle_blue;
                    break;
                case 2:
                    drawable = R.drawable.background_circle_red;
                    break;
            }

            state.setBackground(ContextCompat.getDrawable(itemView.getContext(), drawable));
        }
    }
}