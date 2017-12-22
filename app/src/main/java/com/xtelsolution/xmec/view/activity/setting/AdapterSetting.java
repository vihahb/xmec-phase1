package com.xtelsolution.xmec.view.activity.setting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.OnItemClickListener;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.SettingObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vivu on 12/12/17
 * xtel.vn
 */
public class AdapterSetting extends
        RecyclerView.Adapter<AdapterSetting.ViewHolder> {

    private static final String TAG = AdapterSetting.class.getSimpleName();

    private Context context;
    private List<SettingObject> list;
    private OnItemClickListener<SettingObject> onItemClickListener;

    public AdapterSetting(Context context,
                          OnItemClickListener<SettingObject> onItemClickListener) {
        this.context = context;
        this.list = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public void synchronizedCollection(List<SettingObject> settingList) {
        if (list.size() > 0){
            list.clear();
        }
        list.addAll(settingList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Todo Butterknife bindings
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final SettingObject model,
                         final OnItemClickListener<SettingObject> listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getLayoutPosition(), model);
                }
            });

            if (model.getResource() != -1){
                imgIcon.setImageResource(model.getResource());
            }
            if (model.getName()!=null){
                tvName.setText(model.getName());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_setting, parent, false);
        ButterKnife.bind(this, view);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SettingObject item = list.get(position);
        //Todo: Setup viewholder for item 
        holder.bind(item, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}