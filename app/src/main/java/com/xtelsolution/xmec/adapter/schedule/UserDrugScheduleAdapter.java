package com.xtelsolution.xmec.adapter.schedule;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.UserDrugImages;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.model.entity.UserDrugs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/26/2017
 * Email: leconglongvu@gmail.com
 */
public class UserDrugScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserDrugSchedule> listData;

    private String[] drugUnits;

    private ItemClickListener itemClickListener;

    private SparseArray<UserDrugs> array;

    private int showType;

    public UserDrugScheduleAdapter() {
        listData = new ArrayList<>();
        array = new SparseArray<>();

        drugUnits = MyApplication.context.getResources().getStringArray(R.array.drug_unit);
        showType = 0;
    }

    public void setShowType(int type) {
        showType = type;
    }

    public void addItem(UserDrugSchedule userDrugs) {
        listData.add(userDrugs);
        notifyDataSetChanged();
    }

    public void addItem(List<UserDrugSchedule> list) {
        listData.addAll(list);
        notifyDataSetChanged();
    }

    public void setListData(List<UserDrugSchedule> list) {
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }

    public void setListUserDrug(RealmList<UserDrugs> userDrug) {
        array.clear();

        for (UserDrugs userDrugs : userDrug) {
            array.put(userDrugs.getId(), userDrugs);
        }

        notifyDataSetChanged();
    }

    public void updateItem(UserDrugSchedule userDrugs) {
        for (int i = listData.size() - 1; i >= 0; i--) {
            if (listData.get(i).getId() == userDrugs.getId()) {
                listData.remove(i);
                listData.add(i, userDrugs);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void deleteItem(UserDrugSchedule userDrugs) {
        listData.remove(userDrugs);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        listData.remove(position);
        notifyDataSetChanged();
    }

    public List<UserDrugSchedule> getListData() {
        return listData;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (showType) {
            case 0:
                return new ItemHolder(layoutInflater.inflate(R.layout.item_user_drug_schedule_not_action, parent, false));
            case 1:
                return new ItemHolder(layoutInflater.inflate(R.layout.item_user_drug_schedule_delete, parent, false));
            case 2:
                return new ItemHolder(layoutInflater.inflate(R.layout.item_user_drug_schedule, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;

            final UserDrugSchedule userDrugSchedule = listData.get(position);

            itemHolder.setData(userDrugSchedule);

            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null)
                        itemClickListener.onItemClick(0, userDrugSchedule);
                }
            });

            if (showType == 1 || showType == 2)
                itemHolder.imgOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (itemClickListener != null)
                            itemClickListener.onItemClick(-1, userDrugSchedule);
                    }
                });

        } else if (holder instanceof EmptyHolder) {
            EmptyHolder emptyHolder = (EmptyHolder) holder;

            emptyHolder.setData();
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private class EmptyHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtMessage;

        EmptyHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_title);
            txtMessage = itemView.findViewById(R.id.txt_message);
        }

        private void setData() {
            txtTitle.setText(("Danh sách thuốc trống"));
            txtMessage.setText(("để thêm thuốc)"));
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtTitle;
        private TextView txtNote;
        private ImageView imageView;
        private ImageButton imgOption;

        ItemHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_name);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtNote = itemView.findViewById(R.id.txt_note);
            imageView = itemView.findViewById(R.id.img_image);

            if (showType == 1 || showType == 2)
                imgOption = itemView.findViewById(R.id.img_option);
        }

        private void setData(UserDrugSchedule userDrugSchedule) {
            txtName.setText(userDrugSchedule.getDrugName());
            txtTitle.setText(("Liều lượng:"));
            txtNote.setText((userDrugSchedule.getAmount() + " " + drugUnits[userDrugSchedule.getUnit()] + " / lần"));

            UserDrugs userDrugs = array.get(userDrugSchedule.getUserDrugId());

            if (userDrugs != null && userDrugs.getUserDrugImages().size() > 0) {
                imageView.setVisibility(View.VISIBLE);

                UserDrugImages userDrugImages = userDrugs.getUserDrugImages().get(0);

                if (userDrugImages.getImagePath() != null) {
                    File file = new File(userDrugImages.getImagePath());

                    if (file.exists()) {
                        setIamgeFile(file);
                    } else {
                        setImageUrl(userDrugImages.getUrlImage());
                    }
                } else {
                    setImageUrl(userDrugImages.getUrlImage());
                }
            } else {
                imageView.setVisibility(View.GONE);
            }
        }

        private void setIamgeFile(File file) {
            WidgetUtils.setImageFile(imageView, file, R.mipmap.ic_small_avatar_default, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    imageView.setVisibility(View.GONE);
                }
            });
        }

        private void setImageUrl(String url) {
            WidgetUtils.setImageURL(imageView, url, R.mipmap.ic_small_avatar_default, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    imageView.setVisibility(View.GONE);
                }
            });
        }
    }
}