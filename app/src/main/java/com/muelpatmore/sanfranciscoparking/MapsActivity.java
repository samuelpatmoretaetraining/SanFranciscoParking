package com.muelpatmore.sanfranciscoparking;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muelpatmore.sanfranciscoparking.NetworkModels.ParkingListModel;
import com.muelpatmore.sanfranciscoparking.NetworkModels.PointModel;
import com.muelpatmore.sanfranciscoparking.messages.ParkingSpotsDataRecieved;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_CYAN;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapsActivity";
    private static final LatLng DEFAULT_LOCATION = new LatLng(37.779062, -122.408523);
    private static final LatLngBounds SAN_FRANCISCO = new LatLngBounds(
            new LatLng(37.692100, -122.521307), new LatLng(37.813489, -122.354833));

    private DataManager mDataManager;
    private GoogleMap mMap;

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

        // test API
        mDataManager.fetchParkingSpots(userLocation);

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
    }

    private void plotAndFocusOnUser() {
        mMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title("User location")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(HUE_CYAN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ParkingSpotsDataRecieved event) {
        Log.i(TAG, "ParkingSpotsDataRecieved, size: "+event.getPoints().size());
        plotMapMarkers(event.getPoints());
    };

    private void plotMapMarkers(ArrayList<PointModel> pointList) {
        // clear all markers on the map
        mMap.clear();
        for (PointModel r : pointList) {
            float color = r.isReserved() ? BitmapDescriptorFactory.HUE_GREEN : BitmapDescriptorFactory.HUE_ROSE;
            String snippet = r.isReserved() ? "Reserved" : "Empty";
            mMap.addMarker(new MarkerOptions()
                    .position(r.getLocation())
                    .title(String.valueOf(r.getId()))
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(color)));

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

        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    }
}
