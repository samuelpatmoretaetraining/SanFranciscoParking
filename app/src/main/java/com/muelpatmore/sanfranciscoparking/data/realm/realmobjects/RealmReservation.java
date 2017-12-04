package com.muelpatmore.sanfranciscoparking.data.realm.realmobjects;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Samuel on 03/12/2017.
 */

public class RealmReservation extends RealmObject{
    @PrimaryKey
    private String timestamp;
    private int id;
    private Date reservedUntil;

    public RealmReservation() {}

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public RealmReservation(int id, Date reservedUntil) {
        this.id = id;
        this.reservedUntil = reservedUntil;
        timestamp = String.valueOf(System.currentTimeMillis());

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(Date reservedUntil) {
        this.reservedUntil = reservedUntil;
    }
}
