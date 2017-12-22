package com.xtelsolution.xmec.presenter.hr;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.HealthRecordImages;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.model.entity.UserDiseases;
import com.xtelsolution.xmec.model.req.REQ_Hr_Create;
import com.xtelsolution.xmec.model.req.REQ_Hr_Image;
import com.xtelsolution.xmec.model.resp.RESP_Id;
import com.xtelsolution.xmec.model.resp.RESP_Upload_Image;
import com.xtelsolution.xmec.model.server.AddHrImageModel;
import com.xtelsolution.xmec.model.server.AddUserDiseaseModel;
import com.xtelsolution.xmec.model.server.CreateHrModel;
import com.xtelsolution.xmec.model.server.SearchHospitalModel;
import com.xtelsolution.xmec.model.server.UploadHrImageModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.hr.IHrCreateView;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/24/2017
 * Email: leconglongvu@gmail.com
 */
public class HrCreatePresenter extends BasicPresenter {
    private IHrCreateView view;

    private HealthRecords healthRecords;

    public HrCreatePresenter(IHrCreateView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 1:

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
            case 2:

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
            case 3:

                final REQ_Hr_Create hrCreate = (REQ_Hr_Create) params[1];

                new CreateHrModel(JsonHelper.toJson(hrCreate)) {
                    @Override
                    protected void onSuccess(RESP_Id obj) {
                        hrCreate.setId(obj.getId());
                        healthRecords = new HealthRecords(hrCreate);
                        checkAddDisease();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 4:

                final UserDiseases userDiseases = (UserDiseases) view.getDiseaseList().get(0);
                userDiseases.setHrId(healthRecords.getId());
                userDiseases.setDiseaseId(userDiseases.getId());

                new AddUserDiseaseModel(JsonHelper.toJson(userDiseases)) {
                    @Override
                    protected void onSuccess(RESP_Id respId) {
                        userDiseases.setId(respId.getId());
                        healthRecords.getUserDiseases().add(userDiseases);
                        view.getDiseaseList().remove(0);
                        checkAddDisease();
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

                final HealthRecordImages healthRecordImages = (HealthRecordImages) view.getImageList().get(0);

                REQ_Hr_Image reqHrImage = new REQ_Hr_Image();
                reqHrImage.setHeathRecordId(healthRecords.getId());
                reqHrImage.setUrlImage(healthRecordImages.getUrlImage());

                new AddHrImageModel(JsonHelper.toJson(reqHrImage), 0) {
                    @Override
                    protected void onSuccess(RESP_Id respId) {
                        healthRecordImages.setId(respId.getId());
                        healthRecords.getHealthRecordImages().add(healthRecordImages);
                        view.getImageList().remove(0);
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
            case 6:

                new SaveObjectModel<HealthRecords>(healthRecords) {
                    @Override
                    protected void onSuccess() {
                        view.onCreateHrSuccess();
                    }

                    @Override
                    protected void onError() {
                        view.onCreateHrSuccess();
                    }
                };

                break;
        }
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

        iCmd(1, file);
    }

    public void searchHospital(String key) {
        key = TextUtils.unicodeToKoDau(key);
        key = key.replaceAll(" ", "%20");
        iCmd(2, key);
    }

    public void continueHr(Object... params) {
        int mbrId = (int) params[0];
        String hrName = (String) params[1];
        String startDate = (String) params[2];
        String note = (String) params[3];
        String doctorName = (String) params[4];
        Hospital hospital = (Hospital) params[5];

//        List<Tag> disease   = view.getDiseaseList();
//        List<HealthRecordImages> images = view.getImageList();

        if (TextUtils.isEmpty(hrName)) {
            showError(301);
            return;
        }

//        Pattern p = Pattern.compile("[!@#$%^&*()_+=~`/?><]", Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(hrName);
//        boolean b = m.find();
//
//        if (b) {
//            showError(302);
//            return;
//        }

//        if (view.getDiseaseList().size() == 0) {
//            showError(302);
//            return;
//        }

        if (TextUtils.isEmpty(startDate)) {
            showError(303);
            return;
        }

        view.showProgressBar(2);

        long dateCreateLong = TimeUtils.convertDateToLong(startDate);

        view.getImageList();

        REQ_Hr_Create hrCreate = new REQ_Hr_Create();
        hrCreate.setName(hrName);
        hrCreate.setDateCreateLong(dateCreateLong);
        hrCreate.setNote(note);
        hrCreate.setMrbId(mbrId);
        hrCreate.setHospitalId(hospital.getId());
        hrCreate.setHospitalName(hospital.getName());
        hrCreate.setDoctorId(-1);
        hrCreate.setDoctorName(doctorName);

        iCmd(3, hrCreate);
    }

    public void checkAddDisease() {
        if (view.getDiseaseList().size() == 0) {
            checkAddImage();
            return;
        }

        iCmd(4);
    }

    public void checkAddImage() {
        if (view.getImageList().size() == 0) {
            iCmd(6);
            return;
        }

        iCmd(5);
    }

    public HealthRecords getHealthRecords() {
        return healthRecords;
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}