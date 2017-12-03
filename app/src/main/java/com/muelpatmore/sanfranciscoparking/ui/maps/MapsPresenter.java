package com.muelpatmore.sanfranciscoparking.ui.maps;

/**
 * Created by Samuel on 03/12/2017.
 */

public class MapsPresenter<V extends MapsViewInterface> implements MapsPresenterInterface<V> {
    private MapsViewInterface view;

    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    @Override
    public void onDetach() {

    }
}
