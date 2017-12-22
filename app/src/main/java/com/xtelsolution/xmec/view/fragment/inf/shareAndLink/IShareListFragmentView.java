package com.xtelsolution.xmec.view.fragment.inf.shareAndLink;

import android.app.Activity;
import android.content.Context;

import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.entity.ShareMbrEntity;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Created by xtel on 11/14/17.
 */

public interface IShareListFragmentView extends IBasicView{

    void hideData(boolean hiddenState);
    void showData();
    void setData(String message);

    Activity getActivity();

    void setDataListShare(List<ShareMbrEntity> data);

    void getMbrSuccess(Mbr mbr);

    void shareOffSuccess(List<ShareAccounts> shareAccounts);

    Context getContext();
}
