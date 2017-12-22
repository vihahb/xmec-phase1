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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtelsolution.sdk.callback.DialogListener;
import com.xtelsolution.sdk.callback.HealthRecordOptionListener;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.DialogUtils;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.share.SharedAdapter;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.presenter.share.SharedPresenter;
import com.xtelsolution.xmec.view.activity.HomeActivity;
import com.xtelsolution.xmec.view.activity.share.DetailShareActivity;
import com.xtelsolution.xmec.view.activity.share.ListShareActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.fragment.inf.share.ISharedView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmList;

/**
 * Created by lecon on 12/1/2017
 */

public class SharedFragment extends BasicFragment implements ISharedView {
    private SharedPresenter presenter;

    @BindView(R.id.swipe_progress_view)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;

    private SharedAdapter sharedAdapter;

    private final int REQUEST_SHARE = 1;

    public static SharedFragment newInstance() {
        return new SharedFragment();
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

        presenter = new SharedPresenter(this);

        initRecyclerView();
        initSwipe();
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        sharedAdapter = new SharedAdapter(getContext().getApplicationContext());
        recyclerview.setAdapter(sharedAdapter);

        if (checkSession()) {
            sharedAdapter.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(int position, Object object) {
                    switch (position) {
                        case -2:
                            presenter.getShared();
                            break;
                        case -1:
                            showOption((Mbr) object);
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

    }

    private boolean checkSession() {
        return SharedUtils.getInstance().getStringValue(Constants.SESSION) != null;
    }

    private void initSwipe() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkSession()) {
                    presenter.getShared();
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (checkSession()) {
                    presenter.getShared();
                }
            }
        });
    }

    private void showOption(final Mbr mbr) {
        DialogUtils.showSharedOption(getContext(), new HealthRecordOptionListener() {
            @Override
            public void onClicked(int type) {
                switch (type) {
                    case 3:
                        startActivityForResult(ListShareActivity.class, Constants.OBJECT, mbr.getMrbId(), REQUEST_SHARE);
                        break;
                    case 4:
                        presenter.deleteMbr(mbr);
                        break;
                }
            }
        });
    }

    private void showConfirmContinueDelete(final Mbr mbr) {
        showMaterialDialog(false, false, null,
                "Xóa chia sẻ không thành công. Bạn có muốn tiếp tục?",
                getString(R.string.layout_disagree), getString(R.string.layout_agree),
                new DialogListener() {
                    @Override
                    public void negativeClicked() {
                        sharedAdapter.updateItem(mbr);
                    }

                    @Override
                    public void positiveClicked() {
                        presenter.checkDeleteMbr();
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
    public void onDeleteSuccess(Mbr mbr) {
        closeProgressBar();

        sharedAdapter.removeItem(mbr);
    }

    @Override
    public void onDeleteErrpr(Mbr mbr) {
        showConfirmContinueDelete(mbr);
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
    public void onRequestError() {
        showLongToast("Có lỗi xảy ra. Vui lòng tải lại dữ liệu.");
    }

    @Override
    public void onGetSharedError(int code) {
        swipeRefreshLayout.setRefreshing(false);

        if (sharedAdapter.getListData().size() > 0) {
            onRequestError();
        } else {
            sharedAdapter.setErrorCode(code);
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
                    message = "Đang xóa y bạ…";
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_not_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case HomeActivity.REQUEST_NOTIFICATION:
                    if (checkSession()) {
                        presenter.getShared();
                    }
                    break;
                case REQUEST_SHARE:
                    if (checkSession()) {
                        presenter.getShared();
                    }
                    break;
            }
        }
    }
}