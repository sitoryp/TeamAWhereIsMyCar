package mlb402.teamawhereismycar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected Button aboutButton;
    protected Button storeLocation;
    protected LocationManager locationListener;

    // variable to hold the location so it can easily be accessed and set after it is stored on SharedPreferences
    protected Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

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
                currentLocation = locationListener.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                TextView testing = (TextView) findViewById(R.id.testing);
                testing.setText(currentLocation.toString());
            }
        });
    }



    //I created this method for all of the setup code for buttons views etc to
    // keep it out of the top mixed in with all of the logic
    private void setupUI(){
        aboutButton = (Button)findViewById(R.id.aboutButton);
        storeLocation = (Button)findViewById(R.id.setLocationButton);

        //adding this to request permission from the user. . .
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }
}
