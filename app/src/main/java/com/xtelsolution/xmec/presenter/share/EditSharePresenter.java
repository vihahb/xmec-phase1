package com.xtelsolution.xmec.presenter.share;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.GetSharedMbrModel;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.share.IEditShareView;

/**
 * Created by lecon on 12/8/2017
 */
public class EditSharePresenter extends BasicPresenter {
    private IEditShareView view;

    public EditSharePresenter(IEditShareView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {
            case 1:

                int mbrID = (int) params[1];

                new GetSharedMbrModel(mbrID) {
                    @Override
                    protected void onSuccess(Mbr object) {
                        view.onSetData(object);
                    }
                };

                break;
        }
    }

    public void getShareAccount(int mbrID) {
        iCmd(1, mbrID);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}