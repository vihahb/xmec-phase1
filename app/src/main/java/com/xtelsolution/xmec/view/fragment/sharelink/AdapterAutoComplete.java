package com.xtelsolution.xmec.view.fragment.sharelink;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.database.GetContainListByKeyModel;
import com.xtelsolution.xmec.model.entity.Contact_Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by vivu on 11/24/17
 */

public class AdapterAutoComplete extends BaseAdapter implements Filterable {

    private static final String TAG = "AdapterAutoComplete";

    private List<Contact_Model> list;
    private List<Contact_Model> suggestion;
    private Context context;
    private Filter numberFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestion.clear();
                new GetContainListByKeyModel<Contact_Model>(Contact_Model.class, "contactMobile", charSequence.toString()) {
                    @Override
                    protected void onSuccess(RealmList<Contact_Model> realmList) {
                        suggestion.addAll(realmList);
                        Log.e(TAG, "performFiltering realmList: " + Arrays.toString(realmList.toArray()));
                    }
                };

//                for (Contact_Model model : tempItem) {
//
//
//
//                    if (model.getContactMobile().contains(charSequence.toString())) {
//                        suggestion.add(model);
//                        Log.e(TAG, "Suggestion mobile: " + model);
//                    }
//                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestion;
                filterResults.count = suggestion.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            List<Contact_Model> filterList = (List<Contact_Model>) filterResults.values;
            if (filterResults.count > 0) {
                list.clear();
                for (Contact_Model model : filterList) {
                    list.add(model);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public AdapterAutoComplete(Context context, List<Contact_Model> list) {
        this.list = list;
        this.context = context;
        suggestion = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position).getContactMobile();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        View view_inf = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_autocomplete_contact, viewGroup, false);
        if (view_inf != null) {
            TextView contact_name = view_inf.findViewById(R.id.contact_name);
            TextView contact_number = view_inf.findViewById(R.id.contact_number);
            contact_name.setText(list.get(position).getContactName());
            contact_number.setText(list.get(position).getContactMobile());
            view_inf.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }
        viewGroup.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        return view_inf;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view_inf = LayoutInflater.from(convertView.getContext()).inflate(R.layout.item_autocomplete_contact, parent, false);
        if (view_inf != null) {
            TextView contact_name = view_inf.findViewById(R.id.contact_name);
            TextView contact_number = view_inf.findViewById(R.id.contact_number);
            contact_name.setText(list.get(position).getContactName());
            if (list.get(position).getContactHome() != null) {
                contact_number.setText(list.get(position).getContactHome());
            } else if (list.get(position).getContactMobile() != null) {
                contact_number.setText(list.get(position).getContactMobile());
            } else if (list.get(position).getContactWork() != null) {
                contact_number.setText(list.get(position).getContactWork());
            }
            view_inf.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }
        parent.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        return view_inf;
    }

    @Override
    public Filter getFilter() {
        return numberFilter;
    }
}
