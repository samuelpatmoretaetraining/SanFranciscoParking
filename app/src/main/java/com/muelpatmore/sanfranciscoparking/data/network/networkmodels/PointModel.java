package com.muelpatmore.sanfranciscoparking.data.network.networkmodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Samuel on 01/12/2017.
 */

public class PointModel implements Parcelable {
    private LatLng location;
    private double lat, lng;
    private int id;
    private boolean isReserved;

    public PointModel(LatLng location, int id, boolean isReserved) {
        this.location = location;
        this.id = id;
        this.isReserved = isReserved;
        this.lat = location.latitude;
        this.lng = location.longitude;
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

    public Double getLng() {
        return lng;
    }

    public Double getLat() {
        return lat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private PointModel(Parcel in) {
        double[] loc = {0.0};
        loc = in.createDoubleArray();
        lat = loc[0];
        lng = loc[1];
        location = new LatLng(lat, lng);
        isReserved = in.readByte() != 0;
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        double[] loc = {lat, lng};
        dest.writeDoubleArray(loc);
        dest.writeInt(id);
        dest.writeByte((byte) (isReserved ? 1 : 0));
    }

    public static final Parcelable.Creator<PointModel> CREATOR
            = new Parcelable.Creator<PointModel>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public PointModel createFromParcel(Parcel in) {
            return new PointModel(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public PointModel[] newArray(int size) {
            return new PointModel[size];
        }
    };
}
