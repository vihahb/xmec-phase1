package com.xtelsolution.xmec.adapter.mbr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.xtelsolution.xmec.R;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class MbrSpinnerAdapter extends BaseAdapter {

    private String[] list;

    private int selected = 0;

    public MbrSpinnerAdapter() {
        list = new String[] {};
    }

    public String getItemContent(int position) {
        selected = position;
        return list[selected];
    }

    public void setList(String[] list) {
        selected = 0;
        this.list = list;
        notifyDataSetChanged();
    }

    public String[] getList() {
        return list;
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spinner_relationship_dropdown, viewGroup, false);
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_relationship_dropdown, parent, false);

        CheckedTextView checkedTextView = convertView.findViewById(R.id.txt_content);

        if (selected == position) {
            checkedTextView.setChecked(true);
        } else {
            checkedTextView.setSelected(false);
        }

        checkedTextView.setText(list[position]);

        return convertView;
    }
}