package com.xtelsolution.xmec.view.activity.hospitalInfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.RatingObject;
import com.xtelsolution.xmec.view.widget.RoundImage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by vivu on 11/28/17
 * xtel.vn
 */
public class AdapterRateList extends
        RecyclerView.Adapter<AdapterRateList.ViewHolder> {

    private static final String TAG = AdapterRateList.class.getSimpleName();

    private Context context;
    private List<RatingObject> list;
    private OnItemClickListener onItemClickListener;

    public AdapterRateList(Context context, List<RatingObject> list,
                           OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Todo Butterknife bindings
        @BindView(R.id.img_avatar_rate)
        RoundImage imgAvatarRate;
        @BindView(R.id.tv_name_rate)
        TextView tvNameRate;
        @BindView(R.id.tv_time_rate)
        TextView tvTimeRate;
        @BindView(R.id.rt_user_rate)
        MaterialRatingBar rtUserRate;
        @BindView(R.id.tv_content_rate)
        TextView tvContentRate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final RatingObject model,
                         final OnItemClickListener listener) {

            tvNameRate.setText(model.getFullname());
            tvTimeRate.setText(TimeUtils.convertLongToDate(model.getDateCreatedLong()));
            tvContentRate.setText(model.getComment());

            float total_rate = (model.getHygieneRate() + model.getQualityRate() + model.getServiceRate()) / 3;

            rtUserRate.setRating(total_rate);

            if (!TextUtils.isEmpty(model.getAvatar())) {
                WidgetUtils.setImageURL(imgAvatarRate, model.getAvatar(), R.mipmap.ic_small_avatar_default);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getLayoutPosition());

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_rating, parent, false);
        ButterKnife.bind(this, view);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RatingObject item = list.get(position);
        //Todo: Setup viewholder for item
        if (item != null) {
            holder.bind(item, onItemClickListener);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}