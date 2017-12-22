package com.xtelsolution.xmec.view.activity.disease;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.JsoupDiseaseDetail;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vivu on 11/22/17.
 */

public class AdapterDiseaseDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<JsoupDiseaseDetail> diseaseDetail;
    Context context;

    public AdapterDiseaseDetail(List<JsoupDiseaseDetail> diseaseDetail, Context context) {
        this.diseaseDetail = diseaseDetail;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drug_info, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder h = (ViewHolder) holder;
            if (position < 2) {
                h.setData(diseaseDetail.get(position), true);
            } else {
                h.setData(diseaseDetail.get(position), false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return diseaseDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_type)
        ImageView imgType;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.des_nav)
        ImageView desNav;
        @BindView(R.id.tv_content_ex)
        TextView tvContentEx;
        @BindView(R.id.ex_content)
        ExpandableLayout exContent;

        @OnClick(R.id.img_type)
        public void clickImg(){
            actionExpand();
        }
        @OnClick(R.id.tv_title)
        public void clickTitle(){
            actionExpand();
        }

        @OnClick(R.id.des_nav)
        public void clickNav() {
            actionExpand();
        }

        private void actionExpand() {
            exContent.toggle(true);
            if (isExpand()) {
                desNav.setRotation(90);
            } else {
                desNav.setRotation(0);
            }
        }

        private boolean isExpand() {
            if (exContent.isExpanded()) {
                return true;
            } else {
                return false;
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(JsoupDiseaseDetail data, boolean isExpand) {
            if (data != null) {
                tvTitle.setText(Html.fromHtml(data.getDescrip_title()));
                tvContentEx.setText(Html.fromHtml(data.getContent()));
                if (isExpand) {
                    exContent.expand();
                } else {
                    exContent.collapse(true);
                }

                if (isExpand()) {
                    desNav.setRotation(90);
                } else {
                    desNav.setRotation(0);
                }

                /**
                 * namealiases:  tên khác
                 * images :     hình ảnh ít hoặc nh
                 * desc:        tóm tắt
                 * overview:    tổng quan
                 * reason:      nguyên nhân
                 * prevent:     phòng chống, phòng ngừa
                 * treatment:   điều trị
                 */

                switch (data.getTitle()) {
                    case "name":
                        setIcon(R.mipmap.ic_search_disease);
                        break;
                    case "nameAliases":
                        setIcon(R.mipmap.ic_search_disease);
                        break;
                    case "desc":
                        setIcon(R.mipmap.ic_disease_tomtat);
                        break;
                    case "overview":
                        setIcon(R.mipmap.ic_disease_tongquan);
                        break;
                    case "reason":
                        setIcon(R.mipmap.ic_disease_nguyennhan);
                        break;
                    case "prevent":
                        setIcon(R.mipmap.ic_disease_phongngua);
                        break;
                    case "treatment":
                        setIcon(R.mipmap.ic_disease_dieu_tri);
                        break;
                }
            }
        }

        public void setIcon(int resource) {
            if (resource != -1)
                imgType.setImageResource(resource);
        }
    }

}
