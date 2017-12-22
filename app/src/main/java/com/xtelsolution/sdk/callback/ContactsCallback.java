package com.xtelsolution.sdk.callback;

import com.xtelsolution.xmec.model.entity.Contact_Model;

import java.util.List;

/**
 * Created by vivu on 11/24/17.
 */

public interface ContactsCallback {
    void processSuccess(List<Contact_Model> list);
    void processError(String message);
}
