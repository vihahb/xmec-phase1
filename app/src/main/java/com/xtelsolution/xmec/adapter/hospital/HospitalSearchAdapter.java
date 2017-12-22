package com.xtelsolution.xmec.adapter.hospital;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.xtelsolution.sdk.callback.OnItemClickListener;
import com.xtelsolution.sdk.utils.MapUtil;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.news.LoadHolder;
import com.xtelsolution.xmec.model.entity.Hospital;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ThanhChung on 08/11/2017.
 */

public class HospitalSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "HospitalInMapAdapter";
    private List<Hospital> list;
    private Activity context;
    private OnItemClickListener<Hospital> itemClickListener;
    private LatLng myLatlng;
    private boolean loadmore = false;
    private final int ITEM = 1;
    private final int LOAD = 2;

    public void setMyLatlng(LatLng myLatlng) {
        this.myLatlng = myLatlng;
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener<Hospital> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public HospitalSearchAdapter(List<Hospital> list, Activity context) {
        this.list = list;
        this.context = context;
    }

    public void setLoadmore(boolean loadmore) {
        this.loadmore = loadmore;
        notifyItemChanged(list.size() + 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM) {
            return new HospitaSearchlHolde(LayoutInflater.from(context).inflate(R.layout.item_hospital_search_map, parent, false));
        } else {
            return new LoadHolder(LayoutInflater.from(context).inflate(R.layout.item_load_more, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HospitaSearchlHolde) {
            final Hospital hospital = list.get(position);
            HospitaSearchlHolde h = (HospitaSearchlHolde) holder;
            if (hospital.getName() == null) {
                h.nameHospital.setVisibility(
                        View.GONE
                );
            } else {
                h.nameHospital.setText(Html.fromHtml(hospital.getName()));
                h.nameHospital.setVisibility(
                        View.VISIBLE
                );
            }

            if (hospital.getTag() != null) {
                h.tagHospital.setText(Html.fromHtml("<b>Khoa: </b>" + hospital.getTag()));
                h.tagHospital.setVisibility(View.VISIBLE);
            } else {
                h.tagHospital.setVisibility(View.GONE);
            }
            if (hospital.getAddress() != null) {
                h.addressHospital.setText(Html.fromHtml("<b>Địa chỉ: </b>" + hospital.getAddress()));
                h.addressHospital.setVisibility(View.VISIBLE);
            } else {
                h.addressHospital.setVisibility(View.GONE);
            }
            if (myLatlng != null) {
                h.tvDistance.setVisibility(View.VISIBLE);
                if (hospital.getLat() != null && hospital.getLng() != null) {
                    h.tvDistance.setText(MapUtil.calculationByDistance(myLatlng, new LatLng(hospital.getLat(), hospital.getLng())) + " km");
                } else {
                    h.tvDistance.setVisibility(View.GONE);
                }
            } else {
                h.tvDistance.setVisibility(View.GONE);
            }
            if (hospital.getImage() != null)
                Picasso.with(context).load(hospital.getImage()).into(h.imageHospital);
            else {
                h.imageHospital.setImageResource(0);
            }
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(position, hospital);
                    }
                }
            });
        } else {
            if (loadmore) {
                holder.itemView.setVisibility(View.VISIBLE);
            } else {
                holder.itemView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position < list.size() ? ITEM : LOAD;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public class HospitaSearchlHolde extends RecyclerView.ViewHolder {
        @BindView(R.id.image_hospital)
        ImageView imageHospital;
        @BindView(R.id.name_hospital)
        TextView nameHospital;
        @BindView(R.id.tag_hospital)
        TextView tagHospital;
        @BindView(R.id.address_hospital)
        TextView addressHospital;
        @BindView(R.id.tvDistance)
        TextView tvDistance;

        public HospitaSearchlHolde(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
