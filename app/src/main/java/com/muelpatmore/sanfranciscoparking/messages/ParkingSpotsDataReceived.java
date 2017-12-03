package com.muelpatmore.sanfranciscoparking.messages;

import com.muelpatmore.sanfranciscoparking.NetworkModels.PointModel;

import java.util.ArrayList;

/**
 * Created by Samuel on 01/12/2017.
 */

public class ParkingSpotsDataReceived {
    private ArrayList<PointModel> points;

    public ParkingSpotsDataReceived(ArrayList<PointModel> points) {
        this.points = points;
    }

    public ArrayList<PointModel> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<PointModel> points) {
        this.points = points;
    }
}
