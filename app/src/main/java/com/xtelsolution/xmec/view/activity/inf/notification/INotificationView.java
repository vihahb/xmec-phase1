package com.xtelsolution.xmec.view.activity.inf.notification;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ObjectNotify;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Created by xtel on 11/8/17.
 */

public interface INotificationView extends IBasicView{
    void getListNotification(List<ObjectNotify> data);

    void requestUpdateStateNotify(ObjectNotify data, int position);
    void updateStateNotify(ObjectNotify data, int position);

    void showProgressBar(int type);
    void showSwipe(boolean swipe);
    Activity getActivity();

    void showDialogMbr(ObjectNotify data, Mbr mbr, int type, int position, int TYPE_VIEW);

    void getInfomationMbr(ObjectNotify data, int type, int position, int TYPE_VIEW);

    void startUrl(String contentUrl);

    void setResultReload(Mbr mbrIdList);

    void setMessage(String s);

    void deleteNotification(int position, int id);

    void deleteNotificationSuccess(int position_delete);
}
