package com.xtelsolution.xmec.presenter.hr;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.UserDrugImages;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.model.resp.RESP_Id;
import com.xtelsolution.xmec.model.resp.RESP_Upload_Image;
import com.xtelsolution.xmec.model.server.AddDrugImageModel;
import com.xtelsolution.xmec.model.server.CreateDrugModel;
import com.xtelsolution.xmec.model.server.SearchDrugModel;
import com.xtelsolution.xmec.model.server.UploadDrugImageModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.hr.ICreateDrugView;

import java.io.File;
import java.util.List;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/26/2017
 * Email: leconglongvu@gmail.com
 */
public class CreateDrugPresenter extends BasicPresenter {
    private ICreateDrugView view;

    private UserDrugs userDrugs;

    private List<UserDrugs> drugsList;

    public CreateDrugPresenter(ICreateDrugView view) {
        super(view);
        this.view = view;
        this.userDrugs = new UserDrugs();

        drugsList = new RealmList<>();
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 0:

                int hrID = (int) params[1];

                new GetListByKeyModel<UserDrugs>(UserDrugs.class, "healthRecordId", hrID) {
                    @Override
                    protected void onSuccess(RealmList<UserDrugs> realmList) {
                        if (realmList != null)
                            drugsList.addAll(realmList);
                    }
                };

                break;
            case 1:

                new CreateDrugModel(JsonHelper.toJson(userDrugs)) {
                    @Override
                    protected void onSuccess(RESP_Id respId) {
                        userDrugs.setId(respId.getId());
                        checkUploadImage();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode,params);
                    }
                };

                break;
            case 2:

                final UserDrugImages userDrugImages2 = (UserDrugImages) view.getListImage().get(0);
                final File file = new File(userDrugImages2.getImagePath());

                new UploadDrugImageModel(file) {
                    @Override
                    protected void onSuccess(RESP_Upload_Image obj) {
                        userDrugImages2.setUrlImage(obj.getPath());
                        userDrugs.getUserDrugImages().add(userDrugImages2);
                        view.getListImage().remove(0);
                        checkUploadImage();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION)
                            getNewSession(params);
                        else
                            showError(302);
                    }
                };

                break;
            case 3:

                final UserDrugImages userDrugImages = (UserDrugImages) view.getListImage().get(0);
                userDrugImages.setUserDrugId(userDrugs.getId());

                new AddDrugImageModel(JsonHelper.toJson(userDrugImages), 0) {
                    @Override
                    protected void onSuccess(RESP_Id resp_id) {
                        userDrugImages.setId(resp_id.getId());
                        userDrugs.getUserDrugImages().add(userDrugImages);
                        view.getListImage().remove(0);

                        checkAddImage();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(303);
                    }
                };

                break;
            case 4:

                final String key = (String) params[1];

                new SearchDrugModel(key, 1) {
                    @Override
                    protected void onSuccess(List<Drug> drugs) {
                        view.onSearchSuccess(drugs, key);
                    }

                    @Override
                    protected void onErrror(int code) {
                        if (code == Constants.ERROR_SESSION)
                            getNewSession(params);
                        else
                            showError(304);
                    }
                };

                break;
            case 5:

                new SaveObjectModel<UserDrugs>(userDrugs) {
                    @Override
                    protected void onSuccess() {
                        view.onAddSuccess();
                    }

                    @Override
                    protected void onError() {
                        view.onAddSuccess();
                    }
                };

                break;
        }
    }

    public void getUserDrugs(int hrID) {
        iCmd(0, hrID);
    }

    public void createDrug(Object... params) {
        int hrId = (int) params[0];
        Drug drug = (Drug) params[1];
        String note = (String) params[2];

        if (TextUtils.isEmpty(drug.getTenThuoc())) {
            showError(301);
            return;
        }

        for (UserDrugs userDrugs : drugsList) {
            if (userDrugs.getDrugName().equals(drug.getTenThuoc())) {
                showError(305);
                return;
            }
        }

        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        userDrugs.setHealthRecordId(hrId);
        userDrugs.setDrugName(drug.getTenThuoc());
        userDrugs.setDrugId(drug.getId());
        userDrugs.setNote(note);

        view.showProgressBar(2);
        iCmd(1);
    }

    public void checkUploadImage() {
        if (view.getListImage().size() == 0) {
            view.getListImage().addAll(userDrugs.getUserDrugImages());
            userDrugs.getUserDrugImages().clear();
            checkAddImage();
            return;
        }

        iCmd(2);
    }

    public void checkAddImage() {
        if (view.getListImage().size() == 0) {
            iCmd(5);
            return;
        }

        iCmd(3);
    }

    public void searchDrug(String key) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(304);
            return;
        }

        if (TextUtils.isEmpty(key)) {
            showError(304);
            return;
        }

        key = TextUtils.unicodeToKoDauLowerCase(key);
        key = key.replaceAll(" ", "%20");

        iCmd(4, key);
    }

    public int getUserDrugsID() {
        return userDrugs.getId();
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}