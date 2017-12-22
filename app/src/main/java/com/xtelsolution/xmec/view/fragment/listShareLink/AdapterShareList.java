package com.xtelsolution.xmec.view.fragment.listShareLink;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.ShareMbrEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xtel on 11/14/17
 */
public class AdapterShareList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ShareMbrEntity> list;
    Context context;

    IAdapterListView view;
    private int state;

    public AdapterShareList(Context context, IAdapterListView view, int state) {
        this.list = new ArrayList<>();
        this.context = context;
        this.view = view;
        this.state = state;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_share_link, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ShareMbrEntity entity = list.get(position);
            ViewHolder holder_view = (ViewHolder) holder;
            holder_view.setData(position, entity);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<ShareMbrEntity> data) {
        if (list.size() > 0) {
            list.clear();
        }
        list.addAll(data);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, list.size());
    }

    public void changeDataPosition(int position, ShareMbrEntity shareUpdate) {
        if (shareUpdate != null) {
            list.get(position).setShareTo(shareUpdate.getShareTo());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_avatar)
        ImageView img_avatar;
        @BindView(R.id.img_edit)
        ImageButton img_edit;
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_birthday)
        TextView txt_sharehDay;
        @BindView(R.id.view_status)
        View viewState;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position, ShareMbrEntity data) {
            if (data != null) {
                if (!TextUtils.isEmpty(data.getAvatar())) {
                    WidgetUtils.setImageURL(img_avatar, data.getAvatar(), R.mipmap.ic_small_avatar_default);
                }

                txt_name.setText(data.getFullname());
                String day = data.getShareDateLong() != null ? TimeUtils.convertLongToDate(data.getShareDateLong()) : null;
                String shareDay = day != null ? day : MyApplication.context.getString(R.string.layout_not_update);
                txt_sharehDay.setText(shareDay);
            }

            if (state == 0)
                initOnClick(position, data);
            else
                img_edit.setVisibility(View.GONE);

            int drawable = R.drawable.background_circle_blue;

            switch (data.getShareState()) {
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

            viewState.setBackground(ContextCompat.getDrawable(itemView.getContext(), drawable));
        }

        private void initOnClick(final int position, final ShareMbrEntity data) {
            img_edit.setVisibility(View.VISIBLE);

            img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.onClickItem(position, data);
                }
            });
        }
    }
}