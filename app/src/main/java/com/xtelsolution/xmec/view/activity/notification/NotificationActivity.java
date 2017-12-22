package com.xtelsolution.xmec.view.activity.notification;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.DialogListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.mbr.Avatar16Adapter;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.NotifyConfig;
import com.xtelsolution.xmec.model.entity.ObjectNotify;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.presenter.notification.NotificationPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.notification.INotificationView;
import com.xtelsolution.xmec.view.activity.webview.WebviewActivity;
import com.xtelsolution.xmec.view.widget.RoundImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends BasicActivity implements INotificationView {

    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.progressBarLoading)
    ProgressBar progressBarLoading;
    @BindView(R.id.rcl_notify)
    RecyclerView rcl_notify;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.img_message)
    ImageView imgMessage;
    private NotificationAdapter adapter;
    List<ObjectNotify> notifyList;
    private NotificationPresenter presenter;
    private boolean isShow = false;
    private BroadcastReceiver loadReceive;
    List<Integer> mbrIdList;
    private Intent callbackIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initToolbar(R.id.toolbar, getString(R.string.title_notify));
        ButterKnife.bind(this);
        presenter = new NotificationPresenter(this);
        Log.e("FCM Key", "onCreate: " + FirebaseInstanceId.getInstance().getToken());
        initializingList();
        if (SharedUtils.getInstance().getStringValue(Constants.SESSION) != null) {
            presenter.getNotificationUser();
        } else {
            presenter.getNotificationSystem();
        }
        initializingSwipeRefresh();
        listenLoading();
        registerReceiver(loadReceive, new IntentFilter("ACTION_GET"));
    }

    private void listenLoading() {
        loadReceive = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                swipeRefreshLayout.setRefreshing(true);
                if (!android.text.TextUtils.isEmpty(SharedUtils.getInstance().getStringValue(Constants.SESSION))) {
                    presenter.getNotificationUser();
                } else {
                    presenter.getNotificationSystem();
                }
            }
        };
    }

    private void initializingSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark));

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!android.text.TextUtils.isEmpty(SharedUtils.getInstance().getStringValue(Constants.SESSION))) {
                    presenter.getNotificationUser();
                } else {
                    presenter.getNotificationSystem();
                }
            }
        });
    }

    private void initializingList() {
        mbrIdList = new ArrayList<>();
        callbackIntent = new Intent();

        notifyList = new ArrayList<>();
        rcl_notify.setLayoutManager(new LinearLayoutManager(this));
        rcl_notify.setHasFixedSize(true);
        adapter = new NotificationAdapter(this, this);
        rcl_notify.setAdapter(adapter);
        adapter.updateDataCollection(notifyList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getListNotification(final List<ObjectNotify> data) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                MyApplication.log("Data", data.toString());
                if (data.size() == 0) {
                    imgMessage.setVisibility(View.VISIBLE);
                    tv_state.setVisibility(View.VISIBLE);
                    tv_state.setText("Không có thông báo nào!");
                } else {
                    imgMessage.setVisibility(View.GONE);
                    tv_state.setVisibility(View.GONE);
                }
                adapter.updateDataCollection(data);
                closeProgressBar();
                progressBarLoading.setVisibility(View.GONE);
            }
        }, 1000);
    }

    @Override
    public void requestUpdateStateNotify(ObjectNotify data, int position) {
        if (SharedUtils.getInstance().getStringValue(Constants.SESSION) != null) {
            presenter.updateNotifyState(data, position);
        } else {
            presenter.updateStateDatabase(data, position);
        }
    }

    @Override
    public void updateStateNotify(ObjectNotify data, int position) {
        adapter.getItem(position).setDateCreatedLong(data.getDateCreatedLong());
        adapter.getItem(position).setDateCreated(data.getDateCreated());
        adapter.notifyItemChanged(position);
    }

    @Override
    public void showProgressBar(int type) {
    }

    @Override
    public void showSwipe(boolean swipe) {
        swipeRefreshLayout.setRefreshing(swipe);
    }

    @Override
    public void onError(int code) {
        super.onError(code);
        swipeRefreshLayout.setRefreshing(false);
        progressBarLoading.setVisibility(View.GONE);
        switch (code) {
            case 605:
                showLongToast("Không thể lấy dữ liệu thông báo. Vui lòng thử lại sau!");
                break;
            case 606:
                showLongToast("Không thể lấy dữ liệu y bạ. Vui lòng thử lại sau!");
                break;
            case 607:
                showLongToast("Không thể cập nhật trạng trái thông báo. Vui lòng thử lại sau!");
                break;
            case 608:
                showLongToast("Thao tác chưa hoàn tất. Vui lòng thử lại sau!");
                break;
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(loadReceive);
    }

    @Override
    public void getInfomationMbr(ObjectNotify data, int type, int position, int TYPE_VIEW) {
        showProgressBar(false, false, "Đang tải...");
        presenter.getMbrById(data, data.getMrbId(), type, position, TYPE_VIEW);
    }

    @Override
    public void startUrl(String contentUrl) {
        startActivity(WebviewActivity.class, Constants.URL_INTENT, contentUrl);
    }

    @Override
    public void setResultReload(Mbr mbr) {
        if (mbrIdList.size() > 0) {
            for (int i = 0; i < mbrIdList.size(); i++) {
                if (mbrIdList.get(i) == mbr.getMrbId()) {
                    mbrIdList.remove(i);
                    mbrIdList.add(mbr.getMrbId());
                }
            }
        } else {
            mbrIdList.add(mbr.getMrbId());
        }
        callbackIntent.putExtra(Constants.ID_LIST, (Serializable) mbrIdList);
        setResult(Activity.RESULT_OK, callbackIntent);
    }

    @Override
    public void setMessage(String s) {
        imgMessage.setVisibility(View.VISIBLE);
        tv_state.setText(s);
        tv_state.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        progressBarLoading.setVisibility(View.GONE);
    }

    @Override
    public void deleteNotification(int position, int id) {
        presenter.deleteNotification(position, id);
    }

    @Override
    public void deleteNotificationSuccess(int position_delete) {
        adapter.removeItem(position_delete);
    }

    @Override
    public void showDialogMbr(final ObjectNotify data, final Mbr mbr, final int type, final int position, final int TYPE_VIEW) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeProgressBar();
                if (TYPE_VIEW == 1) {
                    showMbrDialog(mbr, type, null);
                } else {
                    showMbrDialog(mbr, type, new DialogListener() {
                        @Override
                        public void negativeClicked() {
                            adapter.updateAction(position, false, data.getDateCreatedLong());
                            data.setActionState(2);
                            presenter.updateActionNotify(data, position);
                            presenter.updateSharedState(data);
                        }

                        @Override
                        public void positiveClicked() {
                            adapter.updateAction(position, true, data.getDateCreatedLong());
                            data.setActionState(1);
                            presenter.updateActionNotify(data, position);
                            presenter.updateSharedState(data);
                            presenter.saveMrbToRealm(data.getShare_id(), mbr);
                        }
                    });
                }
            }
        }, 1000);
    }

    protected void showMbrDialog(Mbr mbr, int type_title, final DialogListener dialogListener) {

        RecyclerView recyclerviewLink;
        RecyclerView recyclerviewShare;
        ImageView img_back;
        Button btn_close;

        final Dialog dialog = new Dialog(this, R.style.Theme_Transparent);
        dialog.setContentView(R.layout.item_dialog_mbr);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        LinearLayout ln_action = dialog.findViewById(R.id.ln_action);

        RoundImage avatar = dialog.findViewById(R.id.img_main_avatar);
        RoundImage create_avatar = dialog.findViewById(R.id.img_avatar);

        TextView txt_title = dialog.findViewById(R.id.txt_title_dialog);
        TextView txt_name = dialog.findViewById(R.id.txt_name);
        TextView txt_birthday = dialog.findViewById(R.id.txt_birthday);
        TextView txt_gender = dialog.findViewById(R.id.txt_gender);

        btn_close = dialog.findViewById(R.id.btn_close);

        img_back = dialog.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = false;
                dialog.dismiss();
            }
        });

        recyclerviewLink = dialog.findViewById(R.id.recyclerview_link);
        recyclerviewShare = dialog.findViewById(R.id.recyclerview_share);


        /**
         * Khởi tạo danh sách Chia sẻ
         */

        recyclerviewShare.setHasFixedSize(false);
        recyclerviewShare.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        Avatar16Adapter adapterShare = new Avatar16Adapter();
        recyclerviewShare.setAdapter(adapterShare);

        /**
         * Khởi tạo danh sách Liên kết
         */
        recyclerviewLink.setHasFixedSize(false);
        recyclerviewLink.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        Avatar16Adapter adapterLink = new Avatar16Adapter();
        recyclerviewLink.setAdapter(adapterLink);


        /**
         * Tách danh sách share và liên kết
         */
        List<ShareAccounts> shareList = new ArrayList<>();
        List<ShareAccounts> linkList = new ArrayList<>();

        for (ShareAccounts shareAccounts : mbr.getShareAccounts()) {
            if (shareAccounts.getShareType() == 1)
                shareList.add(shareAccounts);
            else
                linkList.add(shareAccounts);
        }

        adapterLink.setListData(linkList);
        adapterShare.setListData(shareList);

        int resourceAvatar = mbr.getGender() == 1 ? R.mipmap.ic_female_avatar : R.mipmap.ic_male_avatar;

        WidgetUtils.setImageURL(avatar, mbr.getAvatar(), resourceAvatar);
        WidgetUtils.setImageURL(create_avatar, mbr.getCreatorAvatar(), R.mipmap.ic_small_avatar_default);

        txt_name.setText(mbr.getName());

        String birthday = TimeUtils.convertLongToDate(mbr.getBirthdayLong());
        if (birthday == null)
            birthday = getString(R.string.layout_not_update);

        txt_birthday.setText(birthday);

        int resourceGender = mbr.getGender() == 1 ? R.mipmap.ic_gender_female_black_14 : R.mipmap.ic_gender_male_black_14;
        txt_gender.setText(TextUtils.getGender(mbr.getGender()));
        WidgetUtils.setTextViewDrawable(txt_gender, 0, resourceGender);


        Button btn_decline = dialog.findViewById(R.id.btn_decline);
        Button btn_accept = dialog.findViewById(R.id.btn_accept);

        switch (type_title) {
            case NotifyConfig.TYPE_LINK:
                txt_title.setText("Liên kết y bạ");
                break;
            case NotifyConfig.TYPE_SHARE:
                txt_title.setText("Chia sẻ y bạ");
                break;
            case NotifyConfig.TYPE_ACCEPTED:
                txt_title.setText("Đã chấp nhận yêu cầu");
                txt_title.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                break;
            case NotifyConfig.TYPE_DECLINE:
                txt_title.setText("Đã từ chối yêu cầu");
                txt_title.setTextColor(getApplicationContext().getResources().getColor(R.color.background_color));
                break;
        }

        if (dialogListener != null) {
            btn_close.setVisibility(View.GONE);
            btn_decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShow = false;
                    dialog.dismiss();
                    dialogListener.negativeClicked();
                }
            });

            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShow = false;
                    dialog.dismiss();
                    dialogListener.positiveClicked();
                }
            });
        } else {
            ln_action.setVisibility(View.GONE);
            btn_close.setVisibility(View.VISIBLE);
            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isShow = false;
                    dialog.dismiss();
                }
            });
        }

        if (!isShow) {
            isShow = true;
            dialog.show();
        }
    }
}
