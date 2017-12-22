package com.xtelsolution.xmec.view.fragment.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtelsolution.sdk.callback.ConfirmListener;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.share.BeSharedAdapter;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.presenter.share.BeSharedPresenter;
import com.xtelsolution.xmec.view.activity.HomeActivity;
import com.xtelsolution.xmec.view.activity.share.DetailShareActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.fragment.inf.share.IBeSharedView;

import java.nio.file.DirectoryNotEmptyException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmList;

/**
 * Created by lecon on 12/1/2017
 */

public class BeSharedFragment extends BasicFragment implements IBeSharedView {
    private BeSharedPresenter presenter;

    @BindView(R.id.swipe_progress_view)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;

    private BeSharedAdapter sharedAdapter;

    public static BeSharedFragment newInstance() {
        return new BeSharedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new BeSharedPresenter(this);

        initRecyclerView();
        initSwipe();
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        sharedAdapter = new BeSharedAdapter(getContext().getApplicationContext());
        recyclerview.setAdapter(sharedAdapter);

        sharedAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                switch (position) {
                    case -2:
                        presenter.getShared();
                        break;
                    case -1:
                        showConfirmDelete((Mbr) object);
                        break;
                    default:
                        Mbr mbr = (Mbr) object;
                        Intent intent = new Intent(getActivity(), DetailShareActivity.class);
                        intent.putExtra(Constants.OBJECT, mbr.getMrbId());
                        intent.putExtra(Constants.OBJECT_2, mbr.getCreatorName());
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void initSwipe() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getShared();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                presenter.getShared();
            }
        });
    }

    private void showConfirmDelete(final Mbr item) {
        showConfirmDialog(null, getString(R.string.message_shared_delete), new ConfirmListener() {
            @Override
            public void onConfirmSuccess() {
                presenter.deleteBeShared(item);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onGetSharedSuccess(RealmList<Mbr> realmList) {
        swipeRefreshLayout.setRefreshing(false);
        sharedAdapter.setListData(realmList);
    }

    @Override
    public void onGetSharedError(int code) {
        swipeRefreshLayout.setRefreshing(false);
        sharedAdapter.setErrorCode(code);
    }

    @Override
    public void onDeleteSuccess(Mbr item) {
        closeProgressBar();
        sharedAdapter.removeItem(item);
        showLongToast(getString(R.string.success_share_delete));
    }

    @Override
    public void onDeleteError() {
        closeProgressBar();
        showLongToast(getString(R.string.error_share_delete));
    }

    @Override
    public void onNoInternet() {
        closeProgressBar();
        swipeRefreshLayout.setRefreshing(false);

        if (sharedAdapter.getListData().size() > 0) {
            showLongToast("Không có kết nối mạng. Vui lòng kiểm tra và tải lại dữ liệu.");
        } else {
            sharedAdapter.setErrorCode(Constants.ERROR_NO_INTERNET);
        }
    }

    @Override
    public void showProgressBar(int type) {
        if (type == 1) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            String message;

            switch (type) {
                case 2:
                    message = getString(R.string.doing_share_delete);
                    break;
                default:
                    message = getString(R.string.doing_do);
                    break;
            }

            showProgressBar(false, false, message);
        }
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == HomeActivity.REQUEST_NOTIFICATION) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.getShared();
            }
        }
    }
}