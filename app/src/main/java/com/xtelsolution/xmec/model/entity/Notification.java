package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/6/2017
 * Email: leconglongvu@gmail.com
 */
public class Notification {
    /**
     * id người gửi notify
     */
    @Expose
    private Integer uid;
    /**
     * Tên người gửi notify
     */
    @Expose
    private String name;
    /**
     * 1: Chia sẻ
     * 2: Liên kết
     * 3: Đồng bộ
     */
    @Expose
    private Integer type;
    /**
     * Hành động
     */
    @Expose
    private Integer action;
    /**
     * id y bạ
     */
    @Expose
    private Integer mrb_id;
    /**
     * Chia sẻ:  1: Hiện tại; 2: Tương lai
     * Liên kết: 1: Chỉ xem; 2: Xem và sửa
     */
    @Expose
    private Integer action_type;
    /**
     * Trạng thái share: 0: Pending; 1: Đồng ý; 2: Hủy
     */
    @Expose
    private Integer state;

    public Notification() {
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getMrb_id() {
        return mrb_id;
    }

    public void setMrb_id(Integer mrb_id) {
        this.mrb_id = mrb_id;
    }

    public Integer getAction_type() {
        return action_type;
    }

    public void setAction_type(Integer action_type) {
        this.action_type = action_type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}