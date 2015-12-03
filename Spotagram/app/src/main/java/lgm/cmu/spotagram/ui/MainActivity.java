package lgm.cmu.spotagram.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import lgm.cmu.spotagram.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location location;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        index = 0;

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
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    setCurrentLocation(location);
                }


                // this is used when the location is changed, update the display of location
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    try {
                        location = locationManager.getLastKnownLocation(provider);
                        setCurrentLocation(location);
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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

        // get current location
        if(location != null){
            setCurrentLocation(location);
        }else{
            Toast.makeText(this, "No Location Service!", Toast.LENGTH_SHORT).show();
        }



        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    public void setCurrentLocation(Location location){

        LatLng cur = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(cur).title("Position: " + String.valueOf(index)));
        index++;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cur));
        if (index <= 1)
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
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




}
