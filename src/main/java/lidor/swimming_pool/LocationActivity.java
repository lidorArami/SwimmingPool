package lidor.swimming_pool;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static lidor.swimming_pool.R.id.map;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);

        if(isMapServiceOk())
        {
            mapFragment.getMapAsync(this);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission
                    .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    public boolean isMapServiceOk()
    {
        int available=GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(available==ConnectionResult.SUCCESS) return true;

        else if(GooglePlayServicesUtil.isUserRecoverableError(available))
        {
            Dialog d=GooglePlayServicesUtil.getErrorDialog(available,this,9001);
            d.show();
        }
        return false;
    }

    public void addAnnotation(double longitude, double latitude)
    {
        LatLng place = new LatLng(longitude, latitude);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,14));
        mMap.addMarker(new MarkerOptions().title("Dizingof").snippet("The Most Popular ...").position(place));

    }

    public void setNewLocation(LatLng l)
    {
        CameraUpdate c = CameraUpdateFactory.newLatLng(l);
        mMap.moveCamera(c);
    }

    public void setNewLocationWithZoom(LatLng l, int zoom)
    {
        CameraUpdate c = CameraUpdateFactory.newLatLngZoom(l,zoom);
        mMap.moveCamera(c);
    }

    public boolean isGoogleApiAvailabe() {

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int Available = api.isGooglePlayServicesAvailable(this);

        if (Available == ConnectionResult.SUCCESS) return true;
        else if (api.isUserResolvableError(Available))
        {
            Dialog dialog = api.getErrorDialog(this, Available, 0);
            dialog.show();
        }
        else Toast.makeText(this, "Dont have google maps service", Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
