package com.xtelsolution.xmec.adapter.hr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Drug;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/25/2017
 * Email: leconglongvu@gmail.com
 */
public class SearchDrugAdapter extends ArrayAdapter<Drug> {
    private final String MY_DEBUG_TAG = "HospitalAdapter";
    private List<Drug> items;
    private List<Drug> itemsAll;
    private List<Drug> suggestions;

    public SearchDrugAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.items = new ArrayList<>();
        this.itemsAll = new ArrayList<>();
        this.suggestions = new ArrayList<>();
    }

    public void clearData() {
        items.clear();
        itemsAll.clear();
        suggestions.clear();
        notifyDataSetChanged();
    }

    public void setListData(List<Drug> list) {
        items.clear();
        itemsAll.clear();
        suggestions.clear();

        items.addAll(list);
        itemsAll.addAll(list);
        notifyDataSetChanged();
    }

    @SuppressLint("InflateParams")
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_spinner_black_normal, null);
        }

        Drug hospital;

        try {
            hospital = items.get(position);
        } catch (Exception e) {
            return v;
        }

        if (hospital != null) {
            TextView customerNameLabel = v.findViewById(R.id.txt_title);
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView Hospital Name:"+customer.getName());
                customerNameLabel.setText(hospital.getTenThuoc());
            }
        }
        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((Drug)(resultValue)).getTenThuoc();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                String key = TextUtils.unicodeToKoDauLowerCase(constraint.toString());

                suggestions.clear();
                for (Drug hospital : itemsAll) {
                    String value = TextUtils.unicodeToKoDauLowerCase(hospital.getTenThuoc());
                    if(value.contains(key)){
                        suggestions.add(hospital);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//            List<Drug> filteredList = (List<Drug>) results.values;
//            if(results.count > 0) {
                clear();
                try {
                    for (Drug c : suggestions) {
                        add(c);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
//            }
        }
    };
}