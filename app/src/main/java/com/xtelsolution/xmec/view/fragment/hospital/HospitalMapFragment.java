package com.xtelsolution.xmec.view.fragment.hospital;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.xtelsolution.sdk.callback.OnItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.MapUtil;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.PermissionHelper;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.Utils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.hospital.HospitalInMapAdapter;
import com.xtelsolution.xmec.adapter.hospital.HospitalSearchAdapter;
import com.xtelsolution.xmec.adapter.hospital.SpinnerTypeHospitalAdapter;
import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.model.entity.HospitalTypeModel;
import com.xtelsolution.xmec.presenter.hospital.HospitalPresenter;
import com.xtelsolution.xmec.view.activity.HomeActivity;
import com.xtelsolution.xmec.view.activity.hospitalInfo.HospitalInfoActivity;
import com.xtelsolution.xmec.view.activity.inf.hospital.IHospitalView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xtelsolution.sdk.commons.Constants.HOSPITAL_TYPE;
import static com.xtelsolution.sdk.commons.Constants.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.xtelsolution.sdk.commons.Constants.REQUEST_CHECK_SETTINGS;
import static com.xtelsolution.sdk.commons.Constants.SEARCH_HOSPITALBY_LATLNG;

/**
 * Created by ThanhChung on 07/11/2017.
 */

