package com.xtelsolution.xmec.adapter.hr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.callback.NumberChangeListener;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.UserDrugImages;
import com.xtelsolution.xmec.model.entity.UserDrugs;

import java.io.File;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/26/2017
 * Email: leconglongvu@gmail.com
 */
public class DrugAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RealmList<UserDrugs> listData;

    private final int EMPTY_TYPE = 0;
    private final int ITEM_TYPE = 1;

    private long endDateLong;
    private boolean isShowAction;

    private ItemClickListener itemClickListener;

    private NumberChangeListener numberChangeListener;

    public DrugAdapter(long endDateLong) {
        this.endDateLong = endDateLong;

        listData = new RealmList<>();
        isShowAction = true;
    }

    public void setShowAction(boolean showAction) {
        isShowAction = showAction;
    }

    public void addItem(UserDrugs userDrugs) {
        listData.add(userDrugs);
        notifyDataSetChanged();

        if (numberChangeListener != null)
            numberChangeListener.onChange(listData.size());
    }

    public void addItem(RealmList<UserDrugs> list) {
        listData.addAll(list);
        notifyDataSetChanged();

        if (numberChangeListener != null)
            numberChangeListener.onChange(listData.size());
    }

    public void setListData(RealmList<UserDrugs> list) {
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();

        if (numberChangeListener != null)
            numberChangeListener.onChange(listData.size());
    }

    public void updateItem(UserDrugs userDrugs) {
        for (int i = listData.size() - 1; i >= 0; i--) {
            if (listData.get(i).getId() == userDrugs.getId()) {
                listData.remove(i);
                listData.add(i, userDrugs);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void deleteItem(UserDrugs userDrugs) {
        listData.remove(userDrugs);
        notifyDataSetChanged();

        if (numberChangeListener != null)
            numberChangeListener.onChange(listData.size());
    }

    public RealmList<UserDrugs> getListData() {
        return listData;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setNumberChangeListener(NumberChangeListener numberChangeListener) {
        this.numberChangeListener = numberChangeListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case EMPTY_TYPE:
                return new EmptyHolder(layoutInflater.inflate(R.layout.item_empty_with_guid, parent, false));
            case ITEM_TYPE:
                if (isShowAction)
                    return new ItemHolder(layoutInflater.inflate(R.layout.item_drug, parent, false));
                else
                    return new ItemHolder(layoutInflater.inflate(R.layout.item_drug_not_action, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;

            final UserDrugs userDrugs = listData.get(position);

            itemHolder.setData(position, userDrugs);

            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null)
                        itemClickListener.onItemClick(0, userDrugs);
                }
            });

            if (isShowAction)
                itemHolder.imgOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (itemClickListener != null)
                            itemClickListener.onItemClick(-1, userDrugs);
                    }
                });
        } else if (holder instanceof EmptyHolder) {
            EmptyHolder emptyHolder = (EmptyHolder) holder;

            emptyHolder.setData();
        }
    }

    @Override
    public int getItemCount() {
        return listData.size() > 0 ? listData.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (listData.size() == 0)
            return EMPTY_TYPE;
        else
            return ITEM_TYPE;
    }

    private class EmptyHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtMessage;
        private LinearLayout layout_1;

        EmptyHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_title);
            txtMessage = itemView.findViewById(R.id.txt_message);
            layout_1 = itemView.findViewById(R.id.layout_1);
        }

        private void setData() {
            txtTitle.setText(("Danh sách thuốc trống"));

            if (endDateLong > 0) {
                layout_1.setVisibility(View.GONE);
            } else {
                txtMessage.setText(("để thêm thuốc)"));
            }
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        //        private View line;
        private TextView txtName;
        private TextView txtTitle;
        private TextView txtNote;
        private ImageView imageView;
        private ImageButton imgOption;

        ItemHolder(View itemView) {
            super(itemView);

//            line = itemView.findViewById(R.id.view_1);
            txtName = itemView.findViewById(R.id.txt_name);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtNote = itemView.findViewById(R.id.txt_note);
            imageView = itemView.findViewById(R.id.img_image);

            if (isShowAction)
                imgOption = itemView.findViewById(R.id.img_option);
        }

        private void setData(int position, UserDrugs userDrugs) {
            txtName.setText(userDrugs.getDrugName());
            txtNote.setText(userDrugs.getNote());

            int visibility = TextUtils.isEmpty(userDrugs.getNote()) ? View.INVISIBLE : View.VISIBLE;
            txtTitle.setVisibility(visibility);
            txtNote.setVisibility(visibility);

            if (userDrugs.getUserDrugImages().size() > 0) {
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

            if (endDateLong > 0) {
                imgOption.setVisibility(View.INVISIBLE);
            } else {
                imgOption.setVisibility(View.VISIBLE);
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