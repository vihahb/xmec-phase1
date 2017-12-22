package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Created by vivu on 12/16/17
 * xtel.vn
 */

public class RESP_ValidatePhone extends RESP_Basic {

    @Expose
    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "RESP_ValidatePhone{" +
                "valid=" + valid +
                '}';
    }
}
