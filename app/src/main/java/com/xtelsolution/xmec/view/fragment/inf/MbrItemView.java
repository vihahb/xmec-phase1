package com.xtelsolution.xmec.view.fragment.inf;

import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

/**
 * Created by vivu on 12/6/17
 * xtel.vn
 */

public interface MbrItemView extends IBasicView{
    void removeShareLink(int id_share, int action_type, int mbrId);

    void notifyData(Mbr mbr);
}
