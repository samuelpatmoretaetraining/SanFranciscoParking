package com.muelpatmore.sanfranciscoparking.data.realm.realmobjects;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Samuel on 03/12/2017.
 */

public class RealmReservation extends RealmObject{
    private int id;
    private Double lat;
    private Double lng;
    private Date reservedUntil;

    public RealmReservation() {}

    public RealmReservation(int id, Double lat, Double lng, Date reservedUntil) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.reservedUntil = reservedUntil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getLocation() {
        return new LatLng(lat, lng);
    }

    public void setLocation(LatLng location) {
        this.lat = location.latitude;
        this.lng = location.longitude;
    }

    public Date getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(Date reservedUntil) {
        this.reservedUntil = reservedUntil;
    }
}
