package com.muelpatmore.sanfranciscoparking.ui.utils.rx;

import io.reactivex.Scheduler;

/**
 * Created by hrskrs on 5/8/2017.
 */

public interface SchedulerProviderInterface {

    Scheduler ui();

    Scheduler computation();

    Scheduler io();

}
