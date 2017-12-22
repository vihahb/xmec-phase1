package com.xtelsolution.xmec.presenter.hr;

import android.util.SparseBooleanArray;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.database.DeleteObjectByKeyModel;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.HealthRecordImages;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.model.entity.UserDiseases;
import com.xtelsolution.xmec.model.req.REQ_Delete_Image;
import com.xtelsolution.xmec.model.req.REQ_Hr_Image;
import com.xtelsolution.xmec.model.resp.RESP_Id;
import com.xtelsolution.xmec.model.resp.RESP_Upload_Image;
import com.xtelsolution.xmec.model.server.AddHrImageModel;
import com.xtelsolution.xmec.model.server.AddUserDiseaseModel;
import com.xtelsolution.xmec.model.server.DeleteDiseaseModel;
import com.xtelsolution.xmec.model.server.DeleteHrImageModel;
import com.xtelsolution.xmec.model.server.SearchHospitalModel;
import com.xtelsolution.xmec.model.server.UpdateHrModel;
import com.xtelsolution.xmec.model.server.UploadHrImageModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.hr.IUpdateHrView;

import java.io.File;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/1/2017
 * Email: leconglongvu@gmail.com
 */
public class UpdateHrPresenter extends BasicPresenter {
    private IUpdateHrView view;

    private SparseBooleanArray arrayImage;

