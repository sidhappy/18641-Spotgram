package lgm.cmu.spotagram.ui;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.model2.Note;
import lgm.cmu.spotagram.request.NearByRequest;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location location;
    private ArrayList<MarkerOptions> markers;

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
                Toast.makeText(getApplicationContext(), "onQueryTextChange:" + queryText, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String queryText) {
                Toast.makeText(getApplicationContext(), "onQueryTextSubmit:" + queryText, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        MenuItemCompat.OnActionExpandListener expandListener = new OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(getApplicationContext(), "onMenuItemActionCollapse", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(getApplicationContext(), "onMenuItemActionExpand", Toast.LENGTH_SHORT).show();
                return true;
            }
        };


        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
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
                intent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(intent);
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


       // mMap.clear();
        mMap.addMarker(new MarkerOptions()
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

}
