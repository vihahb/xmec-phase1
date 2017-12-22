package com.xtelsolution.xmec.view.fragment.listShareLink;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.entity.ShareMbrEntity;
import com.xtelsolution.xmec.presenter.sharelinkpresenter.ShareListPresenter;
import com.xtelsolution.xmec.view.activity.edit.EditShareActivity;
import com.xtelsolution.xmec.view.activity.share.ListShareActivity;
import com.xtelsolution.xmec.view.activity.sharelink.ShareLinkListActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.fragment.inf.shareAndLink.IShareListFragmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShareListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShareListFragment extends BasicFragment implements IShareListFragmentView, IAdapterListView {

    @BindView(R.id.rcl_share)
    RecyclerView rcl_share;
    @BindView(R.id.ln_loading)
    LinearLayout ln_loading;
    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.fr_main)
    FrameLayout fr_main;

    private int mbr_id;
    private boolean isShared;
    private ShareListPresenter presenter;
    private AdapterShareList adapter;
    private int REQUEST_EDIT_SHARE = 91;
    private int ACTION_DELETE = 3;
    private int ACTION_UPDATE = 2;
    private int state;

    public ShareListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mbr_id
     * @return A new instance of fragment ShareListFragment.
     */
    public static ShareListFragment newInstance(int mbr_id, boolean isShared) {
        ShareListFragment fragment = new ShareListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.MRB_ID, mbr_id);
        args.putBoolean(Constants.OBJECT, isShared);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new ShareListPresenter(this);

        getData();
    }

    private void getData() {
        mbr_id = getIntExtra(Constants.MRB_ID);

        try {
            isShared = getArguments().getBoolean(Constants.OBJECT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mbr_id == -1) {
            return;
        }

        presenter.getMbr(mbr_id);
    }

    private void initRecyclerView() {
        adapter = new AdapterShareList(getContext(), this, state);
        rcl_share.setLayoutManager(new LinearLayoutManager(getContext()));
        rcl_share.setHasFixedSize(true);
        rcl_share.setAdapter(adapter);
    }

    @Override
    public void hideData(boolean hiddenState) {
        if (hiddenState) {
            fr_main.setVisibility(View.GONE);
            tv_state.setVisibility(View.GONE);
        } else {
            tv_state.setVisibility(View.VISIBLE);
        }
        ln_loading.setVisibility(View.GONE);
    }

    @Override
    public void showData() {
        ln_loading.setVisibility(View.GONE);
        tv_state.setVisibility(View.VISIBLE);
        tv_state.setTextColor(getResources().getColor(R.color.black_65));
    }

    @Override
    public void setData(String message) {
        tv_state.setVisibility(View.VISIBLE);
        tv_state.setText(message);
        tv_state.setTextColor(getResources().getColor(R.color.black_65));
    }

    @Override
    public void setDataListShare(List<ShareMbrEntity> data) {
        if (data.size() > 0) {
            hideData(true);
            adapter.updateData(data);
        } else {
            hideData(false);
            setData("Không có chia sẻ nào!");
        }
    }

    @Override
    public void getMbrSuccess(Mbr mbr) {
        if (mbr.getShareType() > 0 || mbr.getShareTo() > 0) {
            state = 1;
        } else {
            state = 0;
        }

        if (isShared)
            state = 0;

        initRecyclerView();
    }

    @Override
    public void shareOffSuccess(List<ShareAccounts> shareAccounts) {
        List<ShareMbrEntity> list = new ArrayList<>();
        if (shareAccounts.size() > 0) {
            for (int i = 0; i < shareAccounts.size(); i++) {
                if (shareAccounts.get(i).getShareType() == 1) {
                    list.add(new ShareMbrEntity(shareAccounts.get(i)));
                }
            }
            if (list.size() > 0) {
                hideData(true);
                adapter.updateData(list);
            } else {
                hideData(false);
                setData("Không có chia sẻ nào!");
            }
        } else {
            hideData(false);
            setData("Không có chia sẻ nào!");
        }
    }

    @Override
    public void onError(int code) {
        super.onError(code);
    }

    @Override
    public void onClickItem(int position, ShareMbrEntity entity) {
        boolean isChangeOnly = getActivity() instanceof ListShareActivity;

        String[] key = {Constants.OBJECT, Constants.POSITION, Constants.MRB_ID, Constants.OBJECT_2};
        Object[] value = {entity, position, mbr_id, isChangeOnly};

        startActivityForResult(EditShareActivity.class, key, value, REQUEST_EDIT_SHARE);

//        startActivityForResult(EditShareActivity.class,
//                Constants.OBJECT,   entity,
//                Constants.POSITION, position,
//                Constants.MRB_ID,   mbr_id,
//                REQUEST_EDIT_SHARE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_SHARE) {
            if (data != null) {
                int type = data.getIntExtra(Constants.ACTION_SHARE, -1);
                int position = data.getIntExtra(Constants.POSITION, -1);
                if (type == ACTION_DELETE) {
                    adapter.removeItem(position);
                    if (adapter.getItemCount() == 0) {
                        onCancel();
                        return;
                    }
                }
                if (type == ACTION_UPDATE) {
                    ShareMbrEntity shareUpdate = (ShareMbrEntity) data.getSerializableExtra(Constants.OBJECT);
                    adapter.changeDataPosition(position, shareUpdate);
                }

                if (getActivity() instanceof ListShareActivity) {
                    ListShareActivity activity = (ListShareActivity) getActivity();
                    activity.refreshMbr(false);
                }
            }
        }
    }

    private void onCancel() {
        if (getActivity() instanceof ShareLinkListActivity) {
            ShareLinkListActivity activity = (ShareLinkListActivity) getActivity();
            activity.onCancel();
        } else if (getActivity() instanceof ListShareActivity) {
            ListShareActivity activity = (ListShareActivity) getActivity();
            activity.refreshMbr(true);
        }
    }
}