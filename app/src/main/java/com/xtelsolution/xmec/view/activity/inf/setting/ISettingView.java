package com.xtelsolution.xmec.view.activity.inf.setting;

import com.xtelsolution.xmec.model.entity.SettingObject;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Created by vivu on 12/12/17
 * xtel.vn
 */

public interface ISettingView extends IBasicView{
    void initializedSetting(List<SettingObject> settingList);
}
