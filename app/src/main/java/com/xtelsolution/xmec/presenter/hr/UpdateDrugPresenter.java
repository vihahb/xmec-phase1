package com.xtelsolution.xmec.presenter.hr;

import android.util.SparseBooleanArray;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.database.DeleteObjectByKeyModel;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.UserDrugImages;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.model.resp.RESP_Id;
import com.xtelsolution.xmec.model.resp.RESP_Upload_Image;
import com.xtelsolution.xmec.model.server.AddDrugImageModel;
import com.xtelsolution.xmec.model.server.DeleteDrugImageModel;
import com.xtelsolution.xmec.model.server.SearchDrugModel;
import com.xtelsolution.xmec.model.server.UpdateDrugModel;
import com.xtelsolution.xmec.model.server.UploadDrugImageModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.hr.IUpdateDrugView;

import java.io.File;
import java.util.List;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/26/2017
 * Email: leconglongvu@gmail.com
 */
public class UpdateDrugPresenter extends BasicPresenter {
    private IUpdateDrugView view;

    private SparseBooleanArray arrayImage;

    private RealmList<UserDrugs> drugsList;

    public UpdateDrugPresenter(IUpdateDrugView view) {
        super(view);
        this.view = view;

        arrayImage = new SparseBooleanArray();
        drugsList = new RealmList<>();
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case -1:

                int id = (int) params[1];

                new GetObjectByKeyModel<UserDrugs>(UserDrugs.class, "id", id) {
                    @Override
                    protected void onSuccess(UserDrugs object) {
                        view.onGetDataSuccess(object);

                        if (object != null) {
                            iCmd(0, object.getHealthRecordId());
                        }
                    }
                };

                break;
            case 0:

                final int id0 = (int) params[1];

                new GetListByKeyModel<UserDrugs>(UserDrugs.class, "healthRecordId", id0) {
                    @Override
                    protected void onSuccess(RealmList<UserDrugs> realmList) {
                        drugsList.addAll(realmList);

                        int uID = view.getUserDrugs().getId();

                        for (UserDrugs userDrugs : drugsList) {
                            if (userDrugs.getId() == uID) {
                                drugsList.remove(userDrugs);
                                return;
                            }
                        }
                    }
                };

                break;
            case 1:

                final File file = (File) params[1];

                new UploadDrugImageModel(file) {
                    @Override
                    protected void onSuccess(RESP_Upload_Image obj) {
                        UserDrugImages userDrugImages = new UserDrugImages();

                        userDrugImages.setUrlImage(obj.getPath());
                        userDrugImages.setImagePath(file.getPath());

                        view.onUploadSuccess(userDrugImages);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 2:

                new UpdateDrugModel(JsonHelper.toJson(view.getUserDrugs())) {
                    @Override
                    protected void onSuccess() {
                        checkBeforeDeleteImage();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 3:

                RESP_Upload_Image respUploadImage = (RESP_Upload_Image) params[1];

                new DeleteDrugImageModel(JsonHelper.toJson(respUploadImage)) {
                    @Override
                    protected void onSuccess() {
                        int id = view.getUserDrugs().getUserDrugImages().get(0).getId();
                        view.getUserDrugs().getUserDrugImages().remove(0);
                        iCmd(6, id);
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
            case 4:

                final UserDrugImages userDrugImages = (UserDrugImages) view.getListImage().get(0);
                userDrugImages.setUserDrugId(view.getUserDrugs().getId());

                new AddDrugImageModel(JsonHelper.toJson(userDrugImages), 0) {
                    @Override
                    protected void onSuccess(RESP_Id resp_id) {
                        userDrugImages.setId(resp_id.getId());
                        view.getUserDrugs().getUserDrugImages().add(userDrugImages);
                        view.getListImage().remove(0);

                        checkAddImage();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION)
                            getNewSession(params);
                        else
                            showError(303);
                    }
                };

                break;
            case 5:

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
                    }
                };

                break;
            case 6:

                int id6 = (int) params[1];

                new DeleteObjectByKeyModel<UserDrugImages>(UserDrugImages.class, "id", id6) {
                    @Override
                    protected void onSuccess() {
                        checkDeleteImage();
                    }

                    @Override
                    protected void onError() {
                        checkDeleteImage();
                    }
                };

                break;
            case 7:

                new SaveObjectModel<UserDrugs>(view.getUserDrugs()) {
                    @Override
                    protected void onSuccess() {
                        view.onUpdateSuccess();
                    }

                    @Override
                    protected void onError() {
                        view.onUpdateSuccess();
                    }
                };

                break;
        }
    }

    public void getData(int id) {
        iCmd(-1, id);
    }

    public void uploadImage(File file) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(1);
        iCmd(1, file);
    }

    public void updateDrug() {
        String drugName = view.getUserDrugs().getDrugName();
        if (TextUtils.isEmpty(drugName)) {
            showError(301);
            return;
        }

        for (UserDrugs userDrugs : drugsList) {
            if (userDrugs.getDrugName().equals(drugName)) {
                showError(305);
                return;
            }
        }

        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(2);
        iCmd(2);
    }

    private void checkBeforeDeleteImage() {
        SparseBooleanArray array = new SparseBooleanArray();

        for (int i = view.getListImage().size() - 1; i >= 0; i--) {
            UserDrugImages userDrugImages = (UserDrugImages) view.getListImage().get(i);
            array.put(userDrugImages.getId(), true);
        }

        for (int i = view.getUserDrugs().getUserDrugImages().size() - 1; i >= 0; i--) {
            UserDrugImages userDrugImages = view.getUserDrugs().getUserDrugImages().get(i);

            if (array.get(userDrugImages.getId())) {
                view.getUserDrugs().getUserDrugImages().remove(i);
                arrayImage.put(userDrugImages.getId(), true);
            }
        }

        checkDeleteImage();
    }

    public void checkDeleteImage() {
        if (view.getUserDrugs().getUserDrugImages().size() == 0) {

            for (int i = view.getListImage().size() - 1; i >= 0; i--) {
                UserDrugImages healthRecordImages = (UserDrugImages) view.getListImage().get(i);

                if (arrayImage.get(healthRecordImages.getId())) {
                    view.getListImage().remove(i);
                    view.getUserDrugs().getUserDrugImages().add(0, healthRecordImages);
                }
            }

            checkAddImage();
            return;
        }

        RESP_Upload_Image respUploadImage = new RESP_Upload_Image();

        UserDrugImages userDrugImages = view.getUserDrugs().getUserDrugImages().get(0);
        respUploadImage.setPath(userDrugImages.getUrlImage());

        iCmd(3, respUploadImage);
    }

    public void checkAddImage() {
        if (view.getListImage().size() == 0) {
            iCmd(7);
            return;
        }

        iCmd(4);
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

        iCmd(5, key);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}