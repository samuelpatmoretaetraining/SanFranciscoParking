package com.muelpatmore.sanfranciscoparking.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Samuel on 03/12/2017.
 */

public interface PreferencesHelperInterface {

    void setUsername(String username);

    String getUsername();

    void setDefaultLocation(LatLng location);

    LatLng getLocation();
}
