package com.muelpatmore.sanfranciscoparking.ui.reservations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muelpatmore.sanfranciscoparking.R;
import com.muelpatmore.sanfranciscoparking.data.realm.realmobjects.RealmReservation;
import com.muelpatmore.sanfranciscoparking.ui.utils.DateUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Samuel on 03/12/2017.
 */

public class ReservationsRecyclerAdapter
        extends RecyclerView.Adapter<ReservationsRecyclerAdapter.ReservationViewHolder> {
    private final static String TAG = "Res-RecyclerAdapter";

    private ArrayList<RealmReservation> reservationList;
    private int row_reservation;
    private Context applicationContext;

    public ReservationsRecyclerAdapter(ArrayList<RealmReservation> reservationList, int row_reservation, Context applicationContext) {
        Log.i(TAG, "ReservationsRecyclerAdapter constructor");
        this.reservationList = reservationList;
        this.row_reservation = row_reservation;
        this.applicationContext = applicationContext;
    }

    @Override
    public ReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(applicationContext)
                .inflate(R.layout.reservation_card, parent, false);
        return new ReservationViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ReservationViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder");
        holder.tvReservationSpaceId.setText(
                "Parking space id: "+reservationList.get(position).getId());

        //holder.tvTimeExpires.setText(DateUtils.dateToString(reservationList.get(position).getReservedUntil()));
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ReservationViewHolder extends RecyclerView.ViewHolder {

        private final static String TAG = "Res-ViewHolder";

        @BindView(R.id.tvReservationSpaceId) TextView tvReservationSpaceId;
        @BindView(R.id.tvTimeExpires) TextView tvTimeExpires;

        public ReservationViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "ReservationViewHolder constructor");
            ButterKnife.bind(this, itemView);
        }
    }
}

