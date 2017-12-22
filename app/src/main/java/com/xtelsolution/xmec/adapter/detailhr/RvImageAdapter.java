package com.xtelsolution.xmec.adapter.detailhr;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.HealthRecordImages;
import com.xtelsolution.xmec.model.entity.UserDrugImages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/3/2017
 * Email: leconglongvu@gmail.com
 */
public class RvImageAdapter extends RecyclerView.Adapter<RvImageAdapter.ViewHolder> {
    private List<Object> listData;

    private ItemClickListener itemClickListener;

    private int selected = 0;

    public RvImageAdapter() {
        listData = new ArrayList<>();
    }

    public void setListData(List<Object> list) {
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_hr_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;

        Object object = listData.get(pos);

        if (object instanceof HealthRecordImages)
            holder.setHealthRecordImages(pos, (HealthRecordImages) object);
        if (object instanceof UserDrugImages)
            holder.setUserDrugImages(pos, (UserDrugImages) object);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = pos;
                itemClickListener.onItemClick(pos, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout rootView;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.rootView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        private void setHealthRecordImages(int pos, HealthRecordImages healthRecordImages) {
            Drawable drawable = selected == pos ? ContextCompat.getDrawable(MyApplication.context, R.drawable.outline_square_blue_1) : null;
            rootView.setBackgroundDrawable(drawable);

            if (healthRecordImages.getImagePath() != null) {
                File file = new File(healthRecordImages.getImagePath());

                if (file.exists()) {
                    WidgetUtils.setImageFile(imageView, file, R.mipmap.ic_small_avatar_default);
                } else {
                    WidgetUtils.setImageURL(imageView, healthRecordImages.getUrlImage(), R.mipmap.ic_small_avatar_default);
                }
            } else {
                WidgetUtils.setImageURL(imageView, healthRecordImages.getUrlImage(), R.mipmap.ic_small_avatar_default);
            }
        }

        private void setUserDrugImages(int pos, UserDrugImages userDrugImages) {
            Drawable drawable = selected == pos ? ContextCompat.getDrawable(MyApplication.context, R.drawable.outline_square_blue_1) : null;
            rootView.setBackgroundDrawable(drawable);

            if (userDrugImages.getImagePath() != null) {
                File file = new File(userDrugImages.getImagePath());

                if (file.exists()) {
                    WidgetUtils.setImageFile(imageView, file, R.mipmap.ic_small_avatar_default);
                } else {
                    WidgetUtils.setImageURL(imageView, userDrugImages.getUrlImage(), R.mipmap.ic_small_avatar_default);
                }
            } else {
                WidgetUtils.setImageURL(imageView, userDrugImages.getUrlImage(), R.mipmap.ic_small_avatar_default);
            }
        }
    }
}