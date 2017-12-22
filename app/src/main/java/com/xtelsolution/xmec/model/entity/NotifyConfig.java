package com.xtelsolution.xmec.model.entity;

public class NotifyConfig {

    /*Type notification*/
    public static final int TYPE_SHARE = 1;
    public static final int TYPE_LINK = 2;
    public static final int TYPE_SERVER = 3;

    /*Type share notification*/
    public static final int SHARE_PAST = 1;
    public static final int SHARE_FUTURE = 2;

    /*Type link notification*/
    public static final int LINK_ONLY_VIEW = 1;
    public static final int LINK_VIEW_EDIT = 2;

    /*Type state notification*/
    public static final int STATE_PENDING = 0;
    public static final int STATE_ACCEPT = 1;
    public static final int STATE_DECLINE = 2;

    public static final int TYPE_ACCEPTED = 4;
    public static final int TYPE_DECLINE = 5;
}