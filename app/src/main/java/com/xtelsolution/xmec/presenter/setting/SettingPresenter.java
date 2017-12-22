package com.xtelsolution.xmec.presenter.setting;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.SettingObject;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;
import com.xtelsolution.xmec.view.activity.inf.setting.ISettingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivu on 12/12/17
 * xtel.vn
 */

public class SettingPresenter extends BasicPresenter {

    private ISettingView view;

    public SettingPresenter(ISettingView basicView) {
        super(basicView);
        view = basicView;
    }

    @Override
    protected void getSessionSuccess(Object... params) {

    }

    public void initializingList() {
        List<SettingObject> settingList = new ArrayList<>();
        settingList.add(new SettingObject("Điều khoản sử dụng", R.mipmap.ic_policy, 1));
        settingList.add(new SettingObject("Góp ý", R.mipmap.ic_action_feedback, 2));
        settingList.add(new SettingObject("Đánh giá", R.mipmap.ic_thumb_up, 2));
//        settingList.add(new SettingObject("Yêu cầu hỗ trợ", R.mipmap.ic_question, 3));
        settingList.add(new SettingObject("Giới thiệu", R.mipmap.ic_group, 1));
        view.initializedSetting(settingList);
    }
}
