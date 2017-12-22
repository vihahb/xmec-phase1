package com.xtelsolution.xmec.view.fragment;

import android.view.View;

/**
 * Created by Vulcl on 5/5/2017
 */

public class IFragment extends BasicFragment {

    protected View view;
    protected boolean isWatched = false;

    /**
     * Kiểm tra xem có phải view được khởi tạo lần đầu
     * @param view
     * @return
     */
    protected View onCheckCreateView(View view) {
        if (this.view == null)
            this.view = view;

        return this.view;
    }

    /**
     * Kiểm tra xem có phải Fragment được khởi tạo lần đầu
     * @return
     */
    protected boolean isViewCreated() {
        if (!isWatched) {
            isWatched = true;
            return false;
        }

        return true;
    }
}
