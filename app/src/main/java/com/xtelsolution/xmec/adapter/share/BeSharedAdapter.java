package com.xtelsolution.xmec.adapter.share;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.view.widget.RoundImage;

import io.realm.RealmList;

/**
 * Created by lecon on 12/1/2017
 */

public class BeSharedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private RealmList<Mbr> listData;

    private int errorCode = 0;

    private final int ERROR_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int ITEM_TYPE = 2;

    private ItemClickListener itemClickListener;

    public BeSharedAdapter(Context context) {
        errorCode = 0;
        this.context = context;
        checkListData();
    }

    private void checkListData() {
        if (listData == null)
            listData = new RealmList<>();
    }

    /**
     * Thêm item mới vào list data
     */
    public void addItem(Mbr item) {
        checkListData();
        errorCode = 0;

        listData.add(item);
        notifyDataSetChanged();
    }

    /**
     * Xóa item trong list data
     */
    public void removeItem(int position) {
        listData.remove(position);
        notifyDataSetChanged();
    }

    public void removeItem(Mbr shared) {
        try {
            listData.remove(shared);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật lại list data
     */
    public void setListData(RealmList<Mbr> listData) {
        checkListData();
        errorCode = 0;

        this.listData.clear();
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    /**
     * Thêm list mới vào list data
     */
    public void addListData(RealmList<Mbr> listData) {
        checkListData();
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    /**
     * Lấy item theo vị trí trong list data
     */
    public Mbr getItem(int position) {
        return listData.get(position);
    }

    /**
     * Cập nhật lại item trong list data
     */
    public void updateItem(Mbr item) {
        for (int i = listData.size() - 1; i >= 0; i--) {
            if (listData.get(i).getMrbId() == item.getMrbId()) {
                listData.remove(i);
                listData.add(i, item);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * Xóa list data
     */
    public void clearData() {
        errorCode = 0;

        listData.clear();
        notifyDataSetChanged();
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        notifyDataSetChanged();
    }

    public RealmList<Mbr> getListData() {
        return listData;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ERROR_TYPE:
                return new ErrorHolder(layoutInflater.inflate(R.layout.include_warning, parent, false));
            case EMPTY_TYPE:
                return new EmptyHolder(layoutInflater.inflate(R.layout.include_warning, parent, false));
            case ITEM_TYPE:
                return new ItemHolder(layoutInflater.inflate(R.layout.item_be_shared, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;

            Mbr item = listData.get(position);

            itemHolder.setData(item);
            itemHolder.initClickListener(item, position);
        } else if (holder instanceof EmptyHolder) {
            EmptyHolder emptyHolder = (EmptyHolder) holder;

            emptyHolder.setData();
        } else if (holder instanceof ErrorHolder) {
            ErrorHolder errorHolder = (ErrorHolder) holder;

            errorHolder.setData();
            errorHolder.initClickListener();
        }
    }

    @Override
    public int getItemCount() {
        if (listData == null)
            return 0;
        else
            return listData.size() > 0 ? listData.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (errorCode == Constants.ERROR_NO_INTERNET || errorCode == Constants.ERROR_UNKOW) {
            return ERROR_TYPE;
        } else if (listData.size() == 0)
            return EMPTY_TYPE;
        else
            return ITEM_TYPE;
    }

    private class ErrorHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ProgressBar progressBar;
        private TextView txtMessage;

        ErrorHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_message);
            progressBar = itemView.findViewById(R.id.progressBarLoading);
            txtMessage = itemView.findViewById(R.id.tv_message);
        }

        private void setData() {
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            String message;

            if (errorCode == Constants.ERROR_NO_INTERNET) {
                message = context.getString(R.string.error_no_internet_click_to_try_again);
            } else {
                message = context.getString(R.string.error_error_click_to_try_again);
            }

            txtMessage.setText(Html.fromHtml(message));
        }

        private void initClickListener() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(-2, null);
                    }
                }
            });
        }
    }

    private class EmptyHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ProgressBar progressBar;
        private TextView txtMessage;

        EmptyHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_message);
            progressBar = itemView.findViewById(R.id.progressBarLoading);
            txtMessage = itemView.findViewById(R.id.tv_message);
        }

        private void setData() {
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtMessage.setText(("Bạn chưa được chia sẻ y bạ nào"));
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private RoundImage imgMainAvatar;
        private RoundImage imgCreatorAvatar;
        private TextView txtName;
        private TextView txtDate;
        private TextView txtTime;
        private TextView txtCreator;
        private AppCompatImageButton imgDelete;

        ItemHolder(View itemView) {
            super(itemView);

            imgMainAvatar = itemView.findViewById(R.id.img_main_avatar);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtCreator = itemView.findViewById(R.id.txt_creator);
            imgCreatorAvatar = itemView.findViewById(R.id.img_avatar);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }

        private void setData(Mbr item) {
            int resourceAvatar = item.getGender() == 1 ? R.mipmap.ic_female_avatar : R.mipmap.ic_male_avatar;
            WidgetUtils.setImageURL(imgMainAvatar, item.getAvatar(), resourceAvatar);

            WidgetUtils.setImageURL(imgCreatorAvatar, item.getCreatorAvatar(), R.mipmap.ic_small_avatar_default);

            txtName.setText(item.getName());
            txtDate.setText(TimeUtils.convertLongToDate(item.getBirthdayLong()));
            txtTime.setText(TimeUtils.convertLongToDate(item.getShareDateLong()));
            txtCreator.setText(item.getCreatorName());
        }

        private void initClickListener(final Mbr item, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(position, item);
                    }
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(-1, item);
                    }
                }
            });
        }
    }
}