package com.muelpatmore.sanfranciscoparking.networkmodels;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Samuel on 01/12/2017.
 */

public class PointModel {
    private LatLng location;
    private int id;
    private boolean isReserved;

    public PointModel(LatLng location, int id, boolean isReserved) {
        this.location = location;
        this.id = id;
        this.isReserved = isReserved;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public Double getLon() {
        return location.longitude;
    }

    public Double getLat() {
        return location.latitude;
    }
}
