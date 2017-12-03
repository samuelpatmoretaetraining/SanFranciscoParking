package com.muelpatmore.sanfranciscoparking;

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
import com.muelpatmore.sanfranciscoparking.NetworkModels.ParkingListModel;
import com.muelpatmore.sanfranciscoparking.NetworkModels.PointModel;
import com.muelpatmore.sanfranciscoparking.messages.IndividualParkingSpotReceived;
import com.muelpatmore.sanfranciscoparking.messages.ParkingSpotsDataReceived;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_CYAN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapsActivity";
    private static final LatLng DEFAULT_LOCATION = new LatLng(37.779062, -122.408523);
    private static final LatLngBounds SAN_FRANCISCO = new LatLngBounds(
            new LatLng(37.692100, -122.521307), new LatLng(37.813489, -122.354833));

    private DataManager mDataManager;
    private GoogleMap mMap;
    private Marker userMarker;

    private LatLng userLocation = DEFAULT_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_maps);
        mDataManager = new DataManager();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        fetchParkingSpacesNearUser();
    }

    private void plotAndFocusOnUser() {
        userMarker = mMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title(getResources().getString(R.string.user_location))
                .zIndex(1)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(60f))
                .draggable(true));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//        mMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
        CameraPosition newCamPos = new CameraPosition(userLocation,
                17f,
                mMap.getCameraPosition().tilt, //use old tilt
                mMap.getCameraPosition().bearing); //use old bearing
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 4000, null);

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
                fetchParkingSpacesNearUser();
            }
        });
    }

    private void fetchParkingSpacesNearUser() {
        mMap.clear();
        mDataManager.fetchParkingSpots(userLocation);
        plotAndFocusOnUser();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ParkingSpotsDataReceived event) {
        Log.i(TAG, "ParkingSpotsDataReceived, size: "+event.getPoints().size());
        plotMapMarkers(event.getPoints());
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IndividualParkingSpotReceived event) {
        Log.i(TAG, "IndividualParkingSpotsReceived, id: "+event.getParkingSpot().getId());
        showParkingDetailsSnackbar(event.getParkingSpot());
    };

    private void plotMapMarkers(ArrayList<PointModel> pointList) {
        // clear all markers on the map
        mMap.clear();
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
        EventBus.getDefault().unregister(this);
        mDataManager.onStop();
    }



    @Override
    public void onInfoWindowClick(Marker marker) {
        //ToDo change this to z-indez for faster results.
        if (! marker.getTitle().equals(getResources().getString(R.string.user_location))) {
            mDataManager.fetchParkingSpotById(Integer.valueOf(marker.getTitle()));
        }
        //Toast.makeText(this, "Info window clicked",Toast.LENGTH_SHORT).show();
    }

    private void showParkingDetailsSnackbar(ParkingListModel parkingSpace) {
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Test", 8000);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the default Snackbar TextView
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View snackView = inflater.inflate(R.layout.parking_info_bubble, null);

        // Configure the view
        Log.i(TAG, "posting parking space details to snackbar "+parkingSpace.getId());
        TextView tvParkingSpaceId = (TextView) snackView.findViewById(R.id.tvParkingSpaceId);
        Log.i(TAG,tvParkingSpaceId.getText().toString());
        tvParkingSpaceId.setText(parkingSpace.getId().toString());


        TextView tvParkingSpaceCoords = (TextView) snackView.findViewById(R.id.tvParkingSpaceCoords);
        String parkingSpaceCoords = Double.toString(parkingSpace.getLat())+ ", "+ Double.toString(parkingSpace.getLng());
        tvParkingSpaceCoords.setText(parkingSpaceCoords);

        Button btnParkingSpaceReserve = (Button) snackView.findViewById(R.id.btnParkingSpaceReserve);
        if (parkingSpace.getIsReserved()) {
            String reservedUntil = parkingSpace.getReservedUntil();
            reservedUntil = reservedUntil.substring(11,reservedUntil.length()-11);
            btnParkingSpaceReserve.setText("Reserved"+System.getProperty("line.separator")+"free at "+ reservedUntil);
            btnParkingSpaceReserve.setBackgroundColor(getResources().getColor(R.color.colorButtonDisabled));
        } else {
            btnParkingSpaceReserve.setText("Click to reserve this space.");
            btnParkingSpaceReserve.setBackgroundColor(getResources().getColor(R.color.colorButtonActive));
        }

        // Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
        layout.setPadding(0, 0, 0, 0);
        // Show the Snackbar
        snackbar.show();
    }
}
