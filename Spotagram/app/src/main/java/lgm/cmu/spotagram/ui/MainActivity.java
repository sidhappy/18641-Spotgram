package lgm.cmu.spotagram.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;


import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.model2.Note;
import lgm.cmu.spotagram.request.NearByRequest;
import lgm.cmu.spotagram.utils.ConstantValue;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location location;
    private ArrayList<MarkerOptions> markers;
    private Geocoder geoCoder;

    private static final String TAG = "MAIN_ACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        markers = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geoCoder = new Geocoder(this, Locale.ENGLISH);


        // set location manager and location
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            }
        } catch (SecurityException se){

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need to send the location
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);

                if (location != null) {
                    intent.putExtra("latitude", String.valueOf(location.getLatitude()));
                    intent.putExtra("longitude", String.valueOf(location.getLongitude()));
                }
                startActivity(intent);
            }
        });


        try {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,10000,10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // disable location update
                    //setCurrentLocation(location);
                }


                // this is used when the location is changed, update the display of location
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    try {
                        //location = locationManager.getLastKnownLocation(provider);
                        //setCurrentLocation(location);
                    } catch (SecurityException se){

                    }
                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } catch (SecurityException se){

        }

        // overflow button show
        setOverflowShowingAlways();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String queryText) {
                //Toast.makeText(getApplicationContext(), "onQueryTextChange:" + queryText, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String queryText) {
                // Toast.makeText(getApplicationContext(), "onQueryTextSubmit:" + queryText, Toast.LENGTH_SHORT).show();

                String add = queryText;
                //String add = "Times Square";

                final String addressUrl = "http://maps.googleapis.com/maps/api/geocode/json?address="
                        + URLEncoder.encode(add.replaceAll(" ", "+"))
                        + "&sensor=true";

                new GetLocationNameRequest(mMap).execute(addressUrl);


                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//得到InputMethodManager的实例
                if (imm.isActive()) {
//如果开启
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
                }

                return true;
            }


        });

        MenuItemCompat.OnActionExpandListener expandListener = new OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //Toast.makeText(getApplicationContext(), "onMenuItemActionCollapse", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //Toast.makeText(getApplicationContext(), "onMenuItemActionExpand", Toast.LENGTH_SHORT).show();
                return true;
            }
        };


        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);

        return super.onCreateOptionsMenu(menu);
    }



    public void setLoction(double lat, double lon){
        location.setLatitude(lat);
        location.setLatitude(lon);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_search:
                //Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_nearby:
                intent = new Intent(MainActivity.this, NearByActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_aboutme:
                // Set a Dialog to show ABout
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        UiSettings msettings = mMap.getUiSettings();

        msettings.setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
        msettings.setMyLocationButtonEnabled(true);
        msettings.setMapToolbarEnabled(false);
        msettings.setIndoorLevelPickerEnabled(true);



        // get current location
        if(location != null){
            setCurrentLocation(location);
        }else{
            Toast.makeText(this, "No Location Service!", Toast.LENGTH_SHORT).show();
        }

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                if(marker.isInfoWindowShown())
                    marker.hideInfoWindow();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                location.setLatitude(marker.getPosition().latitude);
                location.setLongitude(marker.getPosition().longitude);


                CameraPosition cp = new CameraPosition.Builder()
                        .target(marker.getPosition())
                        .zoom(15)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));



                marker.showInfoWindow();
            }
        });


        // Create different marker from database
        initMarkers();


        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    private void initMarkers(){
        NearByRequest request = new NearByRequest(10, 10, 800000, 100, 0);
        request.setOnNoteReadyListener(new NearByRequest.OnNoteReadyListener() {
            @Override
            public void onNoteReady(boolean isSuccess, List<Note> notes) {
                if (isSuccess) {
                    // add locations to map
                    for (Note n : notes) {
                        markers.add(new MarkerOptions()
                                .position(new LatLng(n.getLatitude(), n.getLongitude()))
                                .title(n.getContent())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        );
                    }

                    for (MarkerOptions mo: markers){
                        mMap.addMarker(mo);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Network err", Toast.LENGTH_SHORT).show();
                }
            }
        });

        request.execute();
    }


    public void setCurrentLocation(Location location){

        LatLng cur = new LatLng(location.getLatitude(),location.getLongitude());
        this.location = location;

       // mMap.clear();

        Marker curMarker = mMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(cur)
                .title("You are HERE"));

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(cur));
        // mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        CameraPosition cp = new CameraPosition.Builder()
                .target(cur)
                .zoom(15)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp), 3000, null);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        setCurrentLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public class GetLocationNameRequest extends AsyncTask<String, Void, Double[]> {

        InputStream is = null;

        private GoogleMap mMap;

        // constructor
        public GetLocationNameRequest(GoogleMap mMap) {
            this.mMap = mMap;
        }

        @Override
        protected Double[] doInBackground(String... urls) {

            Double[] rtn = new Double[2];

            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpPost = new HttpGet(urls[0]);

                HttpResponse getResponse = httpClient.execute(httpPost);
                final int statusCode = getResponse.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    Log.w(getClass().getSimpleName(),
                            "Error " + statusCode + " for URL " + urls[0]);
                    return null;
                }

                HttpEntity getResponseEntity = getResponse.getEntity();

                //HttpResponse httpResponse = httpClient.execute(httpPost);
                //HttpEntity httpEntity = httpResponse.getEntity();
                is = getResponseEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("IO", e.getMessage().toString());
                e.printStackTrace();

            }

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                String page = sb.toString();
                org.json.JSONObject jsonObject = new org.json.JSONObject(page);
                if (jsonObject.has("results")) {
                    JSONArray jsonArray = (JSONArray) jsonObject.get("results");

                    if (jsonArray.length() > 0) {
                        jsonObject = (org.json.JSONObject) jsonArray.get(0);
                        if (jsonObject.has("geometry")) {
                            jsonObject = (org.json.JSONObject) jsonObject
                                    .get("geometry");

                            if (jsonObject.has("location")) {
                                org.json.JSONObject location = (org.json.JSONObject) jsonObject
                                        .get("location");


                                rtn[0] = (Double) location.get("lat");
                                rtn[1] = (Double) location.get("lng");

                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            // return JSON String
            return rtn;

        }

        protected void onPostExecute(Double[] loc)
        {
            //onPostExecute
            setLoction(loc[0],loc[1]);

            LatLng cur = new LatLng(loc[0],loc[1]);

            // mMap.clear();

            Marker curMarker = mMap.addMarker(new MarkerOptions()
                    .draggable(true)
                    .position(cur)
                    .title("You are HERE"));

            // mMap.moveCamera(CameraUpdateFactory.newLatLng(cur));
            // mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

            CameraPosition cp = new CameraPosition.Builder()
                    .target(cur)
                    .zoom(15)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp), 3000, null);

        }

    }


}
