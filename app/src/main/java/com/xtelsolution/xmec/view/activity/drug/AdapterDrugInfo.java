package com.xtelsolution.xmec.view.activity.drug;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.InfoDrug;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xtel on 11/17/17.
 */

public class AdapterDrugInfo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<InfoDrug> list;
    Context context;

    public AdapterDrugInfo(List<InfoDrug> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_drug_info, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.setData(list.get(position), position < 2);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_type)
        ImageView imgType;

        @OnClick(R.id.img_type)
        public void clickImg() {
            actionExpand();
        }

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @OnClick(R.id.tv_title)
        public void click() {
            actionExpand();
        }

        @BindView(R.id.des_nav)
        ImageView desNav;

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

        @BindView(R.id.tv_content_ex)
        TextView tvContentEx;
        @BindView(R.id.ex_content)
        ExpandableLayout exContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(InfoDrug data, boolean isFirst) {
            if (data != null) {
                tvTitle.setText(Html.fromHtml(data.getTitle()));
                tvContentEx.setText(Html.fromHtml(data.getContent()));
                if (isFirst) {
                    exContent.expand();
                } else {
                    exContent.collapse(true);
                }
                if (isExpand()) {
                    desNav.setRotation(90);
                } else {
                    desNav.setRotation(0);
                }

                if (data.getTitle().contains("Tóm tắt")) {
                    setIcon(R.mipmap.ic_summary);
                }

                if (data.getTitle().contains("Hướng dẫn")) {
                    setIcon(R.mipmap.ic_drug_huongdan);
                }

                if (data.getTitle().contains("Cảnh báo")) {
                    setIcon(R.mipmap.ic_drug_canhbao);
                }

                if (data.getTitle().contains("Chỉ định")) {
                    setIcon(R.mipmap.ic_drug_chidinh);
                }

                if (data.getTitle().contains("Chống chỉ định")) {
                    setIcon(R.mipmap.ic_drug_phongngua);
                }

                if (data.getTitle().contains("Tác dụng phụ")) {
                    setIcon(R.mipmap.ic_tac_dung_phu);
                }

                if (data.getTitle().contains("Lưu ý")) {
                    setIcon(R.mipmap.ic_note);
                }

                if (data.getTitle().contains("Quá liều")) {
                    setIcon(R.mipmap.ic_qua_lieu);
                }

                if (data.getTitle().contains("Bảo quản")) {
                    setIcon(R.mipmap.ic_bao_quan);
                }

                if (data.getTitle().contains("Tương tác")) {
                    setIcon(R.mipmap.ic_tuong_tac);
                }

                if (data.getTitle().contains("Chế độ ăn uống")) {
                    setIcon(R.mipmap.ic_chedo_an_uong);
                }

                if (data.getTitle().contains("Dược lý")) {
                    setIcon(R.mipmap.ic_duoc_li_co_che);
                }

                if (data.getTitle().contains("Dược động")) {
                    setIcon(R.mipmap.ic_duoc_dong_hoc);
                }

                if (data.getTitle().contains("Nếu quên")) {
                    setIcon(R.mipmap.ic_quen);
                }
            }
        }

        public void setIcon(int resource) {
            if (resource != -1)
                imgType.setImageResource(resource);
        }
    }

}
