package com.xtelsolution.xmec.presenter.notification;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.database.SaveListModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.NotifyConfig;
import com.xtelsolution.xmec.model.entity.ObjectNotify;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.SharedStateEntity;
import com.xtelsolution.xmec.model.resp.RESP_Notification;
import com.xtelsolution.xmec.model.resp.RESP_ShareInfo;
import com.xtelsolution.xmec.model.server.ActionNotificationModel;
import com.xtelsolution.xmec.model.server.DeleteNotification;
import com.xtelsolution.xmec.model.server.GetMbrByIdModel;
import com.xtelsolution.xmec.model.server.GetShareInfoById;
import com.xtelsolution.xmec.model.server.NotificationModel;
import com.xtelsolution.xmec.model.server.UpdateShareStateModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.notification.INotificationView;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by xtel on 11/8/17.
 */

public class NotificationPresenter extends BasicPresenter {
    INotificationView view;

    public NotificationPresenter(INotificationView view) {
        super(view);
        this.view = view;
    }

    public void iCmd(final Object... params) {
        switch ((int) params[0]) {

            /**
             * GET LIST NOTIFICATION
             **/
            case 1:
                new NotificationModel(1) {
                    @Override
                    protected void onSuccess(RESP_Notification obj) {
                        view.showSwipe(false);
                        view.getListNotification(obj.getData());
                    }

                    @Override
                    protected void onError(int errorCode) {
                        view.showSwipe(false);
                        if (errorCode == Constants.ERROR_SESSION) {
                            getNewSession(params);
                        } else {
                            view.setMessage(TextUtils.getErrorCode(errorCode));
                            showError(605);
                        }
                    }
                };
                break;

            /**
             * GET MBR INFO NOTIFICATION
             **/
            case 2:
                int mbrId = (int) params[1];
                final int type = (int) params[2];
                final int position = (int) params[3];
                final ObjectNotify data_notify = (ObjectNotify) params[4];
                final int TYPE_VIEW = (int) params[5];
                new GetMbrByIdModel(mbrId) {
                    @Override
                    protected void onSuccess(RealmList<Mbr> mbrList) {
                        Log.e("Presenter notification", "onSuccess: " + mbrList.get(0).toString());
                        view.showDialogMbr(data_notify, mbrList.get(0), type, position, TYPE_VIEW);
                    }

                    @Override
                    protected void onErrror(int code) {
                        if (code == Constants.ERROR_SESSION) {
                            getNewSession(params);
                        } else {
                            showError(606);
                        }
                    }
                };
                break;

            /**
             * UPDATE STATE NOTIFICATION
             **/
            case 3:
                final ObjectNotify data = (ObjectNotify) params[1];
                final int postition_state  = (int) params[2];
                final String temp_date_create = data.getDateCreated();
                final long temp_date_create_long = data.getDateCreatedLong();
                data.setDateCreatedLong(-1);
                data.setDateCreated(null);
                new ActionNotificationModel(JsonHelper.toJson(data)) {
                    @Override
                    protected void onSuccess() {
                        data.setDateCreatedLong(temp_date_create_long);
                        data.setDateCreated(temp_date_create);
                        view.updateStateNotify(data, postition_state);
                        MyApplication.log("Notify: ", "Update state ok!");
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION) {
                            getNewSession(params[0]);
                        } else {
                            showError(607);
                        }
                    }
                };

//                new UpdateState().execute(params, data);
                break;

            /**
             * UPDATE ACTION NOTIFICATION
             **/
            case 4:
                //Process accept and decline
                final ObjectNotify data_access = (ObjectNotify) params[1];
                final int position_action = (int) params[2];

                final String temp_date_create_action = data_access.getDateCreated();
                final long temp_date_create_long_action = data_access.getDateCreatedLong();

                data_access.setDateCreated(null);
                data_access.setDateCreatedLong(-1);

                new ActionNotificationModel(JsonHelper.toJson(data_access)) {
                    @Override
                    protected void onSuccess() {
                        data_access.setDateCreated(temp_date_create_action);
                        data_access.setDateCreatedLong(temp_date_create_long_action);
                        view.updateStateNotify(data_access, position_action);

                        switch (data_access.getActionState()) {
                            case NotifyConfig.STATE_ACCEPT:
                                switch (data_access.getNotifyType()) {
                                    case NotifyConfig.TYPE_LINK:
                                        view.showLongToast("Bạn đã đồng ý yêu cầu liên kết y bạ.");
                                        break;
                                    case NotifyConfig.TYPE_SHARE:
                                        view.showLongToast("Bạn đã đồng ý yêu cầu chia sẻ y bạ.");
                                        break;
                                }
                                break;
                            case NotifyConfig.STATE_DECLINE:
                                switch (data_access.getNotifyType()) {
                                    case NotifyConfig.TYPE_LINK:
                                        view.showLongToast("Bạn đã từ chối yêu cầu liên kết y bạ.");
                                        break;
                                    case NotifyConfig.TYPE_SHARE:
                                        view.showLongToast("Bạn đã từ chối yêu cầu chia sẻ y bạ.");
                                        break;
                                }
                                break;
                        }
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION) {
                            getNewSession(params);
                        } else {
                            showError(608);
                        }
                    }
                };
                break;

