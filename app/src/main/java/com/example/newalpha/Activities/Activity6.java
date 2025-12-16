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
import androidx.core.app.ActivityCompat;

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

import java.io.IOException;
import java.util.List;

public class Activity6 extends MasterActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final String LOC = "Nahum Sarig Street";
    private FusedLocationProviderClient fusedLocationClient;

    private GoogleMap gMap;
    private TextView distanceTV;
    private EditText searchED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_6);

        distanceTV = findViewById(R.id.distanceTV);
        searchED = findViewById(R.id.searchED);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setTrafficEnabled(true);
        enableMyLocation();
    }
    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (gMap != null) {
            // Permission is already granted, enable the location layer
            gMap.setMyLocationEnabled(true);
        }
    }

    public void search(View view) {
        String searchString = searchED.getText().toString().trim();
        if (searchString.isEmpty()) {
            searchString = LOC;
        }

        // 1. Check for permissions first
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            enableMyLocation(); // Request permissions if not granted
            Toast.makeText(this, "Location permission needed to perform search.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Get the current location asynchronously
        String finalSearchString = searchString;
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location == null) {
                        Toast.makeText(Activity6.this, "Could not get current location. Please ensure location is enabled.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

                    // 3. Perform geocoding and draw route only AFTER location is found
                    performGeocoding(finalSearchString, currentLatLng);
                });
    }

    private void performGeocoding(String locationName, LatLng currentLatLng) {
        Geocoder geocoder = new Geocoder(Activity6.this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng destinationLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                gMap.addMarker(new MarkerOptions().position(destinationLatLng).title(locationName));

                // Draw the route on the map
                gMap.addPolyline(new PolylineOptions()
                        .add(currentLatLng, destinationLatLng)
                        .width(5)
                        .color(Color.RED));

                // Calculate and display distance
                float[] results = new float[1];
                Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, destinationLatLng.latitude, destinationLatLng.longitude, results);
                float distance = results[0];
                distanceTV.setText(String.format("Distance: %.2f km", distance / 1000));
            } else {
                Toast.makeText(this, "Location not found.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Network error during search.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // Permission was granted, enable the location layer.
                enableMyLocation();
            }
            else
            {
                Toast.makeText(this, "Location permission is required to show your position on the map.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
