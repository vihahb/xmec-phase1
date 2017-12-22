package com.xtelsolution.xmec.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Friends;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.entity.UserDiseases;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lecon on 11/30/2017
 */
public class TagViewAdapter extends RecyclerView.Adapter<TagViewAdapter.ViewHolder> {

    private List<Object> listData;

    private ItemClickListener itemClickListener;

    private SparseBooleanArray array;

    private boolean isDelete = true;

    public TagViewAdapter() {
        array = new SparseBooleanArray();
        listData = new ArrayList<>();
    }

    /**
     * Thêm item mới vào list data
     */
    public void addItem(Object item) {

        if (item instanceof Friends) {
            Friends friends = (Friends) item;

            if (!array.get(friends.getUid())) {
                array.put(friends.getUid(), true);
                listData.add(friends);
            }
        } else if (item instanceof ShareAccounts) {
            ShareAccounts shareAccounts = (ShareAccounts) item;

            if (!array.get(shareAccounts.getUid())) {
                array.put(shareAccounts.getUid(), true);
                listData.add(shareAccounts);
            }
        } else if (item instanceof UserDiseases) {
            listData.add(item);
        }

        notifyDataSetChanged();
    }

    /**
     * Xóa item trong list data
     */
    public void removeItem(int position) {
        Object item = listData.get(position);

        if (item instanceof Friends) {
            array.put(((Friends) item).getUid(), false);
        } else if (item instanceof ShareAccounts) {
            array.put(((ShareAccounts) item).getUid(), false);
        } else if (item instanceof UserDiseases) {
            array.put(((UserDiseases) item).getDiseaseId(), true);
        }

        listData.remove(position);
        notifyDataSetChanged();
    }

    /**
     * Cập nhật lại list data
     */
    public void setListShare(List<ShareAccounts> list) {
        listData.clear();

        for (ShareAccounts item : list) {
            listData.add(item);
            array.put(item.getUid(), true);
        }

        notifyDataSetChanged();
    }

    public void setListDisease(List<UserDiseases> list) {
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * Thêm list mới vào list data
     */
    public void addListData(List<Friends> list) {

        for (Friends item : list) {
            if (!array.get(item.getUid())) {
                listData.add(item);
                array.put(item.getUid(), true);
            }
        }

        notifyDataSetChanged();
    }

    public void addListShare(List<ShareAccounts> list) {

        for (ShareAccounts item : list) {
            if (!array.get(item.getUid())) {
                listData.add(item);
                array.put(item.getUid(), true);
            }
        }

        notifyDataSetChanged();
    }

    public List<Object> getListData() {
        return listData;
    }

    public void disableDelete() {
        isDelete = false;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tagview, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int index = position;
        final Object object = listData.get(index);

        if (object instanceof Friends) {
            holder.setData((Friends) object);
        } else if (object instanceof ShareAccounts) {
            holder.setData((ShareAccounts) object);
        } else if (object instanceof UserDiseases) {
            holder.setData((UserDiseases) object);
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(index, object);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;
        private TextView txtName;
        private ImageButton imgDelete;

        ViewHolder(View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_avatar);
            txtName = itemView.findViewById(R.id.txt_name);
            imgDelete = itemView.findViewById(R.id.img_delete);

            if (!isDelete)
                imgDelete.setVisibility(View.GONE);
            else
                imgDelete.setVisibility(View.VISIBLE);
        }

        private void setData(Friends friends) {
            WidgetUtils.setImageURL(imgAvatar, friends.getAvatar(), R.mipmap.im_docter);
            txtName.setText(friends.getFullname());
        }

        private void setData(ShareAccounts shareAccounts) {
            WidgetUtils.setImageURL(imgAvatar, shareAccounts.getAvatar(), R.mipmap.im_docter);
            txtName.setText(shareAccounts.getFullname());
        }

        private void setData(UserDiseases userDiseases) {
            imgAvatar.setVisibility(View.GONE);
            txtName.setText(userDiseases.getName());
        }
    }
}