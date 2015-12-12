package lgm.cmu.spotagram.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by yulei on 2015/12/11.
 */
public class LocationService extends Service {
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private Location mLocation;

    private MyBinder mBinder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // set location manager and location
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            mLocation = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
            if (mLocation == null) {
                mLocation = mLocationManager.getLastKnownLocation(mLocationManager.NETWORK_PROVIDER);
            }
        } catch (SecurityException se){

        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestory() {
        super.onDestroy();;
    }

    public Location getLocation() {
        return mLocation;
    }

    public class MyBinder extends Binder {

        public LocationService getService() {
            return LocationService.this;
        }


    }
}
