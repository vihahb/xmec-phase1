package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class ShareAccounts extends RealmObject implements Serializable {
    @Expose
    @PrimaryKey
    private int id;
    @Expose
    private int uid;
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
    @Expose
    private String fullname;
    @Expose
    private String avatar;
    /**
     * 0 - Pending
     * 1 - Đồng ý
     * 2 - Từ chối
     */
    @Expose
    private int shareState;

    public ShareAccounts() {
    }

    public ShareAccounts(int id, Friends friends, int shareType) {
        this.id = id;
        uid = friends.getUid();
        this.shareType = shareType;
        shareTo = friends.getShareTo();
        fullname = friends.getFullname();
        avatar = friends.getAvatar();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public int getShareTo() {
        return shareTo;
    }

    public void setShareTo(int shareTo) {
        this.shareTo = shareTo;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getShareState() {
        return shareState;
    }

    public void setShareState(int shareState) {
        this.shareState = shareState;
    }

    @Override
    public String toString() {
        return "ShareAccounts{" +
                "id=" + id +
                ", uid=" + uid +
                ", shareType=" + shareType +
                ", shareTo=" + shareTo +
                ", fullname='" + fullname + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}