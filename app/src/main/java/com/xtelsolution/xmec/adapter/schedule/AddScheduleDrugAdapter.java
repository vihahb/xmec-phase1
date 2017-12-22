package com.xtelsolution.xmec.adapter.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.UserDrugs;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class AddScheduleDrugAdapter extends BaseAdapter {

    private List<Object> listData;

//    private int selected = 0;

    private String title;

    public AddScheduleDrugAdapter(String title) {
        listData = new ArrayList<>();
        this.title = title;
    }

    public void setListData(List<UserDrugs> list) {
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }

    public void setListStringData(List<String> list) {
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spinner_add_drug_schedule_normal, viewGroup, false);
        }

        Object object = listData.get(i);

        TextView txtTitle = view.findViewById(R.id.txt_title);
        TextView txtContent = view.findViewById(R.id.txt_content);

        txtTitle.setText(title);

        String content = null;

        if (object instanceof UserDrugs) {
            content = ((UserDrugs) object).getDrugName();
        } else if (object instanceof String) {
            content = (String) object;
        }

        txtContent.setText(content);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_relationship_dropdown, parent, false);

        CheckedTextView checkedTextView = convertView.findViewById(R.id.txt_content);

        Object object = listData.get(position);

        String content = null;

        if (object instanceof UserDrugs) {
            content = ((UserDrugs) object).getDrugName();
        } else if (object instanceof String) {
            content = (String) object;
        }

        checkedTextView.setText(content);

        return convertView;
    }
}