            /**
             *UPDATE SHARED STATE NOTIFICATION
             * */
            case 5:
                final ObjectNotify data_state = (ObjectNotify) params[1];
                SharedStateEntity stateEntity = new SharedStateEntity();
                stateEntity.setId(data_state.getShare_id());
                stateEntity.setShareState(data_state.getActionState());

                new UpdateShareStateModel(JsonHelper.toJson(stateEntity)) {
                    @Override
                    protected void onSuccess(RESP_Basic obj) {
                        MyApplication.log("Update shared state", "OK");
                    }

                    @Override
                    protected void onError(int errorCode) {
                        MyApplication.log("Update shared state", "Error");
                    }
                };
                break;
            case 6:
                new NotificationModel(0) {
                    @Override
                    protected void onSuccess(RESP_Notification obj) {
                        view.showSwipe(false);
                        view.getListNotification(obj.getData());
                        saveSystemNotificationDb(obj.getData());
                    }

                    @Override
                    protected void onError(int errorCode) {
                        view.showSwipe(false);
                        showError(errorCode);
                        view.setMessage(TextUtils.getErrorCode(errorCode));
                    }
                };
                break;
            case 7:
                Realm realm = Realm.getDefaultInstance();
                List<ObjectNotify> list = (List<ObjectNotify>) params[1];
                RealmList<ObjectNotify> oldList = getListNotifyFromRealm();
                final RealmList<ObjectNotify> newList = new RealmList<>();

                try {
                    if (oldList.size() > 0) {
                        for (int i = 0; i < oldList.size(); i++) {
                            ObjectNotify objectNotify = realm.where(ObjectNotify.class)
                                    .equalTo("id", list.get(i).getId())
                                    .findFirst();
                            if (objectNotify == null) {
                                newList.add(list.get(i));
                            } else {
                                continue;
                            }
                        }
                    } else {
                        newList.addAll(list);
                    }
                } catch (Exception e) {
                    Log.e("REALM PROCESS", "iCmd: " + e);
                }
                new SaveListModel<ObjectNotify>(newList) {
                    @Override
                    protected void onSuccess() {
                        MyApplication.log("Save list success", Arrays.toString(newList.toArray()));
                    }

                    @Override
                    protected void onError() {
                        MyApplication.log("Save list error", newList.toString());
                    }
                };
                break;

            case 8:
                ObjectNotify data_update_local = (ObjectNotify) params[1];
                int position_local = (int) params[2];
                new SaveObjectModel<ObjectNotify>(data_update_local) {
                    @Override
                    protected void onSuccess() {
                        MyApplication.log("Update state local", "OK");
                    }

                    @Override
                    protected void onError() {
                        MyApplication.log("Update state local", "ERROR");
                    }
                };
                break;