    public UpdateHrPresenter(IUpdateHrView view) {
        super(view);
        this.view = view;
        arrayImage = new SparseBooleanArray();
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 0:

                int id = (int) params[1];

                new GetObjectByKeyModel<HealthRecords>(HealthRecords.class, "id", id) {
                    @Override
                    protected void onSuccess(HealthRecords object) {
                        view.onGetHealthRecordSuccess(object);
                    }
                };

                break;
            case 1:

                final String key = (String) params[1];

                new SearchHospitalModel(key, 1) {
                    @Override
                    protected void onSuccess(List<Hospital> hospitals) {
                        view.onSearchHospitalSuccess(hospitals, key);
                    }

                    @Override
                    protected void onErrror(int code) {
                        if (code == Constants.ERROR_SESSION)
                            getNewSession(params);
                    }
                };

                break;
            case 2:

                final File file = (File) params[1];

                new UploadHrImageModel(file) {
                    @Override
                    protected void onSuccess(RESP_Upload_Image obj) {
                        HealthRecordImages userDrugImages = new HealthRecordImages();

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
            case 3:

                new UpdateHrModel(JsonHelper.toJson(view.getHealthRecord())) {
                    @Override
                    protected void onSuccess() {
                        iCmd(8);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 4:

                final UserDiseases userDiseases4 = view.getHealthRecord().getUserDiseases().get(0);

                new DeleteDiseaseModel(userDiseases4.getId(), JsonHelper.toJson(userDiseases4)) {
                    @Override
                    protected void onSuccess() {
                        view.getHealthRecord().getUserDiseases().remove(0);

                        iCmd(9, userDiseases4.getId());
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION)
                            getNewSession(params);
                        else
                            showError(304);
                    }
                };

                break;
            case 5:

                final UserDiseases userDiseases5 = (UserDiseases) view.getDiseaseList().get(0);
                userDiseases5.setHrId(view.getHealthRecord().getId());

                new AddUserDiseaseModel(JsonHelper.toJson(userDiseases5)) {
                    @Override
                    protected void onSuccess(RESP_Id respId) {
                        userDiseases5.setId(respId.getId());
                        view.getHealthRecord().getUserDiseases().add(userDiseases5);
                        view.getDiseaseList().remove(0);
                        checkAddDisease();

//                        iCmd(10, userDiseases5);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION)
                            getNewSession(params);
                        else
                            showError(305);
                    }
                };

                break;
            case 6:

                final HealthRecordImages healthRecordImages6 = view.getHealthRecord().getHealthRecordImages().get(0);
                REQ_Delete_Image reqDeleteImage = new REQ_Delete_Image(healthRecordImages6.getUrlImage());

                new DeleteHrImageModel(JsonHelper.toJson(reqDeleteImage)) {
                    @Override
                    protected void onSuccess() {
                        view.getHealthRecord().getHealthRecordImages().remove(0);

                        iCmd(11, healthRecordImages6.getId());
//                        checkDeleteImage();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION)
                            getNewSession(params);
                        else
                            showError(306);
                    }
                };

                break;
            case 7:

                final HealthRecordImages healthRecordImages = (HealthRecordImages) view.getImageList().get(0);

                REQ_Hr_Image reqHrImage = new REQ_Hr_Image();
                reqHrImage.setHeathRecordId(view.getHealthRecord().getId());
                reqHrImage.setUrlImage(healthRecordImages.getUrlImage().replace("http://x-mec.com/storage/resources/users", ""));

                new AddHrImageModel(JsonHelper.toJson(reqHrImage), 0) {
                    @Override
                    protected void onSuccess(RESP_Id respId) {
                        healthRecordImages.setId(respId.getId());
                        view.getHealthRecord().getHealthRecordImages().add(healthRecordImages);
                        view.getImageList().remove(0);

//                        iCmd(12, healthRecordImages);
                        checkAddImage();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION)
                            getNewSession(params);
                        else
                            showError(305);
                    }
                };

                break;
            case 8:

                new SaveObjectModel<HealthRecords>(view.getHealthRecord()) {
                    @Override
                    protected void onSuccess() {
                        checkDeleteDisease();
                    }

                    @Override
                    protected void onError() {
                        checkDeleteDisease();
                    }
                };

                break;
            case 9:

                int id9 = (int) params[1];

                new DeleteObjectByKeyModel<UserDiseases>(UserDiseases.class, "id", id9) {
                    @Override
                    protected void onSuccess() {
                        checkDeleteDisease();
                    }

                    @Override
                    protected void onError() {
                        checkDeleteDisease();
                    }
                };

                break;
            case 10:

                UserDiseases userDiseases10 = (UserDiseases) params[1];

                new SaveObjectModel<UserDiseases>(userDiseases10) {
                    @Override
                    protected void onSuccess() {
                        checkAddDisease();
                    }

                    @Override
                    protected void onError() {
                        checkAddDisease();
                    }
                };

                break;
            case 11:

                int id11 = (int) params[1];

                new DeleteObjectByKeyModel<HealthRecordImages>(HealthRecordImages.class, "id", id11) {
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
            case 12:

                HealthRecordImages healthRecordImages12 = (HealthRecordImages) params[1];

                new SaveObjectModel<HealthRecordImages>(healthRecordImages12) {
                    @Override
                    protected void onSuccess() {
                        checkAddImage();
                    }

                    @Override
                    protected void onError() {
                        checkAddImage();
                    }
                };

                break;
            case 13:

                MyApplication.log("UpdateHrPresenter", "SaveObjectModel " + JsonHelper.toJson(view.getHealthRecord()));

                new SaveObjectModel<HealthRecords>(view.getHealthRecord()) {
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

    public void getHealthRecord(int hrId) {
        iCmd(0, hrId);
    }

    public void searchHospital(String key) {
        key = TextUtils.unicodeToKoDau(key);
        key = key.replaceAll(" ", "%20");
        iCmd(1, key);
    }

    public void updateImage(File file) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        if (file == null || !file.exists()) {
            showError(Constants.ERROR_UNKOW);
            return;
        }

        view.showProgressBar(1);
        iCmd(2, file);
    }

    public void updateHr() {
        if (view.getHealthRecord().getDateCreateLong() == 0) {
            showError(303);
            return;
        }

        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(2);
        iCmd(3);
    }

    public void checkDeleteDisease() {
        if (view.getHealthRecord().getUserDiseases().size() == 0) {
            checkAddDisease();
            return;
        }

        iCmd(4);
    }

    public void checkAddDisease() {
        if (view.getDiseaseList().size() == 0) {
            SparseBooleanArray array = new SparseBooleanArray();

            for (int i = view.getImageList().size() - 1; i >= 0; i--) {
                HealthRecordImages healthRecordImages = (HealthRecordImages) view.getImageList().get(i);
                array.put(healthRecordImages.getId(), true);
            }

            for (int i = view.getHealthRecord().getHealthRecordImages().size() - 1; i >= 0; i--) {
                HealthRecordImages healthRecordImages = view.getHealthRecord().getHealthRecordImages().get(i);

                if (array.get(healthRecordImages.getId())) {
                    view.getHealthRecord().getHealthRecordImages().remove(i);
                    arrayImage.put(healthRecordImages.getId(), true);
                }
            }

            checkDeleteImage();
            return;
        }

        iCmd(5);
    }

    public void checkDeleteImage() {
        if (view.getHealthRecord().getHealthRecordImages().size() == 0) {

            for (int i = view.getImageList().size() - 1; i >= 0; i--) {
                HealthRecordImages healthRecordImages = (HealthRecordImages) view.getImageList().get(i);
                if (arrayImage.get(healthRecordImages.getId())) {
                    view.getImageList().remove(i);
                    view.getHealthRecord().getHealthRecordImages().add(0, healthRecordImages);
                }
            }

            checkAddImage();
            return;
        }

        iCmd(6);
    }

    public void checkAddImage() {
        if (view.getImageList().size() == 0) {
            iCmd(13);
            return;
        }

        iCmd(7);
    }





    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}