package com.xtelsolution.xmec.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.callback.NumberChangeListener;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.HealthRecordImages;
import com.xtelsolution.xmec.model.entity.UserDrugImages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/24/2017
 * Email: leconglongvu@gmail.com
 */
public class AddImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> listData;

    private NumberChangeListener numberChangeListener;

    private ItemClickListener itemClickListener;

    private boolean isViewOnly;

    private final int ADD_TYPE = 0;
    private final int ITEM_TYPE = 1;

    public AddImageAdapter() {
        listData = new ArrayList<>();
        isViewOnly = false;
    }

    public AddImageAdapter(boolean isViewOnly) {
        listData = new ArrayList<>();
        this.isViewOnly = isViewOnly;
    }

    private void deleteItem(int position) {
        listData.remove(position);
        notifyDataSetChanged();

        if (numberChangeListener != null)
            numberChangeListener.onChange(listData.size());
    }

    public void addItem(Object object) {
        listData.add(0, object);
        notifyDataSetChanged();

        if (numberChangeListener != null)
            numberChangeListener.onChange(listData.size());
    }

    public void setListDrugData(List<UserDrugImages> list) {
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();

        if (numberChangeListener != null)
            numberChangeListener.onChange(listData.size());
    }

    public void setListHrData(List<HealthRecordImages> list) {
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();

        if (numberChangeListener != null)
            numberChangeListener.onChange(listData.size());
    }

    public List<Object> getListData() {
        return listData;
    }

    public void setNumberChangeListener(NumberChangeListener numberChangeListener) {
        this.numberChangeListener = numberChangeListener;
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

            final int realPosition = isViewOnly ? position : position - 1;

            final Object object = listData.get(realPosition);

            if (object instanceof HealthRecordImages) {
                HealthRecordImages healthRecordImages = (HealthRecordImages) object;
                imageHolder.setData(healthRecordImages);
            } else if (object instanceof UserDrugImages) {
                UserDrugImages userDrugImages = (UserDrugImages) object;
                imageHolder.setData(userDrugImages);
            }

            imageHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null)
                        itemClickListener.onItemClick(realPosition, object);
                }
            });

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
        if (isViewOnly)
            return listData.size();
        else
            return listData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isViewOnly)
            return ITEM_TYPE;
        else if (position == 0)
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

        private void checkView() {
            if (isViewOnly)
                imgDelete.setVisibility(View.GONE);
            else
                imgDelete.setVisibility(View.VISIBLE);
        }

        private void setData(HealthRecordImages healthRecordImages) {
            checkView();

            if (healthRecordImages.getImagePath() != null) {
                File file = new File(healthRecordImages.getImagePath());

                if (file.exists()) {
                    WidgetUtils.setSmallImageFile(imgLogo, file, R.mipmap.ic_small_avatar_default);
                } else {
                    WidgetUtils.setImageURL(imgLogo, healthRecordImages.getUrlImage(), R.mipmap.ic_small_avatar_default);
                }
            } else {
                WidgetUtils.setImageURL(imgLogo, healthRecordImages.getUrlImage(), R.mipmap.ic_small_avatar_default);
            }
        }

        private void setData(UserDrugImages userDrugImages) {
            checkView();

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