package com.muelpatmore.sanfranciscoparking.data.realm.realmobjects;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Samuel on 03/12/2017.
 */

public class RealmReservation extends RealmObject{;
    private int id;
    private String reservedUntil;

    public RealmReservation() {}

    public String getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(String reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public RealmReservation(int id, String reservedUntil) {
        this.id = id;
        this.reservedUntil = reservedUntil;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
