package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by xtel on 11/15/17.
 */

public class UpdateShare implements Serializable {

    @Expose
    private int id;
    /**
     * 1 - Chia sẻ
     * 2 - Liên kết
     */
    @Expose
    private int shareType;
    /**
     * Chia sẻ
     * 1 - Hiện tại
     * 2 - Tương lai
     * Liên kết
     * 1 - Chỉ xem
     * 2 - Xem và sửa
     */
    @Expose
    private int shareTo;

    public UpdateShare() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShareTo() {
        return shareTo;
    }

    public void setShareTo(int shareTo) {
        this.shareTo = shareTo;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    @Override
    public String toString() {
        return "UpdateShare{" +
                "id=" + id +
                ", shareTo=" + shareTo +
                ", shareType=" + shareType +
                '}';
    }
}
