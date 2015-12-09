package lgm.cmu.spotagram.utils;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by dawang on 12/7/15.
 */
public class MapUtils {

    public static void setMapFreeze(GoogleMap map){
        UiSettings msettings = map.getUiSettings();

        msettings.setAllGesturesEnabled(false);
        msettings.setMyLocationButtonEnabled(false);
        msettings.setMapToolbarEnabled(false);
        msettings.setIndoorLevelPickerEnabled(false);
        msettings.setZoomControlsEnabled(false);

    }

    public static void showPosMarkers(GoogleMap map, double lat, double lon, ArrayList<MarkerOptions> markers){
        map.clear();
        LatLng cur = new LatLng(lat,lon);

        CameraPosition cp = new CameraPosition.Builder()
                .target(cur)
                .zoom(15)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cp), 3000, null);

        // init markers
        for (MarkerOptions mo: markers){
            map.addMarker(mo);
        }
    }

}
