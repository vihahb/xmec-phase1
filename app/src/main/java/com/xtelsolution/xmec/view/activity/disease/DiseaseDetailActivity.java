package com.xtelsolution.xmec.view.activity.disease;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.DiseaseObject;
import com.xtelsolution.xmec.model.entity.JsoupDiseaseDetail;
import com.xtelsolution.xmec.presenter.disease.DiseaseDetailPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.widget.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiseaseDetailActivity extends BasicActivity implements IDiseaseDetailView {

    BaseRecyclerView baseRecyclerView;
    List<JsoupDiseaseDetail> list;
    AdapterDiseaseDetail adapter;
    DiseaseDetailPresenter presenter;
    @BindView(R.id.tv_name_disease)
    TextView tvNameDisease;
    private DiseaseObject diseaseObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
        ButterKnife.bind(this);
        baseRecyclerView = new BaseRecyclerView(getWindow().getDecorView().getRootView());
        list = new ArrayList<>();
        adapter = new AdapterDiseaseDetail(list, getApplicationContext());
        baseRecyclerView.getRecyclerView().setAdapter(adapter);
        presenter = new DiseaseDetailPresenter(this);
        initToolbar(R.id.toolbar, "");
        getData();
    }

    private void getData() {
        diseaseObject = (DiseaseObject) getIntent().getSerializableExtra(Constants.OBJECT);
        if (!TextUtils.isEmpty(diseaseObject.getName())){
            initToolbar(R.id.toolbar, diseaseObject.getName());
            tvNameDisease.setText(diseaseObject.getName());
        }
        baseRecyclerView.loading();
        presenter.getDiseaseById(diseaseObject.getId());
    }

    private void setData(DiseaseObject disease) {
        if (disease != null) {
            initToolbar(R.id.toolbar, disease.getName());
            tvNameDisease.setText(disease.getName());
//            if (disease.getName() != null) {
//                list.add(new JsoupDiseaseDetail("name", disease.getName(), "Tên bệnh"));
//            }

            if (disease.getNameAliases() != null) {
                list.add(new JsoupDiseaseDetail("nameAliases", disease.getNameAliases().replace("|", "<br>"), "Tên gọi khác của bệnh"));
            }

            if (disease.getDesc() != null) {
                list.add(new JsoupDiseaseDetail("desc", disease.getDesc(), "Tóm tắt bệnh " + disease.getName()));
            }

            if (disease.getOverview() != null) {
                list.add(new JsoupDiseaseDetail("overview", disease.getOverview(), "Tổng quan về bệnh " + disease.getName()));
            }

            if (disease.getReason() != null) {
                list.add(new JsoupDiseaseDetail("reason", disease.getReason().replace("<li>", "").replace("</li>", ""), "Nguyên nhân bệnh " + disease.getName()));
            }

            if (disease.getPrevent() != null) {
                list.add(new JsoupDiseaseDetail("prevent", disease.getPrevent().replace("<li>", "").replace("</li>", ""), "Phòng ngừa bệnh " + disease.getName()));
            }

            if (disease.getTreatment() != null) {
                list.add(new JsoupDiseaseDetail("treatment", disease.getTreatment(), "Điều trị bệnh " + disease.getName()));
            }
        }
        adapter.notifyDataSetChanged();
        baseRecyclerView.showData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getDiseaseSuccess(final DiseaseObject object) {
        if (object != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setData(object);
                }
            }, 1000);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
