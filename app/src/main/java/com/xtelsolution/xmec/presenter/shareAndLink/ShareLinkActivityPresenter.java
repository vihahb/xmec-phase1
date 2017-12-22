package com.xtelsolution.xmec.presenter.shareAndLink;

import com.xtelsolution.MyApplication;
import com.xtelsolution.xmec.model.database.SaveContactModel;
import com.xtelsolution.xmec.model.entity.Contact_Model;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.share.IShareLinkView;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by vivu on 11/25/17
 * xtel.vn
 */

public class ShareLinkActivityPresenter extends BasicPresenter {
    private static final String TAG = "ShareLinkActivityPresen";
    private IShareLinkView view;

    public ShareLinkActivityPresenter(IShareLinkView basicView) {
        super(basicView);
        view = basicView;
    }

    private void iCmd(Object... params){
        switch ((int)params[0]){
        }
    }

    @Override
    protected void getSessionSuccess(Object... params) {

    }

    public void saveListContacts(List<Contact_Model> list) {
        RealmList<Contact_Model> realmList = new RealmList<>();
        realmList.addAll(list);
        new SaveContactModel<Contact_Model>(realmList) {
            @Override
            protected void onSuccess() {
                MyApplication.log("onPostExecute", "SUCCESS");
            }

            @Override
            protected void onError(String message) {
                MyApplication.log("onPostExecute", "onError " + message);
            }
        };
    }
}
