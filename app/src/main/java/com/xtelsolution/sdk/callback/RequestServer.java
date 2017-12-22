package com.xtelsolution.sdk.callback;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.util.Charsets;
import com.koushikdutta.ion.Ion;
import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.model.resp.RESP_NewCategory;

import java.io.File;

/**
 * Created by Lê Công Long Vũ on 12/2/2016
 */

public class RequestServer {

    public void getApi(String url, final ResponseHandle responseHandle) {
        Ion.with(MyApplication.context)
                .load("GET", url)
                .setTimeout(30000)
                .asString(Charsets.UTF_8)
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null)
                            responseHandle.onError(e);
                        else
                            responseHandle.onSuccess(result);
                    }
                });
    }

    public void postApi(String url, String body, final ResponseHandle responseHandle) {
        Ion.with(MyApplication.context)
                .load("POST", url)
                .setTimeout(30000)
                .setStringBody(body)
                .asString(Charsets.UTF_8)
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null)
                            responseHandle.onError(e);
                        else
                            responseHandle.onSuccess(result);
                    }
                });
    }

    public void putApi(String url, String body, final ResponseHandle responseHandle) {
        Ion.with(MyApplication.context)
                .load("PUT", url)
                .setTimeout(30000)
                .setStringBody(body)
                .asString(Charsets.UTF_8)
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null)
                            responseHandle.onError(e);
                        else
                            responseHandle.onSuccess(result);
                    }
                });
    }

    public void uploadImage(String url, File file, final ResponseHandle responseHandle) {
        Ion.with(MyApplication.context)
                .load("POST", url)
                .setTimeout(60000)
                .setMultipartFile("image", file)
                .asString(Charsets.UTF_8)
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null)
                            responseHandle.onError(e);
                        else
                            responseHandle.onSuccess(result);
                    }
                });
    }

//    public void uploadImage(String url, File file, String json, final ResponseHandle responseHandle) {
//        Ion.with(MyApplication.context)
//                .load("POST", url)
//                .setTimeout(60000)
//                .setMultipartFile("image", file)
//                .addMultipartParts(new StringPart("chophi", json))
//                .asString()
//                .setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String result) {
//                        if (e != null)
//                            responseHandle.onError(e);
//                        else
//                            responseHandle.onSuccess(result);
//                    }
//                });
//    }



    public void getApi(String url, String session, final ResponseHandle responseHandle) {
        Ion.with(MyApplication.context)
                .load("GET", url)
                .setTimeout(30000)
                .setHeader(Constants.SESSION, session)
                .asString(Charsets.UTF_8)
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null)
                            responseHandle.onError(e);
                        else
                            responseHandle.onSuccess(result);
                    }
                });
    }

    public void postApi(String url, String session, String body, final ResponseHandle responseHandle) {
        Ion.with(MyApplication.context)
                .load("POST", url)
                .setTimeout(30000)
                .setHeader(Constants.SESSION, session)
                .setStringBody(body)
                .asString(Charsets.UTF_8)
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null)
                            responseHandle.onError(e);
                        else
                            responseHandle.onSuccess(result);
                    }
                });
    }

    public void putApi(String url, String session, String body, final ResponseHandle responseHandle) {
        Ion.with(MyApplication.context)
                .load("PUT", url)
                .setTimeout(30000)
                .setHeader(Constants.SESSION, session)
                .setStringBody(body)
                .asString(Charsets.UTF_8)
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null)
                            responseHandle.onError(e);
                        else
                            responseHandle.onSuccess(result);
                    }
                });
    }

    public void deleteApi(String url, String session, String body, final ResponseHandle responseHandle) {
        Ion.with(MyApplication.context)
                .load("DELETE", url)
                .setTimeout(30000)
                .setHeader(Constants.SESSION, session)
                .setStringBody(body)
                .asString(Charsets.UTF_8)
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null)
                            responseHandle.onError(e);
                        else
                            responseHandle.onSuccess(result);
                    }
                });
    }


    public void uploadImage(String url, String session, File file, final ResponseHandle responseHandle) {
        Ion.with(MyApplication.context)
                .load("POST", url)
                .setTimeout(60000)
                .setHeader(Constants.SESSION, session)
                .setMultipartFile("file", file)
                .asString(Charsets.UTF_8)
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null)
                            responseHandle.onError(e);
                        else
                            responseHandle.onSuccess(result);
                    }
                });
    }


//    public void uploadImage(String url, String session, File file, String json, final ResponseHandle responseHandle) {
//        Ion.with(MyApplication.context)
//                .load("POST", url)
//                .setTimeout(60000)
//                .setHeader(Constants.SESSION, session)
//                .setMultipartFile("image", file)
//                .addMultipartParts(new StringPart("chophi", json))
//                .asString()
//                .setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String result) {
//                        if (e != null)
//                            responseHandle.onError(e);
//                        else
//                            responseHandle.onSuccess(result);
//                    }
//                });
//    }
}
