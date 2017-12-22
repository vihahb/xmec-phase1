package com.xtelsolution.xmec.view.activity.share;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.FragmentsAdapter;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.presenter.share.EditSharePresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.share.IEditShareView;
import com.xtelsolution.xmec.view.fragment.listShareLink.ShareListFragment;
import com.xtelsolution.xmec.view.widget.HeightWrappingViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListShareActivity extends BasicActivity implements IEditShareView {
    private EditSharePresenter presenter;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.viewpager)
    HeightWrappingViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_share);
        ButterKnife.bind(this);

        presenter = new EditSharePresenter(this);
        getData();
    }

    private void getData() {
        int mbrID = getIntExtra(Constants.OBJECT);

        if (mbrID != -1)
//            presenter.getShareAccount(mbrID);
            setViewpager(mbrID);
        else
            onGetDataError();
    }

    private void setViewpager(int mbrID) {
        List<Fragments> list = new ArrayList<>();
        list.add(new Fragments(ShareListFragment.newInstance(mbrID, true)));

        FragmentsAdapter fragmentsAdapter = new FragmentsAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(fragmentsAdapter);
    }

    public void refreshMbr(boolean isFinish) {
        setResult(RESULT_OK);

        if (isFinish)
            finish();
    }

    @OnClick(R.id.btn_close)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onSetData(Mbr mbr) {

    }

    @Override
    public void onGetDataError() {
        showErrorGetData(null);
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
