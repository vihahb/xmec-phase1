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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.entity.ShareMbrEntity;
import com.xtelsolution.xmec.presenter.sharelinkpresenter.LinkListPresenter;
import com.xtelsolution.xmec.view.activity.edit.EditLinkActivity;
import com.xtelsolution.xmec.view.activity.sharelink.ShareLinkListActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.fragment.inf.shareAndLink.ILinkListFragmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link LinkListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LinkListFragment extends BasicFragment implements ILinkListFragmentView, IAdapterListView {

    @BindView(R.id.rcl_link)
    RecyclerView rcl_link;
    @BindView(R.id.ln_loading)
    LinearLayout ln_loading;
    @BindView(R.id.tv_state)
    TextView tv_state;

    private int mbr_id;
    private LinkListPresenter presenter;
    private AdapterShareList adapter;
    private int REQUEST_EDIT_LINK = 92;
    private int ACTION_DELETE = 3;
    private int ACTION_UPDATE = 2;
    private int state;

    public LinkListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mbr_id
     * @return A new instance of fragment LinkListFragment.
     */
    public static LinkListFragment newInstance(int mbr_id) {
        LinkListFragment fragment = new LinkListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.MRB_ID, mbr_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_link_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new LinkListPresenter(this);

        if (getArguments() != null) {
            mbr_id = getArguments().getInt(Constants.MRB_ID);
            presenter.getMbr(mbr_id);
        }
    }

    private void initRecyclerView() {
        adapter = new AdapterShareList(getContext(), this, state);
        rcl_link.setLayoutManager(new LinearLayoutManager(getContext()));
        rcl_link.setHasFixedSize(true);
        rcl_link.setAdapter(adapter);
    }

    @Override
    public void hideData(boolean hiddenState) {
        if (hiddenState) {
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
    public void setDataListLink(List<ShareMbrEntity> data) {
        if (data.size() > 0) {
            hideData(true);
            adapter.updateData(data);
        } else {
            hideData(false);
            setData("Không có liên kết nào!");
        }
    }

    @Override
    public void getMbrSuccess(Mbr mbr) {
        if (mbr.getShareType() > 0 && mbr.getShareTo() > 0) {
            state = 1;
        } else {
            state = 0;
        }

        initRecyclerView();
        presenter.getListLink(mbr_id);
    }

    @Override
    public void shareOffSuccess(List<ShareAccounts> linkAccounts) {
        List<ShareMbrEntity> list = new ArrayList<>();
        if (linkAccounts.size() > 0){
            for (int i = 0; i < linkAccounts.size(); i++) {
                if (linkAccounts.get(i).getShareType() == 2){
                    list.add(new ShareMbrEntity(linkAccounts.get(i)));
                }
            }
            if (list.size() > 0) {
                hideData(true);
                adapter.updateData(list);
            } else {
                hideData(false);
                setData("Không có liên kết nào!");
            }
        }else {
            hideData(false);
            setData("Không có liên kết nào!");
        }
    }

    @Override
    public void onError(int code) {
        super.onError(code);
    }

    @Override
    public void onClickItem(int position, ShareMbrEntity entity) {
        startActivityForResult(EditLinkActivity.class,
                Constants.OBJECT, entity,
                Constants.POSITION, position,
                Constants.MRB_ID, mbr_id,
                REQUEST_EDIT_LINK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_LINK) {
            if (data != null) {
                int type = data.getIntExtra(Constants.ACTION_SHARE, -1);
                int position = data.getIntExtra(Constants.POSITION, -1);
                if (type == ACTION_DELETE) {
                    adapter.removeItem(position);
                    if (adapter.getItemCount() == 0) {
                        onCancel();
                    }
                }
                if (type == ACTION_UPDATE) {
                    ShareMbrEntity shareUpdate = (ShareMbrEntity) data.getSerializableExtra(Constants.OBJECT);
                    adapter.changeDataPosition(position, shareUpdate);
                }
            }
        }
    }

    private void onCancel() {
        if (getActivity() instanceof ShareLinkListActivity) {
            ShareLinkListActivity activity = (ShareLinkListActivity) getActivity();
            activity.onCancel();
        }
    }
}
