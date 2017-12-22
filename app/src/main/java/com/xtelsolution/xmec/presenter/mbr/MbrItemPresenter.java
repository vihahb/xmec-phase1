package com.xtelsolution.xmec.presenter.mbr;

import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.DeleteShare;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.NotificationAction;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.server.DeleteShareModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.fragment.inf.MbrItemView;

import io.realm.RealmList;

/**
 * Created by vivu on 12/6/17
 * xtel.vn
 */

public class MbrItemPresenter extends BasicPresenter {

    private MbrItemView view;

    public MbrItemPresenter(MbrItemView view) {
        super(view);
        this.view = view;
    }

    public void deleteShareLinkByID(final int share_id, int shareType, int mbrId) {
//        new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", mrbId) {
//            @Override
//            protected void onSuccess(Mbr object) {
//                RealmList<ShareAccounts> shareAccounts = object.getShareAccounts();
//                for (int i = 0; i < shareAccounts.size(); i++) {
//                    if (shareAccounts.get(i).getId() == share_id) {
//                        shareObj = shareAccounts.get(i);
//                        break;
//                    }
//                }
//                object.setShareAccounts(shareAccounts);
//                view.removeShareLink(share_id, shareObj.getShareType());
//            }
//        };
        view.removeShareLink(share_id, shareType, mbrId);
    }

    @Override
    protected void getSessionSuccess(Object... params) {

    }
}
