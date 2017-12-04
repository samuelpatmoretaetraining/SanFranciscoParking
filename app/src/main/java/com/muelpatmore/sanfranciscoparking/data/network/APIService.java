package com.muelpatmore.sanfranciscoparking.data.network;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.muelpatmore.sanfranciscoparking.data.messages.ParkingSpotReservedConfirmation;
import com.muelpatmore.sanfranciscoparking.data.messages.IndividualParkingSpotReceived;
import com.muelpatmore.sanfranciscoparking.data.messages.ParkingSpotsDataReceived;
import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.ParkingSpaceModel;
import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.PointModel;
import com.muelpatmore.sanfranciscoparking.data.network.networkutils.ServerConnection;
import com.muelpatmore.sanfranciscoparking.ui.utils.rx.SchedulerProvider;

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

    private static final String TAG = "APIService";

    private final CompositeDisposable mCompositeDisposable;
    private final SchedulerProvider mScheduleProvider;

    public APIService() {
        mCompositeDisposable = new CompositeDisposable();
        mScheduleProvider = new SchedulerProvider();


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
                    return distance < 350.0f;
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
                    public void accept(List<PointModel> pointModels) throws Exception {
                        ArrayList<PointModel> pointModelArrayList = new ArrayList<>(pointModels);
                        Log.i(TAG, pointModelArrayList.size() + " valid entries found.");
                        EventBus.getDefault().post(new ParkingSpotsDataReceived(pointModelArrayList));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    public void fetchParkingSpotById(final int id) {
        mCompositeDisposable.add(
                ServerConnection.getParkingConnection()
                .getParkingSpot(id)
                .filter(s-> {
                    Log.d(TAG, s.getId()+" : "+s.getName());
                    return true;
                })
                .observeOn(mScheduleProvider.ui())
                .subscribeOn(mScheduleProvider.io())
                .subscribe(parkingSpaceModel -> {
                    Log.i(TAG, "Parking space information received, id: "+parkingSpaceModel.getId());
                    EventBus.getDefault().post(new IndividualParkingSpotReceived(parkingSpaceModel));
                }, throwable -> {
                    Log.i(TAG, "Exception thrown in getParkingSpaceById("+id+")");
                    throwable.printStackTrace();
                }));
    }

    public void reserveParkingSpot( final int id, @NotNull final ParkingSpaceModel parkingSpaceModel) {
        mCompositeDisposable.add(
                ServerConnection.getParkingConnection()
                        /*.reserveParkingSpot(id, reservationStatus)*/
                        .reserveParkingSpot(id, parkingSpaceModel)
                        .observeOn(mScheduleProvider.ui())
                        .subscribeOn(mScheduleProvider.io())
                        .subscribe(parkingSpaceModel1 -> {
                            Log.i(TAG, "Parking spot "+ parkingSpaceModel1.getId()+" reserved.");
                            EventBus.getDefault().post(
                                    new ParkingSpotReservedConfirmation(
                                            parkingSpaceModel1.getIsReserved(),
                                            parkingSpaceModel1.getReservedUntil(),
                                            parkingSpaceModel.getId()));
                        }, throwable -> {
                            Log.i(TAG, "Exception thrown in reserveParkingSpot("+id+")");
                            throwable.printStackTrace();
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
