package com.xtelsolution.xmec.view.fragment.hospital;

import android.location.Location;

/**
 * Created by ThanhChung on 12/5/17.
 */

interface OnMyLocationUpdated {
    void myLocationUpdated(Location location);

    void onGetLocationError(int code);
}
