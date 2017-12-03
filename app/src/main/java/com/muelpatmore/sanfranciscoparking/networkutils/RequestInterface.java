package com.muelpatmore.sanfranciscoparking.networkutils;

import com.muelpatmore.sanfranciscoparking.NetworkModels.ParkingSpaceModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Samuel on 01/12/2017.
 */

public interface RequestInterface {

    @GET(Network_Constants.PARKING_DATABASE)
    Observable<List<ParkingSpaceModel>> getParkingList();

    @GET(Network_Constants.PARKING_DATABASE+"/{id}")
    Observable<ParkingSpaceModel> getParkingSpot(@Path("id") int id);

    @PUT(Network_Constants.PARKING_DATABASE+"/{id}")
    Observable<ParkingSpaceModel> reserveParkingSpot(@Path("id") int id, @Body ParkingSpaceModel parkingSpaceModel);
}
