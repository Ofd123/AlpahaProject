package com.example.newalpha.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newalpha.MasterActivity;
import com.example.newalpha.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class Activity6 extends MasterActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final String LOC = "Nahum Sarig Street";
    private FusedLocationProviderClient fusedLocationClient;

    private GoogleMap gMap;
    TextView distanceTV;
    EditText searchED;
    LatLng currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_6);

        distanceTV = findViewById(R.id.distanceTV);
        searchED = findViewById(R.id.searchED);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        enableMyLocation();

        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setTrafficEnabled(true);
        getCurrentLatLng();
    }
    private void enableMyLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        else if (gMap != null)
        {
            gMap.setMyLocationEnabled(true);
        }
    }

    public void getCurrentLatLng() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Toast.makeText(Activity6.this, "Could not get current location. Please ensure location is enabled.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        gMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    }
                });
    }

    public void search(View view) {

        String search = searchED.getText().toString();
        if(search == null || search.isEmpty())
        {
            search = LOC;
        }
        //fusedLocationClient.getLastLocation()
        //      .addOnSuccessListener(this, new OnSuccessListener<Location>() {
        //        @Override
        //      public void onSuccess(Location location) {
        //        //got the last known location
        //      if (location != null)
        //    {
        //      //create a route
        //    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        //  gMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
        //gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
        getCurrentLatLng();
        Geocoder geocoder = new Geocoder(Activity6.this);
        try
        {
            List<Address> addresses = geocoder.getFromLocationName(search, 1);
            if (addresses != null && !addresses.isEmpty())
            {
                Address address = addresses.get(0);
                LatLng destinationLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                gMap.addMarker(new MarkerOptions().position(destinationLatLng).title(search));

                gMap.addPolyline(new PolylineOptions()
                        .add(currentLocation, destinationLatLng)
                        .width(5)
                        .color(Color.RED));

                float[] results = new float[1];
                Location.distanceBetween(currentLocation.latitude, currentLocation.longitude, destinationLatLng.latitude, destinationLatLng.longitude, results);
                float distance = results[0];
                distanceTV.setText("Distance: " + String.format("%.2f", distance / 1000) + " km");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                enableMyLocation();
                // You can optionally call search(null) here to immediately perform the search after permission is granted
                search(null);
            }
            else
            {
                Toast.makeText(this, "Location permission is required to show your position on the map.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
