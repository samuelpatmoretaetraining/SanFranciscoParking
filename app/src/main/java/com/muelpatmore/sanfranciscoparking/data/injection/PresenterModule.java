package com.muelpatmore.sanfranciscoparking.data.injection;

/**
 * Created by Samuel on 04/12/2017.
 */

import com.muelpatmore.sanfranciscoparking.data.DataManager;
import com.muelpatmore.sanfranciscoparking.ui.maps.MapsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides MapsPresenter getMapsPresenter() {
        return new MapsPresenter(
                new DataManager());
    }
}