public class HospitalMapFragment extends BaseMapFragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback,
        OnItemClickListener<Hospital>, DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>,
        IHospitalView, View.OnClickListener, GoogleMap.OnMapClickListener {
    private static final String TAG = "HospitalMapFragment";
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.sp_hospital_type)
    Spinner spHospitalType;
    @BindView(R.id.rc_hospital_map)
    DiscreteScrollView rcHospitalMap;
    @BindView(R.id.progressLocation)
    ProgressBar progressLocation;
    @BindView(R.id.message_error_location)
    TextView messageErrorLocation;
    @BindView(R.id.btn_error_location)
    Button btnErrorLocation;
    @BindView(R.id.ela_error_location)
    LinearLayout elaErrorLocation;
    @BindView(R.id.expandable_layout)
    ExpandableLayout expandableLayout;
    @BindView(R.id.fab_location)
    FloatingActionButton fabLocation;
    @BindView(R.id.dragView)
    LinearLayout dragView;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    Unbinder unbinder;


    private HospitalPresenter presenter;

    private List<Hospital> list;

    private SpinnerTypeHospitalAdapter hospitalAdapter;
    private HospitalInMapAdapter adapter;

    private int heightPanel = 0;
    private int errorLocationCode = 0;
    private int typePremissionError = 0;

    public static HospitalMapFragment newInstance() {

        Bundle args = new Bundle();

        HospitalMapFragment fragment = new HospitalMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //    private GoogleMap mMap;
    private SearchView searchView;
    //  Lưu cụ bộ giá trị text trong ô search
    private String curentTextSearch = "";

    private List<Marker> markers;

    private BroadcastReceiver receiver;

    private List<HospitalTypeModel> typeModels;
    private SearchHospitalFragment searchHospitalFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        list = new ArrayList<>();

        markers = new ArrayList<>();
        adapter = new HospitalInMapAdapter(list, getContext());
        adapter.setItemClickListener(HospitalMapFragment.this);

        presenter = new HospitalPresenter(HospitalMapFragment.this);
        typeModels = MapUtil.getListHospitalType();
        hospitalAdapter = new SpinnerTypeHospitalAdapter(getActivity(), typeModels);
        searchHospitalFragment = new SearchHospitalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hospital_map_fragment, container, false);

        getChildFragmentManager().beginTransaction().add(R.id.dragContent, searchHospitalFragment, null).commit();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcHospitalMap.setSlideOnFlingThreshold(10);
        rcHospitalMap.setAdapter(adapter);
        rcHospitalMap.addOnItemChangedListener(HospitalMapFragment.this);
        rcHospitalMap.setItemTransitionTimeMillis(100);

        spHospitalType.setAdapter(hospitalAdapter);

        initUI();
        initSliding();
        initMap(savedInstanceState);
        initReceiver();
    }


    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null) {
//                    if (intent.getAction().equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                        int type = intent.getIntExtra("type", 0);
//                        // type == 1: quyền được cho phép
//                        // type == 0: quyền bị từ chối
//                        // type == -1: quyền từ chối mãi mãi
//                        Log.e(TAG, "onReceive: type= " + type);
//                        typePremissionError = type;
//                        if (type == 1) {
////                            if (isCurrentPage())
//                            //tracker.getLocation();
//                        } else if (type == 0) {
//
//                        } else {
//
//                        }
//
//                    }
                    if (intent.getAction().matches("REQUEST_SETTINGS_GPS")) {

                        if (intent.getIntExtra("action", 0) == Activity.RESULT_OK) {
                            if (NetworkUtils.isConnected2(getContext())) {
                                errorLocationCode = 0;
                            } else {
                                errorLocationCode = Constants.ERROR_NO_INTERNET;
                            }
                        } else {
                            errorLocationCode = Constants.ERROR_OFF_LOCATION;
                        }
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(Manifest.permission.ACCESS_COARSE_LOCATION);
        filter.addAction("REQUEST_SETTINGS_GPS");
        getActivity().registerReceiver(receiver, filter);
    }


    private void initUI() {
        btnErrorLocation.setOnClickListener(this);
        fabLocation.setOnClickListener(this);
        messageErrorLocation.setOnClickListener(this);
        messageErrorLocation.setVisibility(View.GONE);
        btnErrorLocation.setVisibility(View.GONE);
        setTypeSpinner();

        spHospitalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isCurrentPage()) {
                    if (myLatLng == null) startLocationUpdates(true);
                    else {
                        presenter.getHospitalRadius(myLatLng, hospitalAdapter.getItem(position).getId());
                    }
                    SharedUtils.getInstance().putIntValue(HOSPITAL_TYPE, hospitalAdapter.getItem(position).getId());
                    Log.e(TAG, "onItemSelected: " + hospitalAdapter.getItem(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setTypeSpinner() {
        for (int i = 0; i < typeModels.size(); i++) {
            if (typeModels.get(i).getId() == SharedUtils.getInstance().getIntValue(HOSPITAL_TYPE, 1))
                spHospitalType.setSelection(i);
        }
    }

    private void initSliding() {
        slidingLayout.setVerticalScrollBarEnabled(false);
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.e(TAG, "onPanelStateChanged: " + newState);
                if (newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    slidingLayout.setPanelHeight(0);
                    if (!searchView.isIconified()) {
                        searchView.setIconified(true);

                    }
                    if (!searchView.isIconified())
                        searchView.setIconified(true);
                    slidingLayout.setPanelHeight(0);

                }
                if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
                    if (heightPanel != 0)
                        slidingLayout.setPanelHeight(heightPanel);
                }
                if (newState.equals(SlidingUpPanelLayout.PanelState.HIDDEN)) {
                    slidingLayout.setPanelHeight(0);
                }
            }
        });
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    private void initMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);

    }

    private MenuItem notifi;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_hospital, menu);
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        notifi = menu.findItem(R.id.action_notify);
        searchView = (SearchView) mSearchMenuItem.getActionView();
        initSearchView();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                getActivity().onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //    Khởi tạo các sự kiện của searchView
    private void initSearchView() {
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.e(TAG, "onClose: ");

                setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG, "onFocusChange: " + hasFocus);
                if (!hasFocus && curentTextSearch.length() == 0 && !searchView.isIconified()) {
                    searchView.setIconified(true);
                    if (searchHospitalFragment.size() == 0)
                        setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    hideIconNotifi(true);
                }
                if (hasFocus) {
                    setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    hideIconNotifi(false);
                }

            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Log.e(TAG, "onSuggestionSelect: ");
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Log.e(TAG, "onSuggestionClick: ");
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: ");
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, "onQueryTextSubmit: " + query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG, "onQueryTextChange: ");
                curentTextSearch = newText;
                searchHospital(newText);
                return false;
            }
        });

    }

    private void setPanelState(SlidingUpPanelLayout.PanelState state) {
        if (slidingLayout != null)
            slidingLayout.setPanelState(state);
        if (state.equals(SlidingUpPanelLayout.PanelState.HIDDEN)) {
            slidingLayout.setPanelHeight(0);
        }
    }

    private void hideIconNotifi(final boolean visible) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (notifi != null) notifi.setVisible(visible);
            }
        });

    }

    private void searchHospital(String key) {
        getActivity().sendBroadcast(new Intent(Constants.SEARCH_KEY).putExtra(Constants.SEARCH_KEY, key));

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();


        }
        Log.e(TAG, "onResume: errorLocationCode=" + errorLocationCode);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    private void forcusCamera(LatLng sydney, boolean animate) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(16).build();
        if (animate) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private void setMarker() {
        MapUtil.removerAmination();
        mMap.clear();
        markers.clear();

        for (Hospital hospital : list) {
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(hospital.getLat(), hospital.getLng()))
                    .title(hospital.getName())
                    .icon(MapUtil.getIconMarkerHospital(hospital.getType()));

            Marker marker = mMap.addMarker(options);
            marker.setTag(hospital);
            markers.add(marker);
        }
        upDateUILoaction(mCurrentLocation, false, false);
        if (markers.size() > 0) {
            rcHospitalMap.smoothScrollToPosition(0);
        }


    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        try {
            if (!marker.equals(myMarker)) {
                {
                    Hospital hospital = (Hospital) marker.getTag();
                    rcHospitalMap.smoothScrollToPosition(list.indexOf(hospital));
                    MapUtil.addAnimationCricle(mMap, marker.getPosition());
                    expandableLayout.expand(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        elaErrorLocation.setVisibility(View.GONE);
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //tracker.getLocation();
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(20.9230698, 105.6144126), 10.0f));

        mapView.post(new Runnable() {
            @Override
            public void run() {
                int width = mapView.getMeasuredWidth();
                int height = mapView.getMeasuredHeight();

                String widthAndHeight = width + " " + height;
                Log.e(TAG, "run: " + widthAndHeight);
                heightPanel = height;
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });
    }


    @Override
    public void onClick(int position, Hospital item) {
        if (rcHospitalMap.getCurrentItem() == position) {
            HospitalInfoActivity.start(getContext(), item.getId());
        }
        rcHospitalMap.smoothScrollToPosition(position);
        if (!expandableLayout.isExpanded()) expandableLayout.expand(true);
        forcusMarker(position);

    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        forcusMarker(adapterPosition);
    }

    private void forcusMarker(int adapterPosition) {
        for (Marker marker : markers) {
            try {
                if (marker.getTag().equals(list.get(adapterPosition))) {
                    marker.showInfoWindow();
                    forcusCamera(list.get(adapterPosition).getPosition(), true);
                    MapUtil.addAnimationCricle(mMap, marker.getPosition());
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSearchHospitalSuccess(List<Hospital> hospitalList, String key) {

    }

    @Override
    public void onGetHospitalAroundSuccess(List<Hospital> hospitalList) {
        try {
            list.clear();
            if (hospitalList.size() > 0) {
                list.addAll(hospitalList);
                try {
                    Collections.sort(list, new Comparator<Hospital>() {
                        @Override
                        public int compare(Hospital o1, Hospital o2) {
                            try {
                                if (o1.getLat() == null && o2.getLat() == null) return 1;
                                if (o1.getLat() == null && o2.getLat() != null) return -1;
                                if (o1.getLat() != null && o2.getLat() == null) return 1;
                                double d = (MapUtil.calculationByDistanceM(myLatLng, new LatLng(o1.getLat(), o1.getLng())) - MapUtil.calculationByDistanceM(myLatLng, new LatLng(o2.getLat(), o2.getLng())));
                                return (d == 0) ? 0 : (d > 0) ? 1 : -1;
                            } catch (Exception e) {
                                return 1;
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.setMylocation(myLatLng);
                adapter.notifyDataSetChanged();

                progressLocation.setVisibility(View.GONE);
                rcHospitalMap.setVisibility(View.VISIBLE);
                elaErrorLocation.setVisibility(View.GONE);
            } else {
                onError(Constants.ERROR_EMPTY);
            }
            setMarker();
        } catch (Exception e) {
            //        tránh trường hợp khi fragment destroy khi chưa load dữ liệu xong => view null
            e.printStackTrace();
        }

    }

    @Override
    public void showProgressBar(int type) {
        //        tránh trường hợp khi fragment destroy khi chưa load dữ liệu xong => view null
        try {
            if (type == SEARCH_HOSPITALBY_LATLNG) {
                rcHospitalMap.setVisibility(View.GONE);
                btnErrorLocation.setVisibility(View.GONE);
                elaErrorLocation.setVisibility(View.VISIBLE);
                progressLocation.setVisibility(View.VISIBLE);
                messageErrorLocation.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int code) {
        Log.e(TAG, "onError: " + code);
        runError(code);

    }

    @Override
    public void showLongToast(String message) {

    }

    @Override
    public void onEndSession() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_error_location:
                if (errorLocationCode == Constants.ERROR_OFF_LOCATION) {
                    startLocationUpdates(true);
                } else if (errorLocationCode == Constants.ERROR_PERMISSION_LOCATION) {
                    if (typePremissionError == 0) {
                        startLocationUpdates(true);
                    } else {
                        startActivity(Utils.newAppDetailsIntent(getContext()));
                    }
                } else {
                    if (myLatLng != null) {
                        presenter.getHospitalRadius(myLatLng, SharedUtils.getInstance().getIntValue(HOSPITAL_TYPE, 1));
                    }
                }
                break;
            case R.id.message_error_location:
                if (errorLocationCode == Constants.ERROR_NO_INTERNET) {
                    showProgressBar(SEARCH_HOSPITALBY_LATLNG);
                    if (NetworkUtils.isConnected(getContext())) {
                        if (myLatLng == null) {
                            startLocationUpdates(true);
                        } else
                            presenter.getHospitalRadius(myLatLng, SharedUtils.getInstance().getIntValue(HOSPITAL_TYPE, 1));
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onGetLocationError(Constants.ERROR_NO_INTERNET);
                            }
                        }, 500);
                    }
                }
                break;
            case R.id.fab_location:
                if (myLatLng != null) {
                    forcusCamera(myLatLng, true);
                    getLastLocation(true);
                } else {
                    startLocationUpdates(true);
                }

                break;
        }
    }

    private boolean _hasLoadedOnce = false;

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);

        if (this.isVisible()) {
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                if (isCurrentPage() && list.size() == 0 && presenter != null) {
                    //tracker.getLocation();
                    Log.e(TAG, "setUserVisibleHint: ");
                    if (errorLocationCode != 0 && isCurrentPage()) {
                        showProgressBar(SEARCH_HOSPITALBY_LATLNG);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (HospitalMapFragment.this.errorLocationCode == Constants.ERROR_OFF_LOCATION) {
                                    Utils.showToast(getString(R.string.error_off_location));
                                }
                                if (HospitalMapFragment.this.errorLocationCode == Constants.ERROR_NO_INTERNET) {
                                    Utils.showToast(getString(R.string.error_no_internet));
                                }

                                if (!expandableLayout.isExpanded()) expandableLayout.expand();
                                onGetLocationError(errorLocationCode);
                            }
                        }, 1000);
                    }

                }
                _hasLoadedOnce = true;
            }
        }
    }

    public void onBackPressed() {
        if (slidingLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        } else expandableLayout.expand(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (com.xtelsolution.sdk.utils.PermissionHelper.checkResult(grantResults)) {
                Log.e(TAG, "onRequestPermissionsResult: OK");
                if (!NetworkUtils.isConnected(getActivity())) {
                    onGetLocationError(Constants.ERROR_NO_INTERNET);
                } else {
                    startLocationUpdates(false);
                }
            } else {
                Log.e(TAG, "onRequestPermissionsResult: CANCEL");
                onGetLocationError(Constants.ERROR_PERMISSION_LOCATION);
            }
            try {
                if (PermissionHelper.isAllowPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getActivity()))
                    typePremissionError = 1;
                else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
//                    Toast.makeText(getActivity(), "Go to Settings and Grant the permission to use this feature.", Toast.LENGTH_SHORT).show();
                    typePremissionError = -1;
                    // User selected the Never Ask Again Option
                } else {
                    typePremissionError = 0;
                }
            } catch (Exception e) {
                typePremissionError = 0;
                e.printStackTrace();
            }
        }
    }

    @Override
    public void myLocationUpdated(Location location) {
        super.myLocationUpdated(location);
        forcusCamera(myLatLng, false);
        if (elaErrorLocation.getVisibility() == View.VISIBLE)
            elaErrorLocation.setVisibility(View.GONE);
        if (NetworkUtils.isConnected(getActivity())) {
            presenter.getHospitalRadius(myLatLng, SharedUtils.getInstance().getIntValue(Constants.HOSPITAL_TYPE, 1));
            errorLocationCode = 0;
        } else onGetLocationError(Constants.ERROR_NO_INTERNET);
    }

    @Override
    public void onGetLocationError(int code) {
        Log.e(TAG, "onLocationError: " + code);
        //        tránh trường hợp khi fragment destroy khi chưa load dữ liệu xong => view null

        runError(code);

    }

    public void runError(int code) {
        try {
            this.errorLocationCode = code;
            elaErrorLocation.setVisibility(View.VISIBLE);
            messageErrorLocation.setVisibility(View.VISIBLE);
            progressLocation.setVisibility(View.GONE);
            switch (code) {
                case 0:
                    showProgressBar(SEARCH_HOSPITALBY_LATLNG);
                    break;
                case Constants.ERROR_PERMISSION_LOCATION:
                    messageErrorLocation.setText(getString(R.string.error_permission_location));
                    btnErrorLocation.setVisibility(View.VISIBLE);
                    btnErrorLocation.setText("Cấp quyền");
                    break;
                case Constants.ERROR_OFF_LOCATION:
                    messageErrorLocation.setText(getString(R.string.error_off_location));
                    btnErrorLocation.setVisibility(View.VISIBLE);
                    btnErrorLocation.setText("Cài đặt");
                    break;
                case Constants.ERROR_NO_INTERNET:
                    messageErrorLocation.setText(Html.fromHtml(Constants.getMessageErrorByCode(Constants.ERROR_NO_INTERNET)));
                    btnErrorLocation.setVisibility(View.GONE);
                    break;
                case Constants.ERROR_EMPTY:
                    if (list.size() == 0) {
                        messageErrorLocation.setText(Html.fromHtml(getString(R.string.error_emty_search_hospital,
                                MapUtil.getNameTypeHospitalByID(SharedUtils.getInstance().getIntValue(Constants.HOSPITAL_TYPE, 1)))));
                        btnErrorLocation.setVisibility(View.GONE);
                    } else if (MapUtil.getIdByTypeName(list.get(0).getType()) == SharedUtils.getInstance().getIntValue(Constants.HOSPITAL_TYPE, 1)) {
                        elaErrorLocation.setVisibility(View.GONE);
                        rcHospitalMap.setVisibility(View.VISIBLE);
                    } else {
                        presenter.getHospitalRadius(myLatLng, SharedUtils.getInstance().getIntValue(Constants.HOSPITAL_TYPE, 1));
                        setTypeSpinner();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
