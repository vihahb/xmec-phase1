package com.xtelsolution.xmec.adapter.hospital;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.HospitalTypeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ThanhChung on 12/2/17.
 */

public class SpinnerTypeHospitalAdapter extends BaseAdapter {
    private Context context;
    private List<HospitalTypeModel> list;
    private LayoutInflater inflter;
    private static final String TAG = "SpinnerTypeHospitalAdapter";

    public SpinnerTypeHospitalAdapter(Context context, List<HospitalTypeModel> list) {
        this.context = context;
        this.list = list;
        this.inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public HospitalTypeModel getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_type_hospital_spinner, null);
        ViewHolder holder;
        holder = new ViewHolder(view);
        holder.text.setText(list.get(i).getName());
        holder.image.setImageResource(list.get(i).getIcon());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.text)
        TextView text;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

