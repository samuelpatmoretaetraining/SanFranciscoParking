package com.muelpatmore.sanfranciscoparking;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.muelpatmore.sanfranciscoparking.NetworkModels.ParkingListModel;
import com.muelpatmore.sanfranciscoparking.NetworkModels.PointModel;
import com.muelpatmore.sanfranciscoparking.messages.IndividualParkingSpotReceived;
import com.muelpatmore.sanfranciscoparking.messages.ParkingSpotsDataReceived;
import com.muelpatmore.sanfranciscoparking.networkutils.ScheduleProvider;
import com.muelpatmore.sanfranciscoparking.networkutils.ServerConnection;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Samuel on 01/12/2017.
 */

public class APIService implements APIServiceInterface {

    public static final String TAG = "APIService";

    private CompositeDisposable mCompositeDisposable;
    private ScheduleProvider mScheduleProvider;

    public APIService() {
        mCompositeDisposable = new CompositeDisposable();
        mScheduleProvider = new ScheduleProvider();


    }

    public void fetchParkingSpots(@NotNull final LatLng location) {
        Log.i(TAG, "fetchParkingSpots");
        final Location user = new Location("user");
        user.setLatitude(location.latitude);
        user.setLongitude(location.longitude);

        mCompositeDisposable.add(
                ServerConnection.getParkingConnection()
                .getParkingList()
                .flatMapIterable(x -> x)
                .filter(s -> {
                    if (
                    s == null) { return false; }
                    LatLng stop = new LatLng(s.getLat(), s.getLng());
                    double distance = SphericalUtil.computeDistanceBetween(location, stop);
                    //Log.i(TAG, ""+s.getId()+" -distance-"+distance+"m");
                    return distance < new Double(250);
                })
                .map(s -> {
                    /*Log.i(TAG, "Conversion to PointModel");*/
                    return new PointModel(
                        new LatLng(
                            s.getLat(),
                            s.getLng()),
                        s.getId(),
                        s.getIsReserved()); })
                .toList()
                .observeOn(mScheduleProvider.ui())
                .subscribeOn(mScheduleProvider.io())
                .subscribe(new Consumer<List<PointModel>>() {
                               @Override
                               public void accept(List<PointModel> pointModelList) throws Exception {
                                   ArrayList<PointModel> pointModelArrayList = new ArrayList<>(pointModelList);
                                   Log.i(TAG, pointModelArrayList.size()+" valid entries found.");
                                   EventBus.getDefault().post(
                                           new ParkingSpotsDataReceived(pointModelArrayList));
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   throwable.printStackTrace();
                               }
                           }
                ));
    }

    public void fetchParkingSpotById(@NotNull final int id) {
        mCompositeDisposable.add(
                ServerConnection.getParkingConnection()
                .getParkingSpot(id)
                .filter(s-> {
                    Log.d(TAG, s.getId()+" : "+s.getName());
                    return true;
                })
                .observeOn(mScheduleProvider.ui())
                .subscribeOn(mScheduleProvider.io())
                .subscribe(new Consumer<ParkingListModel>() {
                    @Override
                    public void accept(ParkingListModel parkingListModel) throws Exception {
                        Log.i(TAG, "Parking space information recieved, id: "+parkingListModel.getId());
                        EventBus.getDefault().post(new IndividualParkingSpotReceived(parkingListModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "Exception thrown in getParkingSpaceById("+id+")");
                        throwable.printStackTrace();
                    }
                }));
    }


    /**
     * Empty and clear current Observable store.
     */
    public void clearRequests() {
        mCompositeDisposable.dispose();
        mCompositeDisposable.clear();
    }
}
