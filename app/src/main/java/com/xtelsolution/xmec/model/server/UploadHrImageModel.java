package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Upload_Image;

import java.io.File;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class UploadHrImageModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private File file;

    protected UploadHrImageModel(File file) {
        this.file = file;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.UPLOAD_HR_IMAGE;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("UploadHrImageModel", url);
        MyApplication.log("UploadHrImageModel", session);

        basicModel.requestServer.uploadImage(url, session, file, new ResponseHandle<RESP_Upload_Image>(RESP_Upload_Image.class) {
            @Override
            protected void onSuccess(RESP_Upload_Image obj) {
                UploadHrImageModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                UploadHrImageModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(RESP_Upload_Image obj);

    protected abstract void onError(int errorCode);
}