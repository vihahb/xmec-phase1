package com.xtelsolution.sdk.callback;

import android.util.Log;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class AbsICmd implements ICmd {

    private static final String TAG = "AbsICmd";

    @Override
    public void run() {
        try {
            invoke();
        } catch (Exception e) {
            Log.e(TAG, "run Exception: " + e.toString());
            exception(e.getMessage());
        }
    }

    protected abstract void invoke();

    protected abstract void exception(String message);
}