package com.xtelsolution.xmec.view.fragment.mbr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.ConfirmListener;
import com.xtelsolution.sdk.callback.HealthRecordOptionListener;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.DialogUtils;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.SizeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.hr.HealthRecordsAdapter;
import com.xtelsolution.xmec.adapter.mbr.MbrsAdapter;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.NotificationAction;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.presenter.mbr.MbrPresenter;
import com.xtelsolution.xmec.reminder.ReminderUtils;
import com.xtelsolution.xmec.view.activity.HomeActivity;
import com.xtelsolution.xmec.view.activity.detailhr.DetailHrActivity;
import com.xtelsolution.xmec.view.activity.hr.HrCreateActivity;
import com.xtelsolution.xmec.view.activity.hr.UpdateHrActivity;
import com.xtelsolution.xmec.view.activity.share.ShareActivity;
import com.xtelsolution.xmec.view.fragment.IFragment;
import com.xtelsolution.xmec.view.fragment.inf.mbr.IMbrView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/17/2017
 * Email: leconglongvu@gmail.com
 */
public class MbrFragment extends IFragment implements IMbrView {
    private MbrPresenter presenter;

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    Unbinder unbinder;

    public static List<Mbr> listData;
    private MbrsAdapter fragmentsAdapter;
    private HealthRecordsAdapter healthAdapter;

    private final int REQUEST_HR_CREATE = 3;
    private final int REQUEST_HR_UPDATE = 4;

    private final int REQUEST_SHARE = 5;

    private final int REQUEST_LINK = 6;

    public static MbrFragment newInstance() {
        return new MbrFragment();
    }

