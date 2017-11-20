package wiman.sampleapp;

import android.app.Application;


import me.wiman.listener.WimanSDK;
import me.wiman.logger.Logger;

/**
 * Created by vincenzosarnataro on 17/11/17.
 */

public class SampleAppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        NotificationsAgent.with(this).SDKlistener();

        Logger.setLevel(Logger.Level.DEBUG);
        Logger.setEnablePostLog(true);

        /* For generate key visit https://developers.wiman.me/console/login */
        WimanSDK.initialize(this, "YOUR WIMAN SDK KEY");
    }
}
