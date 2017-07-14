package an.favlistapp.app;

import android.app.Application;

import an.favlistapp.receiver.NetworkConnectionReceiver;

/**
 * Created by sahitya on 14/7/17.
 */

public class CustomApplication extends Application {

    private static CustomApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized CustomApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(NetworkConnectionReceiver.NetworkConnectionReceiverListener listener) {
        NetworkConnectionReceiver.connectivityReceiverListener = listener;
    }
}
