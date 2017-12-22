package com.xtelsolution.xmec.adapter.mbr;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.NumberChangeListener;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Friends;
import com.xtelsolution.xmec.model.resp.RESP_Friends;
import com.xtelsolution.xmec.view.widget.RoundImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class FriendSearchAdapter extends RecyclerView.Adapter<FriendSearchAdapter.ViewHolder> {

    private List<Friends> listData;

    private int max;

    private HashMap<Integer, Boolean> choose;

    private NumberChangeListener chagneListener;

    @SuppressLint("UseSparseArrays")
    public FriendSearchAdapter(int max) {
        this.max = max;

        listData = new ArrayList<>();
        choose = new HashMap<>();
    }

    public void addItem(Friends friends) {
        choose.clear();

        listData.clear();
        listData.add(friends);

        notifyDataSetChanged();
    }

    public void setListData(List<Friends> list) {
        choose.clear();

        listData.clear();
        listData.addAll(list);

        notifyDataSetChanged();
    }

    public void clearItem() {
        choose.clear();
        listData.clear();

        notifyDataSetChanged();
    }

    public RESP_Friends getListSelected(boolean isViewOnly) {
        List<Friends> list = new ArrayList<>();

        for (Friends friends : listData) {
            Boolean isSelected = choose.get(friends.getUid());
            if (isSelected != null && isSelected) {

                if (isViewOnly) {
                    friends.setShareTo(1);
                } else {
                    friends.setShareTo(2);
                }
                list.add(friends);
            }
        }

        return new RESP_Friends(list);
    }

    public RESP_Friends getListSelectedShareNow(boolean shareToNow) {
        List<Friends> list = new ArrayList<>();

        for (Friends friends : listData) {
            Boolean isSelected = choose.get(friends.getUid());
            if (isSelected != null && isSelected) {

                if (shareToNow) {
                    friends.setShareTo(1);
                } else {
                    friends.setShareTo(2);
                }
                list.add(friends);
            }
        }

        return new RESP_Friends(list);
    }

    public Friends getFriend() {
        return listData.size() > 0 ? listData.get(0) : null;
    }

    public int getChooseCount() {
        return choose.size();
    }

    public void setChagneListener(NumberChangeListener chagneListener) {
        this.chagneListener = chagneListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Friends friends = listData.get(position);

        holder.setData(friends);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ckChoose.setChecked(!holder.ckChoose.isChecked());
            }
        });

        holder.ckChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    if (choose.size() == max) {
                        holder.ckChoose.setChecked(false);
                        return;
                    }

                    choose.put(friends.getUid(), true);
                } else
                    choose.remove(friends.getUid());

                chagneListener.onChange(0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RoundImage imgAvatar;
        private TextView txtName;
        private TextView txtBirthday;
        private CheckBox ckChoose;

        ViewHolder(View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_avatar);
            txtName = itemView.findViewById(R.id.txt_name);
            txtBirthday = itemView.findViewById(R.id.txt_birthday);
            ckChoose = itemView.findViewById(R.id.ck_choose);
        }

        private void setData(Friends friends) {
            WidgetUtils.setImageURL(imgAvatar, friends.getAvatar(), R.mipmap.ic_small_avatar_default);

            txtName.setText(friends.getFullname());

            String day = friends.getBirthdayLong() != null ? TimeUtils.convertLongToDate(friends.getBirthdayLong()) : null;
            String birthday = day != null ? day : MyApplication.context.getString(R.string.layout_not_update);
            txtBirthday.setText(birthday);

            boolean isSelected = choose.get(friends.getUid()) != null ? choose.get(friends.getUid()) : false;
            ckChoose.setChecked(isSelected);
        }
    }
}