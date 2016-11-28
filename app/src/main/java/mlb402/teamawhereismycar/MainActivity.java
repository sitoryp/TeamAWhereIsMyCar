package mlb402.teamawhereismycar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Parcel;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    protected Button aboutButton;
    protected Button storeLocation;
    protected Button findCar;
    protected LocationManager locationListener;
    protected Parcel parcel;

    // 3 variables required in order to store the location in the shared preferences. It
    // required converting it to JSON string in order to save it.
    public SharedPreferences.Editor preferences;

    private LocationListener listener;

    // variable to hold the location so it can easily be accessed and set after it is stored on SharedPreferences
    protected Location currentLocation;

    protected double currentLatitude;
    protected double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        //checks to see if Shared preferences has the key value we are looking for
        //if it does, it will store that value as the currentLocation.
        if (getPreferences(MODE_PRIVATE).contains("Longitude") && getPreferences(MODE_PRIVATE).contains("Latitude")) {

            currentLatitude = Double.longBitsToDouble(getPreferences(MODE_PRIVATE).getLong("Latitude", 0));
            currentLongitude = Double.longBitsToDouble(getPreferences(MODE_PRIVATE).getLong("Longitude", 0));

        }

        locationListener = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //collects the current location, converts it to JSON string and stores it in SharedPreferences.
        currentLocation = locationListener.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLongitude = location.getLongitude();
                currentLatitude = location.getLatitude();
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
        };

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.about_layout);

            }
        });

        //this method calls the button's click to gather the location
        storeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog();
            }
        });

        findCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLocation.writeToParcel(parcel, 0);
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
//                intent.putExtra("Longitude", currentLocation);
                intent.putExtra("Longitude", currentLongitude);
                intent.putExtra("Latitude", currentLatitude);
                startActivity(intent);
            }
        });
    }



    //I created this method for all of the setup code for buttons views etc to
    // keep it out of the top mixed in with all of the logic
    private void setupUI(){
        aboutButton = (Button)findViewById(R.id.aboutButton);
        storeLocation = (Button)findViewById(R.id.setLocationButton);
        findCar = (Button) findViewById(R.id.findCarButton);
        parcel = Parcel.obtain();
        preferences  = getPreferences(MODE_PRIVATE).edit();

        //adding this to request permission from the user. . .
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }
public void dialog(){
     AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle("Overwrite");
    dialog.setMessage("Selecting Yes will overwrite the previous location. Are you sure you want to continue?");
    dialog.setNegativeButton("No", null);
    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //requesting an update to the location when the button is clicked.
            locationListener.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, listener);
            Toast.makeText(getBaseContext(), "Location is being request requested please wait. . . ", Toast.LENGTH_SHORT).show();


            // put a timer from the requesting location updates until it is saved to ensure there is enough time to get a response.
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {


                            preferences.putLong("Lat", Double.doubleToRawLongBits(currentLatitude));
                            preferences.putLong("Long", Double.doubleToRawLongBits(currentLongitude));

                            Toast.makeText(getBaseContext(), "Your location has been saved!", Toast.LENGTH_LONG).show();

                        }
                    },
                    2000);
        }
    }).create().show();
}
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
