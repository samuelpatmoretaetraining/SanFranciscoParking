package com.muelpatmore.sanfranciscoparking;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Samuel on 01/12/2017.
 */

public class APIService {

    private CompositeDisposable mCompositeDisposable;

    public APIService() {
        mCompositeDisposable = new CompositeDisposable();
    }

    public void clearRequests() {
        mCompositeDisposable.dispose();
        mCompositeDisposable.clear();
    }
}
