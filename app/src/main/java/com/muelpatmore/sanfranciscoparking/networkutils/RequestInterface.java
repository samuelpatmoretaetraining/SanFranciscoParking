package com.muelpatmore.sanfranciscoparking.networkutils;

import com.muelpatmore.sanfranciscoparking.NetworkModels.ParkingListModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Samuel on 01/12/2017.
 */

public interface RequestInterface {

    @GET(Network_Constants.PARKING_DATABASE)
    Observable<List<ParkingListModel>> getParkingList();

    @GET(Network_Constants.PARKING_DATABASE+"/{id}")
    Observable<ParkingListModel> getParkingSpot(@Path("id") int groupId);
}
