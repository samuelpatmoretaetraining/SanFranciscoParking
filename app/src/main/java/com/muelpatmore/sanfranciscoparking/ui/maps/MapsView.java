package com.muelpatmore.sanfranciscoparking.ui.maps;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muelpatmore.sanfranciscoparking.data.DataManager;
import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.ParkingSpaceModel;
import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.PointModel;
import com.muelpatmore.sanfranciscoparking.ui.utils.DateUtils;
import com.muelpatmore.sanfranciscoparking.R;
import com.muelpatmore.sanfranciscoparking.data.messages.ParkingSpotReservedConfirmation;
import com.muelpatmore.sanfranciscoparking.data.messages.IndividualParkingSpotReceived;
import com.muelpatmore.sanfranciscoparking.data.messages.ParkingSpotsDataReceived;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MapsView extends FragmentActivity implements
        MapsViewInterface,
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapsView";
    private static final LatLng DEFAULT_LOCATION = new LatLng(37.779062, -122.408523);
    private static final LatLngBounds SAN_FRANCISCO = new LatLngBounds(
            new LatLng(37.692100, -122.521307), new LatLng(37.813489, -122.354833));

    private MapsPresenter mMapsPresenter;
    private GoogleMap mMap;
    private Marker userMarker;
    private ArrayList<PointModel> pointList;

    private LatLng userLocation = DEFAULT_LOCATION;

    /**
     * Initialisation of initial state, rebuilt from savedInstance if it exists. Map fragment
     * created and initialised, userLocation checked against city limits.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        pointList = new ArrayList<>();
        mMapsPresenter = new MapsPresenter();
        if (savedInstanceState != null) {
            pointList = savedInstanceState.getParcelableArrayList("marker list");
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (savedInstanceState == null) {
            // First incarnation of this activity.
            mapFragment.setRetainInstance(true);
        }
        if (userLocation == null || !SAN_FRANCISCO.contains(userLocation)) {
            userLocation = DEFAULT_LOCATION;
            Log.e(TAG, "User location outside SF, resetting to default.");
            Toast.makeText(this, "User location outside SF, resetting to default.", Toast.LENGTH_SHORT).show();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapsPresenter.onAttach(this);
    }

    @Override
    public LatLng getUserLocation() {
        return userLocation;
    }

    /**
     * Set new location of user.
     * Will reset to default if the position is null or outside the bounds of San Francisco.
     */
    @Override
    public void setUserLocation(LatLng userLocation) {
        if (userLocation != null && SAN_FRANCISCO.contains(userLocation)) {
            this.userLocation = userLocation;
        } else {
            this.userLocation = DEFAULT_LOCATION;
            Log.e(TAG, "User location outside SF, resetting to default.");
            Toast.makeText(this, "User location outside SF, resetting to default.", Toast.LENGTH_SHORT).show();
        }
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

        // Restrict map to San Francisco
        mMap.setLatLngBoundsForCameraTarget(SAN_FRANCISCO);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnInfoWindowClickListener(this);

        mMapsPresenter.fetchParkingSpacesNear(userLocation);

    }

    public GoogleMap getMap() {
        return mMap;
    }

    /**
     * Plot location of user on map as a gold marker. Marker can be dragged to change the user's
     * chosen location and refresh the map.
     *
     * User location is reset to application default value if it is null or outside the set bounds
     * of San Francisco.
     */
    @Override
    public void plotAndFocusOnUser() {
        userMarker = mMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title(getResources().getString(R.string.user_location))
                .zIndex(1)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(60f))
                .draggable(true));

        mMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
        CameraPosition newCamPos = new CameraPosition(userLocation,
                17f,
                mMap.getCameraPosition().tilt, //use old tilt
                mMap.getCameraPosition().bearing); //use old bearing
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 3000, null);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                userLocation = marker.getPosition();
                mMapsPresenter.fetchParkingSpacesNear(marker.getPosition());
            }
        });
    }

    /**
     * Plots a list of PointModel objects representing parking spaces to the map as custom markers.
     * All spaces display the Parking Spot id in an info bubble on click along with their reserved
     * status. Free spaces are green while reserved spaces are red and partially transparent.
     *
     * @param pointList Set of PointModel objects to plot on map.
     */
    public void plotMapMarkers(List<PointModel> pointList) {
        // clear all markers on the map
        mMap.clear();
        this.pointList.clear();
        this.pointList = new ArrayList<>(pointList);
        for (PointModel r : pointList) {
            float color, alpha;
            String snippet;
            if (r.isReserved()) {
                snippet = "Reserved";
                color = BitmapDescriptorFactory.HUE_RED;
                alpha = 0.5f;
            } else {
                snippet = "Click to reserve";
                color = BitmapDescriptorFactory.HUE_GREEN;
                alpha = 1f;
            }

            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(r.getLocation())
                    .title(String.valueOf(r.getId()))
                    .snippet(snippet)
                    .alpha(alpha)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(color)));
            Log.i(TAG, m.getTitle());
        }
        plotAndFocusOnUser();
    }

    public void onStop() {
        super.onStop();
        mMapsPresenter.onDetach();
    }

    public Context getContext() {
        return this.getContext();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // React to all markers except user location (z-index of 1)
        if (marker.getZIndex() == 0) {
            mMapsPresenter.parkingSpaceDetailsRequested(Integer.valueOf(marker.getTitle()));
        }

    }

    public void showParkingSpaceDetails(ParkingSpaceModel parkingSpace) {
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Test", 8000);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the default Snackbar TextView
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View snackView = inflater.inflate(R.layout.parking_info_bubble, null);

        // Configure the view
        Log.i(TAG, "posting parking space details to Snackbar "+parkingSpace.getId());
        TextView tvParkingSpaceId = (TextView) snackView.findViewById(R.id.tvParkingSpaceId);
        Log.i(TAG,tvParkingSpaceId.getText().toString());
        tvParkingSpaceId.setText(parkingSpace.getId().toString());


        TextView tvParkingSpaceCoords = (TextView) snackView.findViewById(R.id.tvParkingSpaceCoords);
        String parkingSpaceCoords = Double.toString(parkingSpace.getLat())+ ", "+ Double.toString(parkingSpace.getLng());
        tvParkingSpaceCoords.setText(parkingSpaceCoords);

        Button btnParkingSpaceReserve = (Button) snackView.findViewById(R.id.btnParkingSpaceReserve);
        if (parkingSpace.getIsReserved()) {
            String reservedUntil = parkingSpace.getReservedUntil();
            if (reservedUntil.indexOf(".") != -1) {
                // account for outlier date strings
                reservedUntil = reservedUntil.substring(11,reservedUntil.indexOf("."));
            }
            btnParkingSpaceReserve.setText("Reserved"+System.getProperty("line.separator")+"free at "+ reservedUntil);
            btnParkingSpaceReserve.setBackgroundColor(getResources().getColor(R.color.colorButtonDisabled));
        } else {
            btnParkingSpaceReserve.setText("Click to reserve this space.");
            btnParkingSpaceReserve.setBackgroundColor(getResources().getColor(R.color.colorButtonActive));
        }

        btnParkingSpaceReserve.setOnClickListener(v -> {
            parkingSpace.setIsReserved(true);
            //ToDo add ability for user to select duration of stay.
            String reservedUntilString = DateUtils.dateStringFromNow(60); // Default reservation duration of 1 hour
            Log.i(TAG, "new reserved until value: "+reservedUntilString);
            parkingSpace.setReservedUntil(reservedUntilString);
            mMapsPresenter.reserveParkingSpot(parkingSpace.getId(), parkingSpace);
        });

        // Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
        layout.setPadding(0, 0, 0, 0);
        // Show the Snackbar
        snackbar.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void openActivityOnTokenExpire() {

    }

    @Override
    public void onError(int resId) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void showMessage(String message) {
        Log.i(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void hideKeyboard() {

    }
}