            //Add Mrb to list realm and notifyDatasetChanged
            case 9:
                int share_id = (int) params[1];
                final Mbr mbr = (Mbr) params[2];
                new GetShareInfoById(share_id) {
                    @Override
                    protected void onSuccess(RESP_ShareInfo shareInfo) {
                        mbr.setShareType(shareInfo.getShareType());
                        mbr.setShareTo(shareInfo.getShareTo());
                        iCmd(10, mbr);
                    }

                    @Override
                    protected void onErrror(int code) {
                        view.showLongToast("Vui lòng khởi động lại ứng dụng để cập nhật y bạ!");
                    }
                };
                break;

            case 10:
                final Mbr MbrId = (Mbr) params[1];
                Log.e("Notify save Mbr", "iCmd: " + MbrId.toString());
                new SaveObjectModel<Mbr>(MbrId) {
                    @Override
                    protected void onSuccess() {
                        view.setResultReload(MbrId);
                    }

                    @Override
                    protected void onError() {
                        view.showLongToast("Vui lòng khởi động lại ứng dụng để cập nhật y bạ!");
                    }
                };
                break;

            case 11:
                final int position_delete = (int) params[1];
                int id_delete = (int) params[2];
                new DeleteNotification(id_delete) {
                    @Override
                    protected void onSuccess(RESP_Basic obj) {
                        view.deleteNotificationSuccess(position_delete);
                    }

                    @Override
                    protected void onError(int error) {
                        String error_message = TextUtils.getErrorCode(error);
                        if (TextUtils.isEmpty(error_message)){
                            view.showLongToast(error_message);
                        } else {
                            view.showLongToast("Có lỗi xảy ra. Vui lòng thử lại sau!");
                        }
                    }
                };
                break;
        }
    }


    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }

    public void getNotificationUser() {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            view.showSwipe(false);
            view.setMessage("Không có kết nối mạng!");
            return;
        } else {
            view.showSwipe(true);
            iCmd(1);
        }
    }

    public void getNotificationSystem() {
//        http://x-mec.com/api/system/notification
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            view.showSwipe(false);
            view.setMessage("Không có kết nối mạng!");
            return;
        } else {
            view.showSwipe(true);
            iCmd(6);
        }
    }

    public void getMbrById(ObjectNotify data, int mbrId, int type, int position, int TYPE_VIEW) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }
        iCmd(2, mbrId, type, position, data, TYPE_VIEW);
    }

    public void updateNotifyState(ObjectNotify data, int position) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }
        iCmd(3, data, position);
    }

    public void updateSharedState(ObjectNotify data) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }
        iCmd(5, data);
    }

    public void updateActionNotify(ObjectNotify data, int position) {
        iCmd(4, data, position);
    }

    public void saveSystemNotificationDb(List<ObjectNotify> data) {
        iCmd(7, data);
    }

    public RealmList<ObjectNotify> getListNotifyFromRealm() {
        final RealmList<ObjectNotify> mList = new RealmList<>();
        new GetListByKeyModel<ObjectNotify>(ObjectNotify.class, null, -1) {
            @Override
            protected void onSuccess(RealmList<ObjectNotify> realmList) {
                mList.addAll(realmList);
            }
        };
        return mList;
    }

    public void updateStateDatabase(ObjectNotify data, int position) {
        iCmd(8, data, position);
    }

    public void saveMrbToRealm(int share_id, Mbr mbr) {
        iCmd(9, share_id, mbr);
    }

    public void deleteNotification(int position, int id) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }
        iCmd(11, position, id);
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateState extends AsyncTask<Object, Integer, Void> {

        @Override
        protected Void doInBackground(final Object... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyApplication.log("Notify: ", "Update state ok!");
        }
    }
}
