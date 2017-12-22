package com.xtelsolution.xmec.adapter.hr;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.HealthRecords;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/20/2017
 * Email: leconglongvu@gmail.com
 */
public class HealthRecordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HealthRecords> listData= new ArrayList<>();

    private boolean isViewOnly= false;
    private int error = 0;

    private final int ERROR_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int ITEM_TYPE = 2;

    private ItemClickListener itemClickListener;

    public HealthRecordsAdapter() {

    }

    public void setListData(List<HealthRecords> list) {
        error = 0;

        listData.clear();

        if (list != null) {
            listData.addAll(list);
        }

        notifyDataSetChanged();
    }

    public void addItem(HealthRecords healthRecords) {
        error = 0;

        listData.add(healthRecords);
        notifyDataSetChanged();
    }

    public void updateItem(HealthRecords healthRecords) {
        for (int i = listData.size() - 1; i >= 0; i--) {
            if (listData.get(i).getId() == healthRecords.getId()) {
                listData.remove(i);
                listData.add(i, healthRecords);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void setError(int error) {
        this.error = error;
        listData.clear();
        notifyDataSetChanged();
    }

    public void setOnly() {
        this.error = Constants.ERROR_VIEW_ONLY;
        notifyDataSetChanged();
    }

    public void setViewOnly(boolean viewOnly) {
        isViewOnly = viewOnly;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void deleteItem(HealthRecords healthRecords) {
        listData.remove(healthRecords);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ERROR_TYPE:
                return new ErrorHolder(layoutInflater.inflate(R.layout.item_empty, parent, false));
            case EMPTY_TYPE:
                return new EmptyHolder(layoutInflater.inflate(R.layout.item_healthy_records_empty, parent, false));
            case ITEM_TYPE:
                return new ItemHolder(layoutInflater.inflate(R.layout.item_healthy_records, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof ItemHolder) {
            final ItemHolder itemHolder = (ItemHolder) holder;

            final HealthRecords healthRecords = listData.get(position);

            itemHolder.setData(healthRecords);

            itemHolder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(position, healthRecords));

            itemHolder.imgMenu.setOnClickListener(view -> {
                if (healthRecords.getEndDateLong() > 0) {
                    itemClickListener.onItemClick(-5, healthRecords);
                } else
                    itemClickListener.onItemClick(-4, healthRecords);
            });
        } else if (holder instanceof ErrorHolder) {
            ErrorHolder errorHolder = (ErrorHolder) holder;

            errorHolder.setData();

            errorHolder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(error, null));
        } else if (holder instanceof EmptyHolder) {
            EmptyHolder emptyHolder = (EmptyHolder) holder;
            emptyHolder.setData();
        }
    }

    @Override
    public int getItemCount() {
        if (error == Constants.ERROR_NO_INTERNET || error == Constants.ERROR_UNKOW)
            return 1;
        else
            return listData.size() > 0 ? listData.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (error == Constants.ERROR_NO_INTERNET || error == Constants.ERROR_UNKOW)
            return ERROR_TYPE;
        else if (listData.size() == 0)
            return EMPTY_TYPE;
        else
            return ITEM_TYPE;
    }

    private class ErrorHolder extends RecyclerView.ViewHolder {
        private TextView txtMessage;

        ErrorHolder(View itemView) {
            super(itemView);

            txtMessage = itemView.findViewById(R.id.txt_message);
        }

        private void setData() {
            String message;

            if (error == Constants.ERROR_NO_INTERNET) {
                message = "Không có kết nốt internet" +
                        "\nChạm vào đây để thử lại";
            } else {
                message = "Có lỗi xảy ra" +
                        "\nChạm vào đây để thử lại";
            }

            txtMessage.setText(message);
        }
    }

    private class EmptyHolder extends RecyclerView.ViewHolder {
        private LinearLayout rootView;

        EmptyHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.layout_1);
        }

        private void setData() {
            if (isViewOnly) {
                rootView.setVisibility(View.GONE);
            } else {
                rootView.setVisibility(View.VISIBLE);
            }
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo;
        TextView txtTitle;
        TextView txtContent;
        TextView txtDate;
        ImageButton imgMenu;

        ItemHolder(View itemView) {
            super(itemView);

            imgLogo = itemView.findViewById(R.id.img_logo);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtContent = itemView.findViewById(R.id.txt_content);
            txtDate = itemView.findViewById(R.id.txt_date);
            imgMenu = itemView.findViewById(R.id.img_menu);
        }

        private void setData(HealthRecords healthRecords) {
            txtTitle.setText(healthRecords.getName());

            StringBuilder disease;
            if (healthRecords.getUserDiseases().size() > 0) {
                disease = new StringBuilder(healthRecords.getUserDiseases().get(0).getName());

                for (int i = 1; i < healthRecords.getUserDiseases().size(); i++) {
                    disease.append(", ").append(healthRecords.getUserDiseases().get(i).getName());
                }

                txtContent.setVisibility(View.VISIBLE);
                txtContent.setText(disease.toString());
            } else {
                txtContent.setVisibility(View.GONE);
            }

            String date = TimeUtils.convertLongToDate(healthRecords.getDateCreateLong());
            date = date != null ? date : MyApplication.context.getString(R.string.layout_not_update);

            String dateCreate = MyApplication.context.getString(R.string.layout_hr_start) + " " + date;

            if (healthRecords.getEndDateLong() > 0) {
                dateCreate += " - <font color='#FF0000'>" +  MyApplication.context.getString(R.string.layout_hr_end) +
                        " " + TimeUtils.convertLongToDate(healthRecords.getEndDateLong()) + "</font>";
            }

            txtDate.setText(Html.fromHtml(dateCreate));

            int resource;

            if (healthRecords.getEndDateLong() == 0) {
                resource = R.mipmap.ic_hr_noti_36;
            } else {
//                resource = R.mipmap.ic_hr_not_noti_36;
                resource = R.mipmap.ic_hr_done;
            }

            imgLogo.setImageResource(resource);

            if (isViewOnly) {
                imgMenu.setVisibility(View.GONE);
            } else {
                imgMenu.setVisibility(View.VISIBLE);
            }
        }
    }
}