    /**
     * Listen receive
     */
    private BroadcastReceiver delete_receive_notify = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final NotificationAction action = (NotificationAction) intent.getSerializableExtra(Constants.ACTION_DELETE);
                String type = intent.getStringExtra(Constants.TYPE);
                switch (type) {
                    case Constants.TYPE_SHARE:
                        deleteShareLink(action.getMrb_id(), action.getShare_id());
                        break;
                    case Constants.TYPE_LINK:
                        deleteShareLink(action.getMrb_id(), action.getShare_id());
                        break;
                }
            }
        }
    };

    private BroadcastReceiver update_receive_notify = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final NotificationAction action = (NotificationAction) intent.getSerializableExtra(Constants.ACTION_UPDATE);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        if (getActivity() != null) {
            getActivity().registerReceiver(delete_receive_notify, new IntentFilter(Constants.ACTION_DELETE_NOTIFY));
            getActivity().registerReceiver(update_receive_notify, new IntentFilter(Constants.ACTION_UPDATE_NOTIFY));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCheckCreateView(inflater.inflate(R.layout.fragment_mbr, container, false));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (listData == null) {
            listData = new ArrayList<>();
        }

        if (!isViewCreated()) {
            unbinder = ButterKnife.bind(this, view);

            presenter = new MbrPresenter(this);

            initRecyclerview();
            initViewpager();
            initActionListener();

            if (checkSession()) {
                presenter.getMbrs();
            }
        } else {
            fragmentsAdapter = new MbrsAdapter(getChildFragmentManager());
            fragmentsAdapter.setListData(listData);
            viewpager.setAdapter(fragmentsAdapter);
        }
    }

    private boolean checkSession() {
        return SharedUtils.getInstance().getStringValue(Constants.SESSION) != null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * Khởi tạo danh sách Sổ khám bệnh
     */
    private void initRecyclerview() {
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        healthAdapter = new HealthRecordsAdapter();
        recyclerview.setAdapter(healthAdapter);

        healthAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                switch (position) {
                    case Constants.ERROR_NO_INTERNET:
                        presenter.getMbrs();
                        break;
                    case Constants.ERROR_UNKOW:
                        presenter.getMbrs();
                        break;
                    case -4:
                        showHealthRecordOption((HealthRecords) object);
                        break;
                    case -5:
                        showHealthRecordOption((HealthRecords) object);
                        break;
                    default:
                        HealthRecords healthRecords = (HealthRecords) object;
                        startActivityForResult(DetailHrActivity.class, Constants.OBJECT, healthRecords.getId(), REQUEST_HR_UPDATE);
                        break;
                }
            }
        });
    }

    /**
     * Khởi tạo danh sách Mbr
     */
    private void initViewpager() {
        fragmentsAdapter = new MbrsAdapter(getChildFragmentManager());
        fragmentsAdapter.setListData(listData);

        viewpager.setAdapter(fragmentsAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < (fragmentsAdapter.getCount() - 1)) {
                    Mbr mbr = fragmentsAdapter.getMbr(position);

                    if (mbr != null) {
                        boolean isViewOnly = checkIsViewOnly(mbr);

                        healthAdapter.setListData(mbr.getHealthRecords());
                        healthAdapter.setViewOnly(isViewOnly);

                        if (isViewOnly) {
                            setViewOnly();
                        } else {
                            setViewState(View.VISIBLE);
                        }
                    }
                } else {
                    setViewState(View.GONE);
                }

                showFab(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setViewOnly() {
        txtTitle.setVisibility(View.VISIBLE);
        recyclerview.setVisibility(View.VISIBLE);
        fabAdd.setVisibility(View.GONE);

        healthAdapter.setOnly();
    }

    private void setViewState(int state) {
        txtTitle.setVisibility(state);
        recyclerview.setVisibility(state);
        fabAdd.setVisibility(state);
    }

    /**
     * Khởi tạo sự kiện lắng nghe người dùng tương tác
     */
    private void initActionListener() {
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
                Mbr mbr = fragmentsAdapter.getMbr(viewpager.getCurrentItem());
                startActivityForResult(HrCreateActivity.class, Constants.OBJECT, mbr.getMrbId(), REQUEST_HR_CREATE);
            }
        });
    }

    /**
     * Khởi tạo data cho danh sách Sổ khám bệnh
     */
    private void initFragments() {
        viewpager.setVisibility(View.VISIBLE);
        fragmentsAdapter.setListData(listData);

        if (fragmentsAdapter.getCount() > 1) {
            setViewState(View.VISIBLE);

            Mbr mbr = listData.get(viewpager.getCurrentItem());
            healthAdapter.setListData(mbr.getHealthRecords());
            healthAdapter.setViewOnly(checkIsViewOnly(mbr));

            if (checkIsViewOnly(mbr)) {
                setViewOnly();
            }
        } else {
            setViewState(View.GONE);
        }
    }

    /**
     * Tạo Mbr mới
     *
     * @param data thông tin
     */
    private void onAddSuccess(Intent data) {
        int mbrID = getIntExtra(Constants.OBJECT, data);

        if (mbrID != -1 && presenter != null) {
            presenter.getMbrAdded(mbrID);
        }
    }

    private void onUpdateMbr(Intent data) {
        int type = getIntExtra(Constants.OBJECT_2, data);

        if (fragmentsAdapter != null && viewpager != null && healthAdapter != null) {
            if (type != 2) {
                fragmentsAdapter.notifyDataSetChanged();
            } else {
                int currentItem = viewpager.getCurrentItem();

                fragmentsAdapter.removeItem(currentItem);
                updateListData();

                if (currentItem < fragmentsAdapter.getCount() - 1) {
                    Mbr mbr = fragmentsAdapter.getMbr(currentItem);

                    if (mbr != null) {
                        healthAdapter.setListData(mbr.getHealthRecords());
                        healthAdapter.setViewOnly(checkIsViewOnly(mbr));

                        if (mbr.getShareType() == 1 || (mbr.getShareType() == 2 && mbr.getShareTo() == 1)) {
                            setViewOnly();
                        }
                    }
                } else {
                    setViewState(View.GONE);
                }
            }
        }
    }

    private void updateListData() {
        listData.clear();
        listData.addAll(fragmentsAdapter.getListData());
        if (listData.size() > 0) {
            int last = listData.size() - 1;
            if (listData.get(last) == null)
                listData.remove(last);
        }
    }

    private void addHealthRecord(Intent data) {
        int hrID = getIntExtra(Constants.OBJECT, data);

        if (hrID != -1 && presenter != null) {
            presenter.getHealthRecord(hrID, 1);
        }
    }

    private void updateHealthRecord(Intent data) {
        int hrID = getIntExtra(Constants.OBJECT, data);

        if (hrID != -1 && presenter != null) {
            presenter.getHealthRecord(hrID, 2);
        }
    }

    private void showFab(boolean isShow) {
        if (isShow) {
            WidgetUtils.slideView(fabAdd, 0);
        } else {
            WidgetUtils.slideView(fabAdd, fabAdd.getHeight() + SizeUtils.dpToPx(getContext(), 16));
        }
    }

    private boolean checkIsViewOnly(Mbr mbr) {
        return mbr.getShareType() == 1 || (mbr.getShareType() == 2 && mbr.getShareTo() == 1);
    }

    public void ShareAndLinkMbr() {
        int count = fragmentsAdapter.getCount();
        if (count > 0 && (viewpager.getCurrentItem() < count - 1)) {
            Mbr mbr = fragmentsAdapter.getMbr(viewpager.getCurrentItem());
            if (mbr.getShareTo() > 0 && mbr.getShareType() > 0) {
                showLongToast("Chỉ có thể chia sẻ y bạ do chính bạn khởi tạo!");
            } else {
                startActivityForResult(ShareActivity.class, Constants.MRB_ID, mbr.getMrbId(), REQUEST_SHARE);
            }
        } else {
            showLongToast("Vui lòng tạo y bạ để có thể sử dụng chức năng");
        }
    }

    /**
     * Show menu chọn chức năng Health Record
     */
    private void showHealthRecordOption(final HealthRecords healthRecords) {
        DialogUtils.showHealthRecordOption(getContext(), healthRecords.getEndDateLong(), new HealthRecordOptionListener() {
            @Override
            public void onClicked(int type) {
                switch (type) {
                    case 1:
                        presenter.continueTreatment(healthRecords);
                        break;
                    case 2:
                        presenter.endOfTreatment(healthRecords);
                        break;
                    case 3:
                        startActivityForResult(UpdateHrActivity.class, Constants.OBJECT, healthRecords.getId(), REQUEST_HR_UPDATE);
                        break;
                    case 4:
                        confirmDeleteHealthRecord(healthRecords);
                        break;
                }
            }
        });
    }

    private void confirmDeleteHealthRecord(final HealthRecords healthRecords) {
        showConfirmDialog(null, getString(R.string.message_mbr_delete_hr), new ConfirmListener() {
            @Override
            public void onConfirmSuccess() {
                presenter.deleteHealthRecord(healthRecords);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
        if (viewpager != null)
            viewpager.removeAllViews();
        if (fragmentsAdapter != null)
            fragmentsAdapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null) {
            getActivity().unregisterReceiver(delete_receive_notify);
            getActivity().unregisterReceiver(update_receive_notify);
        }
    }

    @Override
    public void onGetSuccess(List<Mbr> mbrList) {
        listData.clear();
        listData.addAll(mbrList);

        if (getFragment() != null && getFragment().isVisible()) {
            initFragments();
            new Reminder(mbrList).execute();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    closeProgressBar();
                }
            }, 500);
        }
    }

    @Override
    public void onAddMbrSuccess(Mbr mbr) {
        fragmentsAdapter.addItem(mbr);
        updateListData();
        setViewState(View.VISIBLE);
        healthAdapter.setListData(mbr.getHealthRecords());
        healthAdapter.setViewOnly(false);
    }

    @Override
    public void onDeleteHRSuccess(HealthRecords healthRecords) {
        showFab(true);
        closeProgressBar();
        showLongToast(getString(R.string.success_schedule_delete));

        healthAdapter.deleteItem(healthRecords);
        fragmentsAdapter.getMbr(viewpager.getCurrentItem()).getHealthRecords().remove(healthRecords);
    }

    @Override
    public void onCreatedHealthRecord(HealthRecords healthRecords) {
        healthAdapter.addItem(healthRecords);
        fragmentsAdapter.getMbr(viewpager.getCurrentItem()).getHealthRecords().add(healthRecords);
    }

    @Override
    public void onUpdatedHealthRecord(HealthRecords healthRecords) {
        List<HealthRecords> recordsList = fragmentsAdapter.getMbr(viewpager.getCurrentItem()).getHealthRecords();

        for (int i = recordsList.size() - 1; i >= 0; i--) {
            if (healthRecords.getId() == recordsList.get(i).getId()) {
                recordsList.remove(i);
                recordsList.add(i, healthRecords);
                healthAdapter.setListData(recordsList);
                healthAdapter.setViewOnly(checkIsViewOnly(fragmentsAdapter.getMbr(viewpager.getCurrentItem())));
                return;
            }
        }
    }

    @Override
    public void onEndOfTreatmentSuccess(HealthRecords healthRecords) {
        closeProgressBar();
        showLongToast(getString(R.string.success_schedule_end_treatment));
        healthAdapter.updateItem(healthRecords);

        for (int i = fragmentsAdapter.getMbr(viewpager.getCurrentItem()).getHealthRecords().size() - 1; i >= 0; i--) {
            if (fragmentsAdapter.getMbr(viewpager.getCurrentItem()).getHealthRecords().get(i).getId() == healthRecords.getId()) {
                fragmentsAdapter.getMbr(viewpager.getCurrentItem()).getHealthRecords().remove(i);
                fragmentsAdapter.getMbr(viewpager.getCurrentItem()).getHealthRecords().add(i, healthRecords);
                return;
            }
        }
    }

    @Override
    public void onContinueTreatmentSuccess(HealthRecords healthRecords) {
        closeProgressBar();
        showLongToast(getString(R.string.success_schedule_continue_treatment));
        healthAdapter.updateItem(healthRecords);

        for (int i = fragmentsAdapter.getMbr(viewpager.getCurrentItem()).getHealthRecords().size() - 1; i >= 0; i--) {
            if (fragmentsAdapter.getMbr(viewpager.getCurrentItem()).getHealthRecords().get(i).getId() == healthRecords.getId()) {
                fragmentsAdapter.getMbr(viewpager.getCurrentItem()).getHealthRecords().remove(i);
                fragmentsAdapter.getMbr(viewpager.getCurrentItem()).getHealthRecords().add(i, healthRecords);
                return;
            }
        }
    }

    @Override
    public void onDeleteShareSuccess() {
        if (fragmentsAdapter != null)
            fragmentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressBar(int type) {
        String message;

        switch (type) {
            case 1:
                message = getString(R.string.doing_load_data);
                break;
            case 3:
                message = getString(R.string.doing_mbr_delete_hr);
                break;
            default:
                message = getString(R.string.doing_do);
                break;
        }

        showProgressBar(false, false, message);
    }

    @Override
    public void onGetMbrError(int code) {
        recyclerview.setVisibility(View.VISIBLE);
        healthAdapter.setError(code);
        showLongToast("Có lối xảy ra. Vui lòng tải lại dữ liệu");
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        switch (code) {
            case 300:

                showLongToast("Không có kết nối mạng. Vui lòng kiểm tra và tải lại dữ liệu");

                break;
        }
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case HomeActivity.REQUEST_MBR_CREATE:
                if (resultCode == RESULT_OK) {
                    onAddSuccess(data);
                }
                break;
            case HomeActivity.REQUEST_MBR_UPDATE:
                if (resultCode == RESULT_OK) {
                    onUpdateMbr(data);
                }
                break;
            case REQUEST_HR_CREATE:
                if (resultCode == RESULT_OK) {
                    addHealthRecord(data);
                }
                break;
            case REQUEST_HR_UPDATE:
                if (resultCode == RESULT_OK) {
                    updateHealthRecord(data);
                }
                break;

            case REQUEST_SHARE:
                if (data != null) {
                    if (data.getAction() == Constants.ACTION_ADD) {
                        fragmentsAdapter.notifyDataSetChanged();
                    }
                    if (data.getAction() == Constants.ACTION_DELETE)
                        fragmentsAdapter.notifyDataSetChanged();
                }
                break;

            case REQUEST_LINK:
                if (data != null) {
                    if (data.getAction() == Constants.ACTION_ADD) {
                        fragmentsAdapter.notifyDataSetChanged();
                    }
                    if (data.getAction() == Constants.ACTION_DELETE)
                        fragmentsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    public void addToList(Mbr mbr) {
//        fragmentsAdapter.addItem(mbr);
//        if (mbr == null) {
        presenter.getMbrsAgain();
//        } else
//            onAddMbrSuccess(mbr);
    }

    private static class Reminder extends AsyncTask<Void, Void, Void> {
        private List<Mbr> mbrList;

        Reminder(List<Mbr> mbrList) {
            this.mbrList = mbrList;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (Mbr mbr : mbrList) {
                if (mbr.getShareType() == 0 && mbr.getShareTo() == 0) {
                    for (HealthRecords healthRecords : mbr.getHealthRecords()) {
                        for (ScheduleDrug scheduleDrug : healthRecords.getScheduleDrugs()) {
                            ReminderUtils.startReminderSchedule(MyApplication.context, scheduleDrug, -1);
                        }
                    }
                } else {
                    for (HealthRecords healthRecords : mbr.getHealthRecords()) {
                        for (ScheduleDrug scheduleDrug : healthRecords.getScheduleDrugs()) {
                            ReminderUtils.cancelReminderSchedule(MyApplication.context, scheduleDrug.getId());
                        }
                    }
                }
            }

            return null;
        }
    }

    /**
     * Method notification
     *
     * @param mrb_id
     * @param share_id
     */
    public void deleteShareLink(int mrb_id, int share_id) {
        presenter.deleteShareAccount(share_id);
    }
}