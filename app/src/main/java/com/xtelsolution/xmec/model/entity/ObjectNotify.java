package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xtel on 11/8/17.
 */

public class ObjectNotify extends RealmObject{
    @PrimaryKey
    @Expose
    private int id; // id thông báo
    @Expose
    private String dateCreated; // ngày khởi tạo
    @Expose
    private long dateCreatedLong; // ngày khởi tạo kiểu long milisecond
    @Expose
    private int notifyType;  // kiểu notify (1: chia sẻ; 2 liên kết; 3: thông báo từ server)
    @Expose
    private String notifyTitle; // tiêu đề
    @Expose
    private String notifyContent; // nội dung
    @Expose
    private String contentUrl; //Url (nếu có)
    @Expose
    private Integer senderUid; // Id người gửi thông báo
    @Expose
    private String senderName;// Tên người gửi thông báo
    @Expose
    private int mrbId; // Id y bạ
    @Expose
    private int share_id; //Share id
    @Expose
    private String mrbName; // Tên y bạ
    @Expose
    private Integer uid; // uid người nhận
    @Expose
    private String avatar; // link avatar, null hiển thị icon mặc định
    @Expose
    private int actionState; // Trạng thái hành động: 0: Pending; 1: Đồng ý; 2: Hủy
    @Expose
    private Integer notifyState; // Trạng thái share: 0: Chưa xem thông báo; 1: Đã xem thông báo;

    private int stateHightLight = 0;

    public ObjectNotify() {
    }

    public int getShare_id() {
        return share_id;
    }

    public void setShare_id(int share_id) {
        this.share_id = share_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateCreatedLong() {
        return dateCreatedLong;
    }

    public void setDateCreatedLong(long dateCreatedLong) {
        this.dateCreatedLong = dateCreatedLong;
    }

    public int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyTitle() {
        return notifyTitle;
    }

    public void setNotifyTitle(String notifyTitle) {
        this.notifyTitle = notifyTitle;
    }

    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Integer getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(Integer senderUid) {
        this.senderUid = senderUid;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getMrbId() {
        return mrbId;
    }

    public void setMrbId(int mrbId) {
        this.mrbId = mrbId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getActionState() {
        return actionState;
    }

    public void setActionState(int actionState) {
        this.actionState = actionState;
    }

    public Integer getNotifyState() {
        return notifyState;
    }

    public void setNotifyState(Integer notifyState) {
        this.notifyState = notifyState;
    }

    public int getStateHightLight() {
        return stateHightLight;
    }

    public void setStateHightLight(int stateHightLight) {
        this.stateHightLight = stateHightLight;
    }

    public String getMrbName() {
        return mrbName;
    }

    public void setMrbName(String mrbName) {
        this.mrbName = mrbName;
    }

    @Override
    public String toString() {
        return "ObjectNotify{" +
                "id=" + id +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateCreatedLong=" + dateCreatedLong +
                ", notifyType=" + notifyType +
                ", notifyTitle='" + notifyTitle + '\'' +
                ", notifyContent='" + notifyContent + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", senderUid=" + senderUid +
                ", senderName='" + senderName + '\'' +
                ", mrbId=" + mrbId +
                ", mrbName='" + mrbName + '\'' +
                ", uid=" + uid +
                ", avatar='" + avatar + '\'' +
                ", actionState=" + actionState +
                ", notifyState=" + notifyState +
                ", stateHightLight=" + stateHightLight +
                '}';
    }
}
