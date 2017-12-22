package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/19/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_ShareLink {
    @Expose
    private int id;
    @Expose
    private Integer uid;
//    @Expose
//    private String dateCreate;
    @Expose
    private Long dateCreateLong;
    @Expose
    private Integer mrbId;

    /**
     * 1 - Chia sẻ
     * 2 - Liên kết
     */
    @Expose
    private Integer shareType;

    /**
     * Chia sẻ
     * 1 - Hiện tại
     * 2 - Tương lai
     * Liên kết
     * 1 - Chỉ xem
     * 2 - Xem và sửa
     */
    @Expose
    private Integer shareTo;
    @Expose
    private Integer shareState;

    public REQ_ShareLink() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

//    public String getDateCreate() {
//        return dateCreate;
//    }
//
//    public void setDateCreate(String dateCreate) {
//        this.dateCreate = dateCreate;
//    }

    public Long getDateCreateLong() {
        return dateCreateLong;
    }

    public void setDateCreateLong(Long dateCreateLong) {
        this.dateCreateLong = dateCreateLong;
    }

    public Integer getMrbId() {
        return mrbId;
    }

    public void setMrbId(Integer mrbId) {
        this.mrbId = mrbId;
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }

    public Integer getShareTo() {
        return shareTo;
    }

    public void setShareTo(Integer shareTo) {
        this.shareTo = shareTo;
    }

    public Integer getShareState() {
        return shareState;
    }

    public void setShareState(Integer shareState) {
        this.shareState = shareState;
    }
}