package com.xtelsolution.xmec.view.fragment.hospital;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.master.permissionhelper.PermissionHelper;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.ConvertUtils;
import com.xtelsolution.sdk.utils.MapUtil;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.view.activity.HomeActivity;

import static com.xtelsolution.sdk.commons.Constants.REQUEST_CHECK_SETTINGS;

/**
 * Created by Admin on 4/10/2017.
 */

public class BaseMapFragment extends Fragment implements OnMapReadyCallback,
        LocationListener,
        OnMyLocationUpdated {
    private String TAG = "BaseMapFragment";
    protected BroadcastReceiver connectReceiver;
    protected PermissionHelper helper;
    protected GoogleMap mMap;
    protected Location mCurrentLocation;
    protected OnMyLocationUpdated onMyLocationUpdated;
    protected Marker myMarker;
    protected LatLng myLatLng, latLngOld = new LatLng(0, 0);
    private LocationRequest mLocationRequest;

    private Exception exception;

    private final long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private final long FASTEST_INTERVAL = 5000; /* 2 sec */
    private int statusCode = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new PermissionHelper(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.MY_PERMISSIONS_REQUEST_LOCATION);
        onMyLocationUpdated = this;

        startLocationUpdates(((HomeActivity) getActivity()).getViewpager().getCurrentItem() == 4);
        initReceiver();
    }

    // Trigger new location updates at interval
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates(final boolean requet) {
        Log.e(TAG, "startLocationUpdates: statusCode= " + statusCode);
        if (statusCode == CommonStatusCodes.RESOLUTION_REQUIRED && exception != null) {
            reuestSetting((ResolvableApiException) exception);
            onMyLocationUpdated.onGetLocationError(Constants.ERROR_OFF_LOCATION);
            return;
        }

        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onMyLocationUpdated.onGetLocationError(Constants.ERROR_OFF_LOCATION);
//            return;
        } else {
            onMyLocationUpdated.onGetLocationError(0);
        }


        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (checkPermissions(requet)) {
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
            LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            Log.e(TAG, "onLocationResult: ");
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    },
                    Looper.myLooper());
            task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    getLastLocation(requet);
                    Log.e(TAG, "onSuccess: " + locationSettingsResponse.getLocationSettingsStates());
                    exception = null;
                    statusCode = 0;
                }
            });

            task.addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    statusCode = ((ApiException) e).getStatusCode();
                    exception = e;
                    Log.e(TAG, "onFailure: " + statusCode);
                    switch (statusCode) {
                        case CommonStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed
                            // by showing the user a dialog.
//
                            onMyLocationUpdated.onGetLocationError(Constants.ERROR_OFF_LOCATION);

                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way
                            // to fix the settings so we won't show the dialog.

                            break;
                    }
                }
            });
        } else {
            onMyLocationUpdated.onGetLocationError(Constants.ERROR_PERMISSION_LOCATION);
        }
    }

    private void reuestSetting(@NonNull ResolvableApiException e) {
        try {
            ResolvableApiException resolvable = e;
            resolvable.startResolutionForResult(getActivity(),
                    Constants.MY_PERMISSIONS_REQUEST_LOCATION);
        } catch (IntentSender.SendIntentException sendEx) {
            // Ignore the error.
        }
    }


    private void initReceiver() {
        connectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.intent.action.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(connectReceiver, filter);
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation(boolean request) {
        // Get last known recent location using new Google Play Services SDK (v11+)
        if (checkPermissions(request)) {
            FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                onLocationChanged(location);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        } else {
            onGetLocationError(Constants.ERROR_PERMISSION_LOCATION);
        }
    }


    protected boolean isCurrentPage() {
        if (((HomeActivity) getActivity()).getViewpager() != null) {
            Log.e(TAG, "isCurrentPage: " + (((HomeActivity) getActivity()).getViewpager().getCurrentItem() == 4));
            return ((HomeActivity) getActivity()).getViewpager().getCurrentItem() == 4;
        } else return false;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (checkPermissions(true)) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private boolean checkPermissions(boolean reqeust) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (reqeust)
                requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        Log.e(TAG, "requestPermissions: ");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isCurrentPage()) return;
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }, 100);

    }


    @Override
    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.e(TAG, "onLocationChanged: " + msg);
//        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        mCurrentLocation = location;

        upDateUILoaction(location, false, false);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    protected void upDateUILoaction(Location location, boolean moveCamera, boolean animate) {
                /*
        * Người sửa: Lê Công Long VŨ
        * Lý do: sau khi load tab này, người dùng chuyển sang tab khác bị crash
        * Nguyên nhân: sau khi tab và các view bị destroy, abc gì đó gọi lại medthod này khi mMap null
        * */
        try {
            if (mMap != null && isCurrentPage()) {
                if (location == null) return;
                myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (myMarker != null) myMarker.remove();
                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(myLatLng)
//                    .flat(true)
                        .title(getString(R.string.txt_myLocation))
//                    .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromBitmap(MapUtil.resizeMapIcons("marker_my_location",
                                ConvertUtils.dpToPixels(getActivity(), 36),
                                ConvertUtils.dpToPixels(getActivity(), 36),
                                getActivity()))));
                if (MapUtil.calculationByDistance(myLatLng, latLngOld) > 2.5) {
                    onMyLocationUpdated.myLocationUpdated(location);
                }
                if (MapUtil.calculationByDistance(latLngOld, myLatLng) > 10) {
                    latLngOld = myLatLng;
                    MapUtil.getNamePlaceByLatlng(getActivity(), myMarker.getPosition(), new MapUtil.OnNamePlaceBuyLatlng() {
                        @Override
                        public void onName(String name, String address, LatLng latLng) {
                            myMarker.setSnippet(address);
                        }

                        @Override
                        public void error() {

                        }
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void myLocationUpdated(Location location) {
    }

    @Override
    public void onGetLocationError(int code) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {

            getActivity().unregisterReceiver(connectReceiver);
        } catch (Exception e) {
            Log.e(TAG, "onDestroy: ", new Throwable(e));
        }
    }

}
