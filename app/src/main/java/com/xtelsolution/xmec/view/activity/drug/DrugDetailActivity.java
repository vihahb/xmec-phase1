package com.xtelsolution.xmec.view.activity.drug;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.ReloadListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.DrugSearchEntity;
import com.xtelsolution.xmec.model.entity.InfoDrug;
import com.xtelsolution.xmec.presenter.drug.DrugDetailPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.widget.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrugDetailActivity extends BasicActivity implements IDrugInfoView {
    DrugSearchEntity drug;
    BaseRecyclerView myList;
    AdapterDrugInfo adapter;
    List<InfoDrug> list;
    DrugDetailPresenter presenter;
    @BindView(R.id.tv_name_drug)
    TextView tv_name_drug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_detail);
        ButterKnife.bind(this);
        presenter = new DrugDetailPresenter(this);
        initToolbar(R.id.toolbar, "Loading...");
        myList = new BaseRecyclerView(getWindow().getDecorView().getRootView());
        list = new ArrayList<>();
        adapter = new AdapterDrugInfo(list, getApplicationContext());
        myList.getRecyclerView().setAdapter(adapter);
        getData();
    }

    private void getData() {
        drug = (DrugSearchEntity) getIntent().getSerializableExtra(Constants.OBJECT);
//        presenter.getContent("https://vicare.vn/thuoc/ventolin-nebules-01-1392/");
        presenter.getDrugById(drug.getId());
        myList.loading();
        setDataDrug(drug);
    }

    private void setDataDrug(DrugSearchEntity drug) {
        initToolbar(R.id.toolbar, drug.getDrugName());
        tv_name_drug.setText(drug.getDrugName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getCollectionSucces(List<InfoDrug> list) {
        Log.e("AAAA", "getCollectionSucces: " + list.toString());
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
        myList.showData();
    }

    @Override
    public Context getActivity() {
        return this;
    }

    @Override
    public void showWarning(String s) {
        myList.showError(s);
        myList.clickReload(new ReloadListener() {
            @Override
            public void OnClick() {
                presenter.getDrugById(drug.getId());
            }
        });
    }

    @Override
    public void showLoading() {
        myList.loading();
    }

    @Override
    public void getDrugSuccess(List<InfoDrug> infoDrugList) {
        Log.e("Drug Info", "getCollectionSucces: " + list.toString());
        this.list.addAll(infoDrugList);
        adapter.notifyDataSetChanged();
        myList.showData();
    }
}
