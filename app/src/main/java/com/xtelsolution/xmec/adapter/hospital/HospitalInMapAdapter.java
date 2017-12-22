package com.xtelsolution.xmec.adapter.hospital;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.xtelsolution.sdk.callback.OnItemClickListener;
import com.xtelsolution.sdk.utils.MapUtil;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Hospital;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ThanhChung on 08/11/2017.
 */

public class HospitalInMapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "HospitalInMapAdapter";
    private List<Hospital> list;
    private Context context;
    private OnItemClickListener<Hospital> itemClickListener;
    private LatLng latLng;

    public void setItemClickListener(OnItemClickListener<Hospital> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public HospitalInMapAdapter(List<Hospital> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HospitalHolde(LayoutInflater.from(context).inflate(R.layout.item_hospital_in_map, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Hospital hospital = list.get(position);
        HospitalHolde h = (HospitalHolde) holder;
        if (hospital.getName() != null) {
            h.nameHospital.setText(Html.fromHtml(hospital.getName()));
            h.nameHospital.setVisibility(View.VISIBLE);
        } else {
            h.nameHospital.setVisibility(View.GONE);
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
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onClick(position, hospital);
                }
            }
        });
        if (latLng != null && latLng.latitude > 0 && latLng.longitude > 0) {
            try {
                h.tvDistance.setText(MapUtil.calculationByDistanceKm(latLng, new LatLng(hospital.getLat(), hospital.getLng())) + " km");
                h.tvDistance.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                h.tvDistance.setVisibility(View.GONE);
            }
        } else {
            h.tvDistance.setVisibility(View.GONE);
        }
        if (hospital.getImage() != null)
            try {
                Picasso.with(context).load(hospital.getImage()).placeholder(MapUtil.getIconMipmapCircleHospital(hospital.getType())).error(MapUtil.getIconMipmapCircleHospital(hospital.getType())).into(h.imageHospital);
            } catch (Exception e) {
                e.printStackTrace();
            }
        else {
            h.imageHospital.setImageResource(MapUtil.getIconMipmapCircleHospital(hospital.getType()));
        }
        h.typeHospital.setText(MapUtil.getNameTypeHospitalByType(hospital.getType()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setMylocation(LatLng latLng) {

        this.latLng = latLng;
        notifyDataSetChanged();
    }

    public class HospitalHolde extends RecyclerView.ViewHolder {
        @BindView(R.id.image_hospital)
        CircularImageView imageHospital;
        @BindView(R.id.name_hospital)
        TextView nameHospital;
        @BindView(R.id.tag_hospital)
        TextView tagHospital;
        @BindView(R.id.address_hospital)
        TextView addressHospital;
        @BindView(R.id.type_hospital)
        TextView typeHospital;
        @BindView(R.id.tvDistance)
        TextView tvDistance;

        public HospitalHolde(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
