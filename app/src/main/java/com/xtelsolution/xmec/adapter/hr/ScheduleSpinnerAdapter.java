package com.xtelsolution.xmec.adapter.hr;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xtelsolution.MyApplication;
import com.xtelsolution.xmec.R;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class ScheduleSpinnerAdapter extends BaseAdapter {
    private String[] list;

    private String selected = "";

    public ScheduleSpinnerAdapter(String[] list) {
        this.list = list;
    }

    public String getItemContent(int position) {
        selected = list[position];
        return selected;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int i) {
        return list[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spinner_per_time_normal, viewGroup, false);
        }

        TextView txtContent = view.findViewById(R.id.txt_content);
        txtContent.setText(list[i]);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

//        if (convertView == null) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_schedule_dropdown, parent, false);
//        }

        TextView textView = convertView.findViewById(R.id.txt_content);

        String content = list[position];

        if (selected.equals(content)) {
            textView.setBackgroundColor(ContextCompat.getColor(MyApplication.context, R.color.black_10));
        } else {
            textView.setBackgroundColor(Color.TRANSPARENT);
        }

        textView.setText(content);

        return convertView;
    }
}