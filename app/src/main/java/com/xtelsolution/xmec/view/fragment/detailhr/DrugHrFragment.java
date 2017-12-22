package com.xtelsolution.xmec.view.fragment.detailhr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtelsolution.sdk.callback.ConfirmListener;
import com.xtelsolution.sdk.callback.HealthRecordOptionListener;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.DialogUtils;
import com.xtelsolution.sdk.utils.SizeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.hr.DrugAdapter;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.presenter.detailhr.DrugHrPresenter;
import com.xtelsolution.xmec.view.activity.detailhr.DetailHrActivity;
import com.xtelsolution.xmec.view.activity.hr.CreateDrugActivity;
import com.xtelsolution.xmec.view.activity.hr.DetailDrugActivity;
import com.xtelsolution.xmec.view.activity.hr.UpdateDrugActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.fragment.inf.IDrugHrView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmList;

import static android.app.Activity.RESULT_OK;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/31/2017
 * Email: leconglongvu@gmail.com
 */
public class DrugHrFragment extends BasicFragment implements IDrugHrView {
    private DrugHrPresenter presenter;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    Unbinder unbinder;

    private DrugAdapter drugAdapter;

    private HealthRecords healthRecords;

    private final int REQUEST_ADD_DRUG = 1;
    private final int REQUEST_UPDATE = 2;

    public static DrugHrFragment newInstance() {
        return new DrugHrFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drug_hr, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new DrugHrPresenter(this);

        getData();
    }

    private void getData() {
        healthRecords = ((DetailHrActivity) getActivity()).getHealthRecords();

        if (healthRecords == null)
            return;

        initRecyclerview();
        initActionListener();

        presenter.getUserDrugs(healthRecords.getId());
    }

    private void initRecyclerview() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        drugAdapter = new DrugAdapter(healthRecords.getEndDateLong());
        recyclerview.setAdapter(drugAdapter);

        drugAdapter.setListData(healthRecords.getUserDrugs());

        drugAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                if (position == -1) {
                    showUserDrugOption((UserDrugs) object);
                } else {
                    UserDrugs userDrugs = (UserDrugs) object;
                    startActivity(DetailDrugActivity.class, Constants.OBJECT, userDrugs.getId());
                }
            }
        });
    }

    private void initActionListener() {
        if (healthRecords.getEndDateLong() > 0)
            fabAdd.setVisibility(View.GONE);

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    showFab(false);
                } else if (dy < 0) {
                    showFab(true);
                }
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (healthRecords == null) {
                    showLongToast(getString(R.string.error_try_again));
                    return;
                }

                startActivityForResult(CreateDrugActivity.class, Constants.OBJECT, healthRecords.getId(), REQUEST_ADD_DRUG);
            }
        });
    }

    private void showUserDrugOption(final UserDrugs userDrugs) {
        DialogUtils.showUserDrugOption(getContext(), new HealthRecordOptionListener() {
            @Override
            public void onClicked(int type) {
                switch (type) {
                    case 3:
                        startActivityForResult(UpdateDrugActivity.class, Constants.OBJECT, userDrugs.getId(), REQUEST_UPDATE);
                        break;
                    case 4:
                        showConfirmDelete(userDrugs);
                        break;
                }
            }
        });
    }

    private void showConfirmDelete(final UserDrugs userDrugs) {
        DialogUtils.showConfirmDeleteDrug(getContext(), userDrugs.getDrugName(), new ConfirmListener() {
            @Override
            public void onConfirmSuccess() {
                presenter.deleteDrug(userDrugs);
            }
        });
    }

    private void onCreatedDrug(Intent data) {
        int id = -1;

        try {
            id = data.getIntExtra(Constants.OBJECT, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (id != -1) {
            presenter.getDrugRequest(id, 1);
        }
    }

    private void updateCreatedDrug(Intent data) {
        int id = -1;

        try {
            id = data.getIntExtra(Constants.OBJECT, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (id != -1) {
            presenter.getDrugRequest(id, 2);
        }
    }

    private void showFab(boolean isShow) {
        if (isShow) {
            WidgetUtils.slideView(fabAdd, 0);
        } else {
            WidgetUtils.slideView(fabAdd, fabAdd.getHeight() + SizeUtils.dpToPx(getContext(), 76));
        }
    }

    private List<UserDrugs> getListUserDrugs() {
        return ((DetailHrActivity) getActivity()).getHealthRecords().getUserDrugs();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onGetUserDrugsSuccess(RealmList<UserDrugs> realmList) {
        drugAdapter.setListData(realmList);
    }

    @Override
    public void onDeleteSuccess(UserDrugs userDrugs) {
        showFab(true);

        closeProgressBar();
        showLongToast(getString(R.string.success_add_drug_delete_drug));

        drugAdapter.deleteItem(userDrugs);
        getListUserDrugs().remove(userDrugs);
    }

    @Override
    public void onCreatedDrug(UserDrugs userDrugs) {
        drugAdapter.addItem(userDrugs);
        getListUserDrugs().add(userDrugs);
    }

    @Override
    public void onUpdatedDrug(UserDrugs userDrugs) {
        drugAdapter.updateItem(userDrugs);
        getListUserDrugs().clear();
        getListUserDrugs().addAll(drugAdapter.getListData());
    }

    @Override
    public void showProgressBar(int type) {
        String message;

        switch (type) {
            case 1:
                message = getString(R.string.doing_add_drug_delete_drug);
                break;
            default:
                message = getString(R.string.doing_do);
                break;
        }

        showProgressBar(false, false, message);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ADD_DRUG:
                if (resultCode == RESULT_OK) {
                    onCreatedDrug(data);
                }
                break;
            case REQUEST_UPDATE:
                if (resultCode == RESULT_OK) {
                    updateCreatedDrug(data);
                }
                break;
        }
    }
}