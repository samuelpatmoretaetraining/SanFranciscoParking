package com.muelpatmore.sanfranciscoparking.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Samuel on 03/12/2017.
 */

public class PreferencesHelper implements PreferencesHelperInterface {

    private static final String PREFERENCES_NAME = "shared preferences";
    private static final String USERNAME = "username";
    private static final String LOCATION = "location";
    private static final LatLng DEFAULT_LOCATION = new LatLng(37.758847, -122.410684);

    private SharedPreferences mSharedPreferences;

    public PreferencesHelper(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void setUsername(String username) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USERNAME, username);
        editor.apply();
    }

    @Override
    public String getUsername() {
        return mSharedPreferences.getString(USERNAME, "Guest");
    }

    @Override
    public void setDefaultLocation(LatLng location) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String latlngString = String.valueOf(location.latitude)+":"+String.valueOf(location.longitude);
        editor.putString(LOCATION, latlngString);
        editor.apply();
    }

    @Override
    public LatLng getLocation() {
        String locationString = mSharedPreferences.getString(LOCATION, null);
        int delimiter = locationString.indexOf(":");
        if (locationString == null || delimiter == -1) {
            return DEFAULT_LOCATION;
        }
        LatLng location = new LatLng(
                Double.valueOf(locationString.substring(0,delimiter-1)),
                Double.valueOf(locationString.substring(delimiter+1)));
        return location;
    }
}
