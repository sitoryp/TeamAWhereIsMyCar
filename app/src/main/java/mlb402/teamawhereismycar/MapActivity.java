package mlb402.teamawhereismycar;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location currentLocation;
    private UiSettings mUiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //getting the intent being passed in from the Main Activity.
        Intent intent = getIntent();
        //setting the current location by retrieving it from the Parcel
        currentLocation = (Location)intent.getParcelableExtra("currentLocation");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // setting the current location by getting the latitude and longitude from the Location object.
        LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        //adding the marker and setting the camera / zoom to the marker.
        mMap.addMarker(new MarkerOptions().position(current).title("My Car is Here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));

        //setting the zoom controls on the screen for a better user experience.
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        
    }
}
