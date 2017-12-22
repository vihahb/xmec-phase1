package com.xtelsolution.xmec.view.activity.mapview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.DisplayUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.MapInfo;
import com.xtelsolution.xmec.view.widget.BubbleTransformation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.img_close)
    ImageView imgClose;

    @OnClick(R.id.img_close)
    public void closeMap() {
        finish();
    }

    private GoogleMap mMap;
    private MapInfo mapInfo;
    Target mTarget;
    Marker hospital_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getData();
    }

    private void getData() {
        mapInfo = (MapInfo) getIntent().getSerializableExtra(Constants.MAP_INFO);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mapInfo != null) {
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mMap.setBuildingsEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                mTarget = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        hospital_marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(mapInfo.getLat(), mapInfo.getLng()))
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .title(mapInfo.getName())
                                .snippet(mapInfo.getAddress())
                        );
                        hospital_marker.showInfoWindow();
                        mMap.getUiSettings().setMapToolbarEnabled(true);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.d("picasso", "onBitmapFailed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                if (mapInfo.getImage()!=null){
                    Picasso.with(this)
                            .load(mapInfo.getImage())
                            .resize(DisplayUtils.convertDpToPixels(86, getApplicationContext()), DisplayUtils.convertDpToPixels(86, getApplicationContext()))
                            .centerCrop()
                            .transform(new BubbleTransformation(5))
                            .into(mTarget);
                } else {
                    Picasso.with(this)
                            .load(R.mipmap.ic_small_avatar_default)
                            .resize(DisplayUtils.convertDpToPixels(86, getApplicationContext()), DisplayUtils.convertDpToPixels(86, getApplicationContext()))
                            .centerCrop()
                            .transform(new BubbleTransformation(5))
                            .into(mTarget);
                }


                LatLng hospital_position = new LatLng(mapInfo.getLat(), mapInfo.getLng());
//                mMap.addMarker(new MarkerOptions().position(hospital_position).title(mapInfo.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospital_position, 15));
                mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
            }
        } else {
            // Add a marker in Hanoi and move the camera
            LatLng HANOI = new LatLng(21.033333, 105.849998);
            mMap.addMarker(new MarkerOptions().position(HANOI).title("Marker in Hanoi"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HANOI, 15));
        }
    }
}
