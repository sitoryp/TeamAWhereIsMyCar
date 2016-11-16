package mlb402.teamawhereismycar;

import android.Manifest;
import android.content.Context;
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
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    protected Button aboutButton;
    protected Button storeLocation;
    protected Button findCar;
    protected LocationManager locationListener;
    protected Parcel parcel;

    // 3 variables required in order to store the location in the shared preferences. It
    // required converting it to JSON string in order to save it.
    public SharedPreferences.Editor preferences;
    private Gson gson;
    private String jsonLocation;

    // variable to hold the location so it can easily be accessed and set after it is stored on SharedPreferences
    protected Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        //checks to see if Shared preferences has the key value we are looking for
        //if it does, it will store that value as the currentLocation.
        if (getPreferences(MODE_PRIVATE).contains("currentLocation")){
            // getting the JSON sting back from the shared preferences.
            jsonLocation = getPreferences(MODE_PRIVATE).getString("currentLocation", "");
            // converting the JSON string back to the Location object to be used in the app.
            currentLocation = gson.fromJson(jsonLocation, Location.class);
            // printing the restored Location currentLocation to the testing TextView to verify. these 2 lines will be removed.
            TextView testing = (TextView) findViewById(R.id.testing);
            testing.setText(currentLocation.toString());
        }

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
                //setting the LocationManager and the services needed to get the location,
                // also checking against user accepted permissions.
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
                jsonLocation = gson.toJson(currentLocation);
                preferences.putString("currentLocation", jsonLocation).apply();

                //testing that the location was retrieved when the button was clicked. these 2 lines
                // will be removed.
                TextView testing = (TextView) findViewById(R.id.testing);
                testing.setText(currentLocation.toString());
            }
        });

        findCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLocation.writeToParcel(parcel, 0);
                Intent intent = new Intent(MainActivity.this, FindCarActivity.class);
                intent.putExtra("currentLocation", currentLocation);
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
        gson = new Gson();

        //adding this to request permission from the user. . .
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }
    public void onBackPressed(){
        setContentView(R.layout.activity_main);
    }
}
