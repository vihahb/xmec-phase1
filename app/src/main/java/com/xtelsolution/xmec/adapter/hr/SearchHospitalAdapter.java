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
import com.xtelsolution.xmec.model.entity.Hospital;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/25/2017
 * Email: leconglongvu@gmail.com
 */
public class SearchHospitalAdapter extends ArrayAdapter<Hospital> {
    private List<Hospital> items;
    private List<Hospital> itemsAll;
    private List<Hospital> suggestions;

    public SearchHospitalAdapter(@NonNull Context context, @LayoutRes int resource) {
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

    public void setListData(List<Hospital> list) {
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

        Hospital hospital;

        try {
            hospital = items.get(position);
        } catch (Exception e) {
            return v;
        }

        if (hospital != null) {
            TextView customerNameLabel = v.findViewById(R.id.txt_title);
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView Hospital Name:"+customer.getName());
                customerNameLabel.setText(hospital.getName());
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
            return ((Hospital) (resultValue)).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                String key = TextUtils.unicodeToKoDauLowerCase(constraint.toString());

                suggestions.clear();
                for (Hospital hospital : itemsAll) {
                    String value = TextUtils.unicodeToKoDauLowerCase(hospital.getName());
                    if (value.contains(key)) {
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
//            List<Hospital> filteredList = (List<Hospital>) results.values;
//            if(results.count > 0) {
            clear();
            try {
                for (Hospital c : suggestions) {
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