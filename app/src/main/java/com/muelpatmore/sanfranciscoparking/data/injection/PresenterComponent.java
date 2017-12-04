package com.muelpatmore.sanfranciscoparking.data.injection;

import com.muelpatmore.sanfranciscoparking.ui.maps.MapsPresenter;
import com.muelpatmore.sanfranciscoparking.ui.maps.MapsView;

import dagger.Component;

/**
 * Created by Samuel on 04/12/2017.
 */

@Component(modules = {PresenterModule.class})

public interface PresenterComponent {
    void inject(MapsView mapsView);
}
