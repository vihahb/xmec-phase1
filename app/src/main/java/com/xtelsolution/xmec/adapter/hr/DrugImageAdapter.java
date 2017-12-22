package com.xtelsolution.xmec.adapter.hr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.UserDrugImages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/24/2017
 * Email: leconglongvu@gmail.com
 */
public class DrugImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserDrugImages> listData;

    private final int ADD_TYPE = 0;
    private final int ITEM_TYPE = 1;

    private ItemClickListener itemClickListener;

    public DrugImageAdapter() {
        listData = new ArrayList<>();
    }

    private void deleteItem(int position) {
        listData.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(UserDrugImages userDrugImages) {
        listData.add(userDrugImages);
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ADD_TYPE:
                return new AddHolder(layoutInflater.inflate(R.layout.item_hr_image_add, parent, false));
            case ITEM_TYPE:
                return new ImageHolder(layoutInflater.inflate(R.layout.item_hr_image, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddHolder) {
            AddHolder addHolder = (AddHolder) holder;

            addHolder.imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(-1, null);
                }
            });
        } else if (holder instanceof ImageHolder) {
            ImageHolder imageHolder = (ImageHolder) holder;

            final int realPosition = position - 1;

            imageHolder.setData(listData.get(realPosition));

            imageHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(realPosition);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return ADD_TYPE;
        else
            return ITEM_TYPE;
    }

    private class AddHolder extends RecyclerView.ViewHolder {
        private ImageButton imgAdd;

        AddHolder(View itemView) {
            super(itemView);

            imgAdd = itemView.findViewById(R.id.img_add);
        }
    }

    private class ImageHolder extends RecyclerView.ViewHolder {
        private ImageView imgLogo;
        private ImageButton imgDelete;

        ImageHolder(View itemView) {
            super(itemView);

            imgLogo = itemView.findViewById(R.id.img_logo);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }

        private void setData(UserDrugImages userDrugImages) {
            if (userDrugImages.getImagePath() != null) {
                File file = new File(userDrugImages.getImagePath());

                if (file.exists()) {
                    WidgetUtils.setSmallImageFile(imgLogo, file, R.mipmap.ic_small_avatar_default);
                } else {
                    WidgetUtils.setImageURL(imgLogo, userDrugImages.getUrlImage(), R.mipmap.ic_small_avatar_default);
                }
            } else {
                WidgetUtils.setImageURL(imgLogo, userDrugImages.getUrlImage(), R.mipmap.ic_small_avatar_default);
            }
        }
    }
}